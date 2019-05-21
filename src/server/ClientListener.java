 

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 *  
 * @author michael
 */
public class ClientListener implements Runnable {

    private boolean open = true;
    private ServerSocket server;
    private List<ConnectedGame> connectedGames;

 

    ClientListener(ServerSocket server, List<ConnectedGame> connectedGames) { 
        this.server = server;
        this.connectedGames = connectedGames;
        
    }

    
    
    
    public void setServer(ServerSocket server) {
        this.server = server; 
    }
    public void setConnectedGames(List<ConnectedGame> connectedGames) {
        this.connectedGames = connectedGames; 
    }
    public List<Socket> socketBuffer = new LinkedList<>();

    @Override
    public void run(){
        while(open){
            try { 
                socketBuffer.add(server.accept());
                if(socketBuffer.size()>1){
                    connectedGames.add(new ConnectedGame(socketBuffer.get(0), socketBuffer.get(1)).start());
                    socketBuffer.remove(1);
                    socketBuffer.remove(0);
                }

            }catch(SocketTimeoutException e){}
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
