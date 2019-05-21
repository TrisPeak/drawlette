/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.DrawletteServer;

/**
 *
 * @author michael
 */
public class MainThread extends Thread {

    Thread t;

    public MainThread() {
        try {
            this.t = new Thread(new DrawletteServer());
        } catch (IOException ex) {
        }
    }

    @Override
    public void run() {
        t.start();
    }

    public static MainThread getInstance() {
        return MainThreadHolder.INSTANCE;
    }
    boolean done = false;

    boolean isDone() {
        return !t.isAlive();
    }

    private static class MainThreadHolder {

        private static final MainThread INSTANCE = new MainThread();
    }
}
