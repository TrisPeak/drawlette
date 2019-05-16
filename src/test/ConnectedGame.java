package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedGame{
    private Socket playerA;
    private Socket playerB;
    private boolean running = false;

    public ConnectedGame(Socket playerA, Socket playerB){
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public ConnectedGame start(){
        running = true;

        InputListener playerAInputListener;
        InputListener playerBInputListener;

        try {
            System.out.println("player A: "+playerA.getRemoteSocketAddress().toString().replaceAll("/", ""));
            System.out.println("player B: "+playerB.getRemoteSocketAddress().toString().replaceAll("/", ""));
            playerAInputListener = new InputListener().setInFromClient(new DataInputStream(playerB.getInputStream())).setOutToClient(new DataOutputStream(playerA.getOutputStream()));
            playerAInputListener.start();
            playerBInputListener = new InputListener().setInFromClient(new DataInputStream(playerA.getInputStream())).setOutToClient(new DataOutputStream(playerB.getOutputStream()));
            playerBInputListener.start();

        }catch(IOException e){
            e.printStackTrace();
        }

        return this;
    }

    public void endGame(){
        try {
            playerA.close();
            playerB.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

class InputListener extends Thread{

    private DataInputStream inFromClient;
    private DataOutputStream outToClient;
    private boolean running = false;

    public InputListener setInFromClient(DataInputStream inFromClient){
        this.inFromClient = inFromClient;
        return this;
    }
    public InputListener setOutToClient(DataOutputStream outToClient) {
        this.outToClient = outToClient;
        return this;
    }


    @Override
    public void run() {
        running = true;
        try {
            outToClient.writeUTF("ready");
        }catch(IOException e){
            e.printStackTrace();
        }

        while(running){
            try{
                String data = inFromClient.readUTF();
                outToClient.writeUTF(data);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void end(){
        running = false;

    }
}


