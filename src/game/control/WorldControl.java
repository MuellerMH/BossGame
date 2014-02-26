/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.control;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import game.Main;
import static game.Main.bulletAppState;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel
 */
public class WorldControl extends AbstractAppState {

    private final Main game;
    private HashMap<String, Object> entities;

    public WorldControl(Main game) {
        this.game = game;
        entities = new HashMap<String, Object>();
    }

    public void addEntity(String name, Object obj) {
        if (obj != null) {

            Logger.getLogger(WorldControl.class.getName()).log(Level.SEVERE, "Name: {0}", name);
            Logger.getLogger(WorldControl.class.getName()).log(Level.SEVERE, "Object: {0}", obj);

            try {
                if (obj instanceof Spatial) {
                    Main.bulletAppState.getPhysicsSpace().add((Spatial) obj);
                } else if (obj instanceof Node) {
                    Main.bulletAppState.getPhysicsSpace().add((Node) obj);
                }
                entities.put(name, obj);
            } catch (NullPointerException ex) {
                Logger.getLogger(WorldControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public Object getEntity(String name) {
        return entities.get(name);
    }

    public Spatial createSpatial(String name, Vector3f pos, String model, String material) {
        Spatial thing = game.getAssetManager().loadModel(model);
        Material mat = new Material(game.getAssetManager(), material);
        thing.setName(name);
        CollisionShape shape =
                CollisionShapeFactory.createDynamicMeshShape(thing);
        thing.addControl(new RigidBodyControl(shape, 123.0f));
        thing.setMaterial(mat);
        addEntity(name, thing);
        return thing;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        try {
            Iterator it = entities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (pairs.getValue() instanceof Spatial) {
                    game.getRootNode().attachChild((Spatial) pairs.getValue());
                } else if (pairs.getValue() instanceof Node) {
                    game.getRootNode().attachChild((Node) pairs.getValue());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(WorldControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(float tpf) {
    }
}
