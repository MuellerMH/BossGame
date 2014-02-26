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
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import game.Main;
import game.Server;
import game.network.ClientManager;
import java.net.ConnectException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel
 */
public class StartScreen extends AbstractAppState implements ScreenController, Controller {

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
    private boolean loading = false;
    private int frameCount = 1;
    private Element progressBarElement;
    private TextRenderer textRenderer;
    private Application app;

    public StartScreen(Main game) {
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
        this.app = app;

        Main.nifty.fromXml("Interface/Screens/EntryScreen.xml", "EntryScreen", this);
        Main.nifty.gotoScreen("EntryScreen");


        // disable the fly cam
//        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);

    }

    public void register() {
        RegisterScreen registerScreen = new RegisterScreen(game);
        game.getStateManager().attach(registerScreen);
        game.getGuiViewPort().removeProcessor(niftyDisplay);
    }

    public void login() {
        // TODO: check Connection to Server
        loading = true;
        try {
            final String userName = nifty.getScreen("EntryScreen").findNiftyControl("textfield0", TextField.class).getRealText();
            System.setProperty("UserName", userName);
            ClientManager mClient = ClientManager.getInstance();
            // TODO: check Login
            GameScreen gameScreen = new GameScreen(game);
            game.getStateManager().attach(gameScreen);
            game.getGuiViewPort().removeProcessor(niftyDisplay);
        } catch (Exception ex) {}


    }

    public void setProgress(final float progress, String loadingText) {
        final int MIN_WIDTH = 32;
        int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
        progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        progressBarElement.getParent().layoutElements();

        textRenderer.setText(loadingText);
    }

    @Override
    public void update(float tpf) {
//        if (loading) {            
//            Element element = nifty.getScreen("loadlevel").findElementByName("loadingtext");
//            textRenderer = element.getRenderer(TextRenderer.class);
//            if (tpf == 10) {
//                setProgress(0.1f, "Check Login");
//            }else if (tpf == 20) {
//                setProgress(0.2f, "Check Login");
//                
//            }else if (tpf == 30) {
//                setProgress(0.3f, "Check Login");
//                
//            }else if (tpf == 40) {
//                setProgress(0.4f, "Check Login");
//                
//            }else if (tpf == 50) {
//                setProgress(0.5f, "Check Login");
//                
//            }else if (tpf == 60) {
//                setProgress(0.6f, "Check Login");
//                
//            }else if (tpf == 70) {
//                setProgress(0.7f, "Check Login");
//                
//            }else if (tpf == 80) {
//                setProgress(0.8f, "Check Login");
//                
//            }else if (tpf == 90) {
//                setProgress(0.9f, "Check Login");
//                
//            }else if (tpf == 100) {
//                setProgress(1f, "Check Login");    
//                loading = false;
//                nifty.gotoScreen("end");
//            }
//        }        
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

    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onFocus(boolean getFocus) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }
}
