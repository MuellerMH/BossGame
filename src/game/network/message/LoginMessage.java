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
public class LoginMessage extends AbstractMessage {
  private String hello;       // custom message data
  public LoginMessage() {}    // empty constructor
  public LoginMessage(String s) { hello = s; } // custom constructor  
  public String getSomething(){ return hello; }
}
