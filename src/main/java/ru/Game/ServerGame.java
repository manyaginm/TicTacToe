package ru.Game;

import ru.Interfaces.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by MManiagin on 29.06.2017.
 */
public class ServerGame {
    public static void main(String[] args) throws IOException {
        Socket s1=null;
        Socket s2=null;
        boolean exit = true;
        ServerSocket serverSocket = new ServerSocket(8080);
        while (exit){
            Socket socket= serverSocket.accept();
            if(s1==null){
                s1=socket;
                continue;
            }
            if(s1!=null){
                s2=socket;
            }
            System.out.println("Connected " + socket);

        Game ch = new TicTacToeGame(s1,s2);
        System.out.println("Game created " + s1.toString() + " " + s2.toString());
        new Thread(ch).start();
        Thread.yield();
        s1=null;
        s2=null;

        }

    }
}
