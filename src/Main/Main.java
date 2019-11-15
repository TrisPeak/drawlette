package Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.DrawletteServer;

/**
 *
 * @author michael
 */
public class Main {

    public static boolean await(MainThread t) {
        while (t.isDone());
        return true;
    }

    public static void main(String[] args) {
        MainThread th = MainThread.getInstance();
        th.start();
        await(th);
    }
}
