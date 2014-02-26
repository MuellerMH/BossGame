package game;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import de.lessvoid.nifty.Nifty;
import game.network.ClientManager;
import game.screen.StartScreen;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    public static AppSettings settings;
    public static void main(String[] args) {
        Main app = new Main();        
        settings = new AppSettings(true);
        settings.setFrameRate(Globals.SCENE_FPS);
        settings.setSettingsDialogImage("/Interface/Images/like_a_boss.jpg");
        settings.setTitle("BOSS-Game");
        settings.setHeight(900);
        settings.setWidth(1600);
        app.setSettings(settings);
        app.start(JmeContext.Type.Display);
    }
    public static Nifty nifty;
    private ScheduledThreadPoolExecutor executor;
    public static BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        
         /** Set up Physics */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        executor = new ScheduledThreadPoolExecutor(4);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        

        Globals.registerMessages();

        StartScreen startScreen = new StartScreen(this);
        getStateManager().attach(startScreen);        
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
        
        try {
            ClientManager.close();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
        executor.shutdown();
    }
}
