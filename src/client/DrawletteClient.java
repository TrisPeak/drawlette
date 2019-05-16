package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class DrawletteClient {

    private Socket socket;
    private ServerListener listener;
    private DataOutputStream dataToServer;

    public static final String SERVER_ADDRESS = "x.x.x.x";
    public static final int APPLICATION_PORT = 3469;

    public void connect(String address, int port){
        try {
            socket = new Socket(address, port);
            dataToServer = new DataOutputStream(socket.getOutputStream());
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        listener = new ServerListener().setSocket(socket).setDrawletteClient(this);
        listener.start();
    }

    public void sendData(String data){
        try {
            dataToServer.writeUTF(data);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void draw(String data){
        System.out.println(data);
        //TODO
    }
}

class ServerListener extends Thread {

    private Socket socket;
    private DrawletteClient drawletteClient;

    public ServerListener setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }
    public ServerListener setDrawletteClient(DrawletteClient drawletteClient){
        this.drawletteClient = drawletteClient;
        return this;
    }

    @Override
    public void run() {
        try {
            InputStream inFromServer = socket.getInputStream();
            DataInputStream dataFromServer = new DataInputStream(inFromServer);
            while(true) {
                String data = dataFromServer.readUTF();
                drawletteClient.draw(data);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
