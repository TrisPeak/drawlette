package test;

import client.DrawletteClient;
import server.DrawletteServer;

public class TesterMain {

    public static void main(String[] args) {
        DrawletteServer server = new DrawletteServer();
        DrawletteClient clientA = new DrawletteClient();
        DrawletteClient clientB = new DrawletteClient();
        clientA.connect("localhost", 3469);
        clientB.connect("localhost", 3469);
        clientA.sendData("test von A");
        clientA.sendData("test von A");
        clientB.sendData("test von B");
        clientA.sendData("test von A");
        clientB.sendData("test von A");
        clientB.sendData("test von A");
        //server.closeServer();

    }

}
