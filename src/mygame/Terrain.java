/*
 * @author Ryan
 * 
 * Loads and creates the terrain from the path provided from the ConfigReader.
 * Also handles the Sun.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

public class Terrain {
    
    //--------------------------------------------------------------------------
    private Main mainHandle;
    
    private DataTerrain dataForTerrain;
    
    public AssetManager assetMan;//gotten from the mainHandle
    public ConfigReader confReader;//gotten from the mainHandle
    public TerrainQuad terrainQuad;//The actual mesh of the terrain
    public Node terrainNode;//The completed node, to be attached to the root node.
    public Material material;//material on the terrain.
    
    //PHYSICS STUFF
    CollisionShape terrainShape;
    private RigidBodyControl landscapeRigidBody;
    
    //This is level one specific!!! oh nos!!!  fix me!!!
    private final float TERRAIN_SIZE_SCALAR_X = 20f;
    private final float TERRAIN_SIZE_SCALAR_Y = 1000f;
    private final float TERRAIN_SIZE_SCALAR_Z = 20f;
    
    //--------------------------------------------------------------------------
    
    public Terrain(Main mainHandle){
        this.mainHandle = mainHandle;
        assetMan = mainHandle.getAssetManager();
        confReader = mainHandle.confReader;
        terrainNode = new Node("Terrain");
        terrainQuad = null;
    }
    
    //--------------------------------------------------------------------------
    /**
     * Call this or nothing will be shown!  All assets loaded by confReader.
     * Note confReader vars must be loaded before calling this...
     * Camera provided by simpleApp.
     * @param camera Camera object from the main class.
     */
    public void load(Camera camera){
        //get the data from the config reader.
        dataForTerrain = confReader.getTerrainData();
        
        // Create material from Terrain Material Definition
        material = new Material(assetMan, "Common/MatDefs/Terrain/Terrain.j3md");
        
        // Load alpha map (for splat textures)
        try{
            material.setTexture("Alpha", assetMan.loadTexture(dataForTerrain.alphaPath) );
        }
        //Not able to load texture, make this more elegent later!!!
        catch(Exception e){
            System.out.println("FUFUFUFU");
            //RETURN SOMETHING TO LET THE CALLING FUNTION KNOW ABOUT AN ERROR!
        }
        
        //Create heightmap from image
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = null;
        try{
            // load heightmap image (for the terrain heightmap)
            heightMapImage = assetMan.loadTexture(dataForTerrain.heightMapPath );
        }
        //Not able to load texture, make this more elegent later!!!
        catch(Exception e){
            System.out.println("FUFUFUFU");
            //RETURN SOMETHING TO LET THE CALLING FUNTION KNOW ABOUT AN ERROR!
        }

        //a second param (float) can set the scale of the height of the map.
        //heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        
        //TEXTURES------------------------------------------
        //You can tell you picked too small a scale if, for example, your road tiles appear like tiny grains of sand.
        //You can tell you picked too big a scale if, for example, the blades of grass look like twigs.

        
        //Load textures
        Texture grass = null;
        Texture dirt = null;
        Texture rock = null;
        try{
            // load grass texture
            grass = assetMan.loadTexture( dataForTerrain.grassTexPath );
            grass.setWrap(WrapMode.Repeat);
            material.setTexture("Tex1", grass);
            material.setFloat("Tex1Scale", 64f);

            // load dirt texture
            dirt = assetMan.loadTexture( dataForTerrain.dirtTexPath );
            dirt.setWrap(WrapMode.Repeat);
            material.setTexture("Tex2", dirt);
            material.setFloat("Tex2Scale", 32f);

            // load rock texture
            rock = assetMan.loadTexture( dataForTerrain.roadTexPath );
            rock.setWrap(WrapMode.Repeat);
            material.setTexture("Tex3", rock);
            material.setFloat("Tex3Scale", 128f);
        }
        catch(Exception e){
            System.out.println("FUFUFUFU");
            //handle this!!!
        }

        //Create Terrain----------------------------------
        //64x64 patchSize requires param of 64+1=65.
        //totalSize is related to heigh map:
        //If you supply a block size of 2x the heightmap size (1024+1=1025), you get a stretched out, wider, flatter terrain.
        //If you supply a block size 1/2 the heightmap size (256+1=257), you get a smaller, more detailed terrain.

        //                             name, patchSize, totalSize, heightMap
        terrainQuad = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        terrainQuad.setMaterial(material);
        terrainQuad.setLocalScale(TERRAIN_SIZE_SCALAR_X, TERRAIN_SIZE_SCALAR_Y, TERRAIN_SIZE_SCALAR_Z); // scale to make it less steep
        
        
        //-------------------------------------------------
        
        //PHYSICS STUFF
        terrainShape = CollisionShapeFactory.createMeshShape((Node) terrainQuad);
        landscapeRigidBody = new RigidBodyControl(terrainShape, 0);
        terrainQuad.addControl(landscapeRigidBody);
        
        //-------------------------------------------------
        //LAST NODE MAKING
        //TERRAIN
        terrainNode.attachChild(terrainQuad);
        
        //SKY
        terrainNode.attachChild(SkyFactory.createSky(assetMan, dataForTerrain.skyboxPath, false));
        
    }//method
    
    
    
    
    //--------------------------------------------------------------------------
    
    public Node getTerrainNode(){
        return terrainNode;
    }
    
    public RigidBodyControl getTerrainRigidBody(){
        return landscapeRigidBody;
    }
    
    public TerrainQuad getTerrainQuad(){
        return terrainQuad;
    }

    

}//class
