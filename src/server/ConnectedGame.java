package server;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedGame implements Closeable {

    
    private static ConnectedGame invalid = new ConnectedGame();
    public static ConnectedGame invalid() {
      return invalid;
    }

    private Socket playerA;
    private Socket playerB;
    InputListener playerAInputListener;
    InputListener playerBInputListener;

    
    private ConnectedGame(){}
    
    
    public ConnectedGame(Socket playerA, Socket playerB) {
        try {
            this.playerA = playerA;
            this.playerB = playerB;
            playerAInputListener = new InputListener(this,new DataInputStream(playerB.getInputStream()),
                    new DataOutputStream(playerA.getOutputStream()));
            playerBInputListener = new InputListener(this,new DataInputStream(playerA.getInputStream()),
                    new DataOutputStream(playerB.getOutputStream()));
        } catch (IOException ex) {
            System.out.println("Exception when creating game " + ex);
        }

    }

    public ConnectedGame start() {

        System.out.println(toString() + " started!");

        playerAInputListener.start();

        playerBInputListener.start();

        return this;
    }

    public boolean isRunning() {
        return playerAInputListener.isRunning() && playerBInputListener.isRunning();
    }

    @Override
    public void close() throws IOException {

        playerA.close();
        playerB.close();  
    }

    @Override
    public String toString() {
        return playerA.getInetAddress() + " <-" + (isRunning() ? "" : "!") + "-> " + playerB.getInetAddress();
    }

}

class InputListener extends Thread {

    private ConnectedGame gameMaster;
    private DataInputStream inFromClient;
    private DataOutputStream outToClient;
    private boolean running = true;

    public InputListener(ConnectedGame gameMaster,DataInputStream inFromClient, DataOutputStream outToClient) {
        this.gameMaster = gameMaster;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        try {
            outToClient.writeUTF("ready");
        } catch (IOException e) {
        }

        while (running) {
            if (outToClient == null) {
                end();
                return;
            }
            try {
                if (running) {
                    if (inFromClient == null) {
                        end();
                        return;
                    }
                    String data = inFromClient.readLine();
                    if (outToClient == null) {
                        end();
                        return;
                    } else {
                        if (data != null) {
                            outToClient.writeUTF(data);
                        }
                    }
                } else {
                    return;
                }
            } catch (IOException e) {
                System.out.println("Error! from read write");
                end();
            }
        }
    }

    public void end() {
        try {
            gameMaster.close();
        } catch (IOException ex) {
         
        }
        running = false;

    }
}
