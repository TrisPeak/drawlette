package test;

import client.DrawletteClient;
import static java.lang.Thread.sleep;
import server.DrawletteServer;

public class TesterMain {

    public static void main(String[] args) throws InterruptedException {
        DrawletteServer server = new DrawletteServer();
        DrawletteClient c;
        for(int i = 0; i < 8; i++){
            DrawletteClient z = new DrawletteClient();
            z.connect("localhost", 3469); 
            z.sendData(" ich bin nummer "+i);
            sleep(100);
        }
    

    }

}
