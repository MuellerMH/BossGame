/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.network.serializing.Serializer;
import game.network.message.ConnectionClosed;
import game.network.message.HelloMessage;
import game.network.message.PlayerStatus;

/**
 *
 * @author mhm
 */
public class Globals {
    public static final String VERSION = "BOSS - be a boss - like a boss";
//    public static final String DEFAULT_SERVER = "192.168.1.24";
    public static final String DEFAULT_SERVER = "localhost";
//    public static final String DEFAULT_SERVER = "jmonkeyengine.com";
//    public static final String DEFAULT_SERVER = "128.238.56.114";
    public static final int PROTOCOL_VERSION = 1;
    public static final int CLIENT_VERSION = 1;
    public static final int SERVER_VERSION = 1;

    public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
    public static final float NETWORK_MAX_PHYSICS_DELAY = 0.25f;
    public static final int SCENE_FPS = 60;
    public static final float PHYSICS_FPS = 1f / 30f;
    //only applies for client, server doesnt render anyway
    public static final boolean PHYSICS_THREADED = true;
    public static final boolean PHYSICS_DEBUG = false;
    public static final int DEFAULT_PORT_TCP = 6143;
    public static final int DEFAULT_PORT_UDP = 6143;
    
    public static void registerMessages()
    {        
            Serializer.registerClass(HelloMessage.class);
            Serializer.registerClass(PlayerStatus.class);
            Serializer.registerClass(ConnectionClosed.class);
    }
}
