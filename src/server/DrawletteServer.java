package server;

import com.sun.security.ntlm.Client;
import test.ConnectedGame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;

public class DrawletteServer extends Thread{
    private List<ConnectedGame> connectedGames;
    private ServerSocket server;
    private ClientListener listener;

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
        listener = new ClientListener().setServer(server).setConnectedGames(connectedGames);
        listener.start();
    }

    public void closeServer(){
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

class ClientListener extends Thread{

    private boolean open = true;
    private ServerSocket server;
    private List<ConnectedGame> connectedGames;

    public ClientListener setServer(ServerSocket server) {
        this.server = server;
        return this;
    }
    public ClientListener setConnectedGames(List<ConnectedGame> connectedGames) {
        this.connectedGames = connectedGames;
        return this;
    }
    public List<Socket> socketBuffer = new LinkedList<>();

    @Override
    public void run(){
        while(open){
            try {
                socketBuffer.add(server.accept());
                if(socketBuffer.size()>1){
                    connectedGames.add(new ConnectedGame(socketBuffer.get(0), socketBuffer.get(1)).start());
                }

            }catch(SocketTimeoutException e){}
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}