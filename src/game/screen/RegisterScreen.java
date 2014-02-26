/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.screen;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.Main;

/**
 *
 * @author Manuel
 */
public class RegisterScreen extends AbstractAppState implements ScreenController {
    private final Node rootNode;
    private final ViewPort viewPort;
    private final Node guiNode;
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private final AudioRenderer audioRenderer;
    private final Main game;
    private Nifty nifty;
    private Node localRootNode = new Node("Entry Screen RootNode");
    private Node localGuiNode = new Node("Entry Screen GuiNode");
    private final ColorRGBA backgroundColor = ColorRGBA.Black;
    private Screen screen;
    private NiftyJmeDisplay niftyDisplay;
    
    public RegisterScreen (Main game)
    {
        this.rootNode = game.getRootNode();
        this.viewPort = game.getViewPort();
        this.guiNode = game.getGuiNode();
        this.assetManager = game.getAssetManager();
        this.inputManager = game.getInputManager();
        this.audioRenderer = game.getAudioRenderer();
        this.game = game;
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        
        Main.nifty.fromXml("Interface/Screens/RegisterScreen.xml", "RegisterScreen", this);
        Main.nifty.gotoScreen("RegisterScreen");


        // disable the fly cam
//        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
       
    }
    
    public void register()
    {
        RegisterScreen registerScreen = new RegisterScreen(game);
        game.getStateManager().detach(this);
        game.getStateManager().attach(registerScreen);
        game.getGuiViewPort().removeProcessor(niftyDisplay);
    }
    
    public void login()
    {
        // TODO: check Connection to Server
        // TODO: check Login
        GameScreen gameScreen = new GameScreen(game);
        game.getStateManager().detach(this);
        game.getStateManager().attach(gameScreen);
        game.getGuiViewPort().removeProcessor(niftyDisplay);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        viewPort.setBackgroundColor(backgroundColor);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
    }
    

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
}
