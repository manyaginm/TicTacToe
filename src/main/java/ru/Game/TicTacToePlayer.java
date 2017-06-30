package ru.Game;

import ru.Interfaces.Player;

import java.net.Socket;

/**
 * Created by MManiagin on 29.06.2017.
 */
public class TicTacToePlayer implements Player{
    private Socket socket;
    private char marker;
    private boolean win;

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public TicTacToePlayer(Socket socket, char marker){
        this.socket=socket;
        this.marker=marker;

    }
    public Socket getSocket() {
        return socket;
    }
    public char getMarker() {
        return marker;
    }


}
