package game;

import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import game.network.message.ConnectionClosed;
import game.network.message.HelloMessage;
import game.network.message.LoginMessage;
import game.network.message.PlayerStatus;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 *
 * @author normenhansen
 */
public class Server extends SimpleApplication implements ErrorListener, ConnectionListener, MessageListener {

    public static void main(String[] args) {
        Server app = new Server();        
        app.start(JmeContext.Type.Headless);
    }
    private com.jme3.network.Server server;
    private ConnectionClosed connectionClosed;

    @Override
    public void simpleInitApp() {
                Globals.registerMessages();
        try {
            server = Network.createServer(Globals.DEFAULT_PORT_UDP);
            server.addConnectionListener(this);            
            server.addMessageListener(this,HelloMessage.class);
            server.addMessageListener(this,PlayerStatus.class);
            server.addMessageListener(this,LoginMessage.class);
            server.start();
            if (server.isRunning()) {
                Message message = new HelloMessage("Welcome!");
                server.broadcast(message);
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Cannot start server: {0}", ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    @Override
    public void destroy() {
        super.destroy();
        server.close();
    }

    public void connectionAdded(com.jme3.network.Server server, HostedConnection conn) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "connectionAdded: {0}", conn);
    }

    public void connectionRemoved(com.jme3.network.Server server, HostedConnection conn) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "connectionRemoved: {0}", conn);
        connectionClosed = new ConnectionClosed();
        server.broadcast(Filters.notEqualTo( conn ),connectionClosed);
    }

    public void handleError(Object source, Throwable t) {        
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "handleError: {0}", source);
    }

    public void messageReceived(Object source, Message message) {
        
        if (message instanceof HelloMessage) {
            // do something with the message
            HelloMessage helloMessage = (HelloMessage) message;
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "messageReceived HelloMessage: {0}", message);
        } 
        
        if (message instanceof PlayerStatus) {
            PlayerStatus playerStatus = (PlayerStatus) message;
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "messageReceived PlayerStatus: {0}", message);
            server.broadcast(Filters.notEqualTo( source ),playerStatus);
        }
        if (message instanceof ConnectionClosed) {
            ConnectionClosed playerStatus = (ConnectionClosed) message;
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "messageReceived PlayerStatus: {0}", message);
            server.broadcast(Filters.notEqualTo( source ),playerStatus);
        }
        
    }
}
