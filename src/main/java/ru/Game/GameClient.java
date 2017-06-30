package ru.Game;

import java.io.*;
import java.net.Socket;

/**
 * Created by MManiagin on 29.06.2017.
 */
public class GameClient {
    private static Socket socket;
    private static BufferedReader in;
    private static BufferedReader lineReader;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 8080);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            lineReader = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String str;

            new Thread(new Runnable() {
                public void run() {
                    String str;
                    try{
                        while (true){
                            if((str = lineReader.readLine()) != null);
                            {
                                if(!str.equals("exit")) {
                                    out.println(str);
                                    System.out.println("Вы отправили " + str);
                                }else {
                                    System.exit(0);
                                }
                            }}
                    }catch (IOException e){
                        System.out.println("Exception at write to thread at class GameClient " + e.getMessage());
                    }
                }
            }).start();


            new Thread(new Runnable() {
                public void run() {
                    String str;
                    try {
                        while(true){
                            if ((str = in.readLine()) != null) {
                                System.out.println(str);
                            }}
                    }catch (IOException e){
                        System.out.println("Exception at read from thread at class GameClient " + e.getMessage());
                    }
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Exception at create socket in client class");
        }finally {

        }
    }

 }

