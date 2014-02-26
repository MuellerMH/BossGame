/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import game.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel
 */
public class Terrain {

    private TerrainQuad terrain;
    private Material mat_terrain;
    private final Node rootNode;
    private final ViewPort viewPort;
    private final Node guiNode;
    private final AssetManager assetManager;
    private final Main game;
    private final Node terrainNode;

    public Terrain(Main game) {
        this.rootNode = game.getRootNode();
        this.viewPort = game.getViewPort();
        this.guiNode = game.getGuiNode();
        this.assetManager = game.getAssetManager();
        this.game = game;

        terrainNode = new Node();        
    }

    public Node createWorldTerrain() {
        /**
         * 1. Create terrain material and load four textures into it.
         */
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        /**
         * 1.1) Add ALPHA map (for red-blue-green coded splat textures)
         */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"));

        /**
         * 1.2) Add GRASS texture into the red layer (Tex1).
         */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /**
         * 1.3) Add DIRT texture into the green layer (Tex2)
         */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /**
         * 1.4) Add ROAD texture into the blue layer (Tex3)
         */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);
        /**
         * 2. Create the height map
         */
        //    AbstractHeightMap heightmap = null;
        //    Texture heightMapImage = assetManager.loadTexture(
        //            "Textures/Terrain/splat/mountains512.png");
        //    heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        //    heightmap.load();
        HillHeightMap heightmap = null;
        try {
            heightmap = new HillHeightMap(1025, 1000, 50, 100, (byte) 3);
        } catch (Exception ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * 3. We have prepared material and heightmap. Now we create the actual
         * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A
         * good value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3)
         * We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the
         * prepared heightmap itself.
         */
        terrain = new TerrainQuad("my terrain", 65, 513, heightmap.getScaledHeightMap());

        /**
         * 4. We give the terrain its material, position & scale it, and attach
         * it.
         */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -50, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        terrainNode.attachChild(terrain);

        /**
         * 5. The LOD (level of detail) depends on were the camera is:
         */
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(game.getCamera());
        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
        terrain.addControl(control);

        /**
         * 6. Add physics:
         */
        // We set up collision detection for the scene by creating a static 
        terrain.addControl(new RigidBodyControl(0));
        // to make them appear in the game world.
        Spatial sky = SkyFactory.createSky(
                assetManager, "Textures/Sky/Bright/BrightSky.dds", false);
        terrainNode.attachChild(sky);
        sky.setQueueBucket(RenderQueue.Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);
        
        Main.bulletAppState.getPhysicsSpace().add(terrain);
        
        return terrainNode;
    }
}
