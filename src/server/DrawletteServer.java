package server;

import com.sun.security.ntlm.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrawletteServer implements Closeable, Runnable {

    private final List<ConnectedGame> connectedGames;
    private final ServerSocket server;
    private Thread ClientThread;

    public DrawletteServer() throws IOException {
        server = new ServerSocket(3469);
        connectedGames = Collections.synchronizedList(new LinkedList<>());
    }

    @Override
    public void close() throws IOException {
        while (connectedGames.size() > 0) {
            connectedGames.get(0).close();
            connectedGames.remove(0);
        }
        server.close();
    }

    @Override
    public void run() {
        ClientThread = new Thread(new ClientListener(server, connectedGames));
        ClientThread.start();
        while (server.isBound()) {

            connectedGames.stream().filter((g) -> (!g.isRunning())).forEachOrdered((g) -> {
                try {
                    g.close();
                    System.out.println(g);
                    connectedGames.set(connectedGames.indexOf(g), ConnectedGame.invalid());
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });
            while (connectedGames.contains(ConnectedGame.invalid())) {
                connectedGames.remove(connectedGames.get(connectedGames.indexOf(ConnectedGame.invalid())));
            }

        }
    }
}
