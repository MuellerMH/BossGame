/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import game.Globals;
import game.network.message.ConnectionClosed;
import game.network.message.HelloMessage;
import game.network.message.LoginMessage;
import game.network.message.PlayerStatus;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel
 */
public class ClientManager implements ErrorListener, ClientStateListener, MessageListener<Client> {

    private static ConcurrentLinkedQueue<Object> messageQueue;
    private static Client client;
    private static boolean connected;
    private static ClientManager clientInstance;

    private ClientManager() throws IOException {
        try {
            messageQueue = new ConcurrentLinkedQueue();
            client = Network.connectToServer(Globals.DEFAULT_SERVER, Globals.DEFAULT_PORT_UDP);
            client.addClientStateListener(this);
            client.addErrorListener(this);
            client.addMessageListener(this, HelloMessage.class);
            client.addMessageListener(this, PlayerStatus.class);
            client.addMessageListener(this, ConnectionClosed.class);
            client.addMessageListener(this, LoginMessage.class);
            client.start();
        } catch (IOException ex) {
            throw ex;
        }

    }

    public static void close() {
        try {
            client.send(new ConnectionClosed(client.getId()));
            client.close();
        } catch (NullPointerException ex) {
            //Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "Client was not connected {0}", nullPointerError);
        }
    }
    
    public static ConcurrentLinkedQueue getQueue()
    {
        return messageQueue;
    }

    public static boolean isConnected() {
        if(connected)
            return true;
        return false;
    }
    
    public static ClientManager getInstance() throws IOException
    {
        if(clientInstance == null)
            clientInstance = new ClientManager();
        return clientInstance;
    }

    public static Client getClient() {
        return client;
    }

    public static void sendMessage(Message message) {
        client.send(message);
    }

    public void sendLoginMessage() {
        client.send(new HelloMessage("Login Onko"));
    }

    public void handleError(Object source, Throwable t) {
        Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "handleError: {0}", source);
    }

    public void clientConnected(Client c) {
        connected = true;
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
        connected = false;
    }

    public void messageReceived(Client source, Message message) {
        Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "messageReceived: {0}", message);
        if (message instanceof HelloMessage) {
            // do something with the message
            HelloMessage helloMessage = (HelloMessage) message;
            Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "messageReceived HelloMessage: {0}", message);
        }

        if (message instanceof PlayerStatus) {
            PlayerStatus playerStatus = (PlayerStatus) message;
            Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "messageReceived PlayerStatus: {0}", message);

            messageQueue.add(playerStatus);
        }

        if (message instanceof ConnectionClosed) {
            ConnectionClosed playerStatus = (ConnectionClosed) message;
            Logger.getLogger(ClientManager.class.getName()).log(Level.INFO, "messageReceived PlayerStatus: {0}", message);

            messageQueue.add(playerStatus);
        }
    }
}
