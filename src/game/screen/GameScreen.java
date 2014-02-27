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
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import game.Main;
import game.control.Player;
import game.control.WorldControl;
import game.model.Terrain;
import game.network.ClientManager;
import game.network.message.ConnectionClosed;
import game.network.message.PlayerStatus;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Manuel
 */
public class GameScreen extends AbstractAppState implements ScreenController {

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
    private Element popup;
    private Player playerControl;

    public GameScreen(Main game) {
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

        Main.nifty.fromXml("Interface/Screens/GameScreen.xml", "GameScreen", this);
        Main.nifty.gotoScreen("GameScreen");
        setMenuPos();
        Element chatControl = Main.nifty.getScreen("GameScreen").findElementByName("ChatWindow");
        chatControl.add(
            
        );


        WorldControl mWorld = new WorldControl(game);
        game.getStateManager().attach(mWorld);

        Terrain terrain = new Terrain(game);
        Node terra = terrain.createWorldTerrain();
        localRootNode.attachChild(terra);

        // disable the fly cam
        //game.getFlyByCamera().setEnabled(true);
        //game.getFlyByCamera().setDragToRotate(false);
        inputManager.setCursorVisible(false);


        playerControl = new Player(game);
        game.getStateManager().attach(playerControl);
    }

    @Override
    public void update(float tpf) {
        try {
            ConcurrentLinkedQueue queue = ClientManager.getQueue();
            Object message = queue.poll();
            if (message != null) {
                if (message instanceof PlayerStatus) {
                    PlayerStatus playerStatus = (PlayerStatus) message;
                    try {
                        localRootNode.getChild(playerStatus.getName()).setLocalTranslation(localRootNode.getChild(playerStatus.getName()).getLocalTranslation().interpolate(playerStatus.getPos(), tpf));
                        //Logger.get//Logger(CientManager.class.getName()).log(Level.SEVERE, "Update: {0}", playerStatus.getName());
                    } catch (NullPointerException e) {
                        //localRootNode.attachChild(new CreateGeoms(game).createSpatial(playerStatus.getName(), playerStatus.getPos()));
                        //Logger.getLogger(CientManager.class.getName()).log(Level.SEVERE, "Create: {0}", playerStatus.getName());
                    }
                }

                if (message instanceof ConnectionClosed) {
                    ConnectionClosed playerStatus = (ConnectionClosed) message;
                    try {
                        //localRootNode.detachChildNamed(playerStatus.getSomething());
                        //Logger.get//Logger(CientManager.class.getName()).log(Level.SEVERE, "Update: {0}", playerStatus.getSomething());
                    } catch (NullPointerException e) {
                    }
                }

            } else {
                //Logger.getLogger(CientManager.class.getName()).log(Level.SEVERE, "Queue is empty");
            }
        } catch (NullPointerException e) {
        }
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

    private void setMenuPos() {
        Element companyMenu = Main.nifty.getScreen("GameScreen").findElementByName("CompanyMenu");
        int xPosCM = Main.settings.getWidth() - 200;
        int yPosCM = Main.settings.getHeight() - 800;
        companyMenu.setConstraintX(new SizeValue("" + xPosCM + ""));
        companyMenu.setConstraintY(new SizeValue("" + yPosCM + ""));

        Element playerMenu = Main.nifty.getScreen("GameScreen").findElementByName("PlayerMenu");
        int xPos = Main.settings.getWidth() - 200;
        int yPos = Main.settings.getHeight() - 400;
        playerMenu.setConstraintX(new SizeValue("" + xPos + ""));
        playerMenu.setConstraintY(new SizeValue("" + yPos + ""));
    }
}
