package ru.Game;

import ru.Interfaces.Game;
import ru.Interfaces.Player;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by MManiagin on 29.06.2017.
 */
public class TicTacToeGame implements Game{
    private Set<PrintWriter> writers = Collections.synchronizedSet(new HashSet<PrintWriter>());
    private TicTacToePlayer player1;
    private TicTacToePlayer player2;
    private boolean roundEnd;
    private String str;

    private char[][] gamePole = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};


    public TicTacToeGame(Socket socket, Socket socket1)throws IOException {
        player1=new TicTacToePlayer(socket, 'X');
        player2=new TicTacToePlayer(socket1, '0');
        roundEnd=false;
        PrintWriter out1 =new PrintWriter(new OutputStreamWriter(player1.getSocket().getOutputStream()), true);
        PrintWriter out2 = new PrintWriter(new OutputStreamWriter(player2.getSocket().getOutputStream()), true);
        writers.add(out1);
        writers.add(out2);

    }

    public void run() {
        try{
        while (!roundEnd){
            play(player1);
            play(player2);
        }
        }
      finally {
            sendMessage("Game over");
        }
        roundEnd=true;
    }

    private void play(TicTacToePlayer player){
        if(player1.isWin()==false&&player2.isWin()==false&&isAvalialbe()) {
        try{
            do{
                str=makeMove(getCoordinates(player), player);
            }while (!str.equals("Done"));
            if (player.isWin()==true) {
                sendMessage(player.getMarker()+" is win! ");
                roundEnd=true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        hasWinner(player);


    }
    }

    private String getPole(char[][] gamePole){
        String str="";
        for (int i=0; i<gamePole.length; i++){
            for (int j=0; j<gamePole[i].length; j++){

                str+=Character.toString(gamePole[i][j]);
            }
            str+="\n";
        }
        return str;
    }

    private String makeMove(String move, TicTacToePlayer player) {
        PrintWriter out=null;
        char marker = player.getMarker();
        String str;
        try {
            out = new PrintWriter(new OutputStreamWriter(player.getSocket().getOutputStream()), true);
        }catch (IOException e){
            System.out.println("Exception at creating PrintWriter, in makeMove method " + e.getMessage());
        }
        try {
            Integer.parseInt(move);
        } catch (NumberFormatException e) {
            str="Должны быть введены цифры";
            out.println(str);
            return str;

        }

        char[] chars = move.toCharArray();
        if(chars.length>2){
            str= "Должны быть введены только две цифры";
            out.println(str);
            return str;
        }

        int x=Integer.parseInt(move.substring(0,1))-1;
        int y = Integer.parseInt(move.substring(1,2))-1;
        System.out.println(x);
        System.out.println(y);

        if(x>this.gamePole.length||y>gamePole.length||x<0||y<0||x>2||y>2){
            str ="Цифры должны быть в диапазоне от  1 до 3";
            out.println(str);
            return str;
        }

        if (!Character.toString(gamePole[x][y]).equals("-")) {
            str ="Данная ячейка уже заполнена";
            out.println(str);
            return str;
        }

        if (Character.toString(gamePole[x][y]).equals("-")) {
            gamePole[x][y]=marker;
            x+=1;
            y+=1;
        }
            out.println("Вы сделали ход линия: "+x+" ячейка: "+y);
            return "Done";

    }

    private   void hasWinner(TicTacToePlayer player) {
        char mark = player.getMarker();
        if( gamePole[0][0]==mark&&gamePole[0][1]==mark&&gamePole[0][2]==mark
                ||gamePole[1][0]==mark&&gamePole[1][1]==mark&&gamePole[1][2]==mark
                ||gamePole[2][0]==mark&&gamePole[2][1]==mark&&gamePole[2][2]==mark
                ||gamePole[0][1]==mark&&gamePole[1][1]==mark&&gamePole[2][1]==mark
                ||gamePole[0][0]==mark&&gamePole[1][0]==mark&&gamePole[2][0]==mark
                ||gamePole[0][2]==mark&&gamePole[1][2]==mark&&gamePole[2][2]==mark
                ||gamePole[0][0]==mark&&gamePole[1][1]==mark&&gamePole[2][2]==mark
                ||gamePole[0][2]==mark&&gamePole[1][1]==mark&&gamePole[2][0]==mark) {
            player.setWin(true);
            sendMessage("Winner is "+mark);
            try {
                player1.getSocket().close();
                player2.getSocket().close();
            }catch (IOException e){
                System.out.println("Exception socket closing at method hasWinner gameClass" +e.getMessage());
            }

        }

    }

    private String getCoordinates(TicTacToePlayer player)throws IOException{
       PrintWriter out = new PrintWriter(new OutputStreamWriter(player.getSocket().getOutputStream()), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
        out.println("Введите координаты для хода 1 цифра-номер линии, вторая - номер столбца , вы играете за: "+player.getMarker());
        out.println(getPole(gamePole));
        return in.readLine();
    }

    private  boolean isAvalialbe(){
        int p=0;
        for (int i=0; i<gamePole.length; i++){
            for (int j=0; j<gamePole[i].length; j++){
                if(Character.toString(gamePole[i][j]).equals("-"))p++;
            }
        }
        if(p>0)return true;
        else return false;
    }

    private void sendMessage(String string){
        for (PrintWriter p:writers
             ) {
            p.println(string);
        }
    }
}
