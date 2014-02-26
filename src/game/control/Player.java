/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.control;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import game.Main;
import game.network.ClientManager;
import game.network.message.PlayerStatus;

/**
 *
 * @author Manuel
 */
public class Player extends AbstractAppState implements AnalogListener, ActionListener {

    private final Main game;
    private final ViewPort viewPort;
    private final AssetManager assetManager;
    private CharacterControl playerObject;
    private boolean bFirstPerson = true;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private CameraNode camNode;
    private boolean bCurserActive=true;
    private String name = System.getProperty("user.name");

    public Player(Main game) {
        this.game = game;
        this.viewPort = game.getGuiViewPort();
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        playerObject = new CharacterControl(capsuleShape, 0.05f);
        playerObject.setJumpSpeed(40);
        playerObject.setFallSpeed(40);
        playerObject.setGravity(100);
        playerObject.setPhysicsLocation(new Vector3f(0, 5, 0));
        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        Main.bulletAppState.getPhysicsSpace().add(playerObject);
        setUpKeys();
    }

    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        InputManager inputManager = game.getInputManager();
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_TAB));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Menu");
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */
    public void onAction(String binding, boolean value, float tpf) {
        if (bFirstPerson) {
            if (binding.equals("Left")) {
                if (value) {
                    left = true;
                } else {
                    left = false;
                }
            } else if (binding.equals("Right")) {
                if (value) {
                    right = true;
                } else {
                    right = false;
                }
            } else if (binding.equals("Up")) {
                if (value) {
                    up = true;
                } else {
                    up = false;
                }
            } else if (binding.equals("Down")) {
                if (value) {
                    down = true;
                } else {
                    down = false;
                }
            } else if (binding.equals("Jump")) {
                playerObject.jump();
            } else if (binding.equals("Menu") && !value) {                
                if (!bCurserActive) {
                    game.getInputManager().setCursorVisible(true);
                    game.getFlyByCamera().setEnabled(true);
                    game.getFlyByCamera().setDragToRotate(false);
                    bCurserActive = true;
                } else {
                    game.getInputManager().setCursorVisible(false);
                    game.getFlyByCamera().setEnabled(false);
                    game.getFlyByCamera().setDragToRotate(true);
                    bCurserActive = false;
                }
            }
        }
    }

    /**
     * This is the main event loop--walking happens here. We check in which
     * direction the player is walking by interpreting the camera direction
     * forward (camDir) and to the side (camLeft). The setWalkDirection()
     * command is what lets a physics-controlled player walk. We also make sure
     * here that the camera moves with player.
     */
    @Override
    public void update(float tpf) {
        if (bFirstPerson) {
            camDir.set(game.getCamera().getDirection()).multLocal(0.6f);
            camLeft.set(game.getCamera().getLeft()).multLocal(0.4f);
            walkDirection.set(0, 0, 0);
            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }
            playerObject.setWalkDirection(walkDirection);
            game.getCamera().setLocation(playerObject.getPhysicsLocation());
            ClientManager.sendMessage(new PlayerStatus(ClientManager.getClient().getGameName(),name,playerObject.getPhysicsLocation(),ClientManager.getClient().getId()));
        }


    }
}
