/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.network.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author mhm
 */
@Serializable
public class ConnectionClosed extends AbstractMessage {
  private int clientid;       // custom message data
  public ConnectionClosed() {}    // empty constructor
  public ConnectionClosed(int s) { clientid = s; } // custom constructor  
  public int getSomething(){ return clientid; }
}
