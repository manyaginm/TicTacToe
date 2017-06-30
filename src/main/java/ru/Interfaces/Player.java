package ru.Interfaces;

import java.net.Socket;

/**
 * Created by MManiagin on 30.06.2017.
 */
public interface Player {
    Socket getSocket();
    boolean isWin();
}
