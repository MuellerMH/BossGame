/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.network.message;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import game.Main;

/**
 *
 * @author mhm
 */
@Serializable
public class PlayerStatus extends AbstractMessage {
  private String msg;       // custom message data
  private String name;
  private long userId;
  private float xPos, yPos,zPos;
    
  public PlayerStatus() {}    // empty constructor
  public PlayerStatus(String msg, String name, Vector3f playerPos, long userId) { 
      this.msg = msg; 
      this.name = name; 
      this.xPos = playerPos.x;
      this.yPos = playerPos.y;
      this.zPos = playerPos.z;
      this.userId = userId;
  } // custom constructor 
  public String getSomething(){ return msg; }
  public String getName(){ return name; }
  public Vector3f getPos(){ return new Vector3f(xPos,yPos,zPos); }
  public long getUserId(){ return userId; }
}
