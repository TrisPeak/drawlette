package server;

import com.sun.security.ntlm.Client;
import com.sun.security.ntlm.Server;
import test.ConnectedGame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrawletteServer {
    private List<ConnectedGame> connectedGames;
    private ServerSocket server;

    private boolean open = true;

    public DrawletteServer(){
        try {
            server = new ServerSocket(3469);
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            server.setSoTimeout(15000);
        }catch(SocketException e){
            e.printStackTrace();
        }
        connectedGames = new LinkedList<>();
    }

    public void acceptConnections(){
        while(open){
            try {
                Socket playerA = server.accept();
                Socket playerB = server.accept();
                connectedGames.add(new ConnectedGame(playerA, playerB).start());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void closeServer(){
        this.open = false;
        while(connectedGames.size()>0){
            connectedGames.get(0).endGame();
            connectedGames.remove(0);
        }
        try {
            server.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}