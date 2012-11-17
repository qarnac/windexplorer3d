/*
 * BY: Ryan Moe
 * 
 * Loads the town model assets and displays them around a level.
 * Each town has final variables describing it.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Ryan
 */
public class ModelsTowns {
    
    //--------------------------------------------------------------------------
    //VARS----------------------------------------------------------------------
    //--------------------------------------------------------------------------
        private AssetManager assetManager;
        //This is the master model for the town model.
        //Gets cloned for every visible model.
        private Spatial townMasterSpatial;
        //all the visible models.
        private Spatial[] _townSpatials;
            //visible models rotation quats.
            private Quaternion[] _townQuats;
        Node rootNode;
            //all models attach here.
            public Node townsModelNode;
        
        DebugGlobals globalFlags;
        //----------------------------------------------------------------------
        //This is where you define the towns------------------------------------
        //----------------------------------------------------------------------
        public final String TOWN_MODEL_PATH = "Models/town/main.j3o";
        
        //LEVEL 1---------------------------------
        //private final short LEVEL_1_NUMBER_OF_TOWNS = 2;
        private final short LEVEL_1_TOWN_SCALE = 4;
        
            private final Vector3f LEVEL_1_TOWN_1_LOCATION = new Vector3f(-1770f, -1.2f, -2597f);
            private final float LEVEL_1_TOWN_1_ROTATION = 90f;

            private final Vector3f LEVEL_1_TOWN_2_LOCATION = new Vector3f(2637f, -1.2f, 3241f);
            private final float LEVEL_1_TOWN_2_ROTATION = 90f;
            
            private final Vector3f LEVEL_1_TOWN_3_LOCATION = new Vector3f(-3882f, -1.2f, 3349f);
            private final float LEVEL_1_TOWN_3_ROTATION = 3f;
        
        //Put all the towns info into arrays so we can access them in a for loop easily.
        private final Vector3f[] _LEVEL_1_TOWN_LOCATIONS = {LEVEL_1_TOWN_1_LOCATION, LEVEL_1_TOWN_2_LOCATION, LEVEL_1_TOWN_3_LOCATION};
        private final float[] _LEVEL_1_TOWN_ROTATIONS = {LEVEL_1_TOWN_1_ROTATION, LEVEL_1_TOWN_2_ROTATION, LEVEL_1_TOWN_3_ROTATION};
        //-----------------------------------------
        
        
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
        
    public ModelsTowns(DataModelsTowns dataObj){
        assetManager = dataObj.assetManager;
        townMasterSpatial = null;
        _townSpatials = null;
        _townQuats = null;
        rootNode = dataObj.rootNode;
        townsModelNode = new Node();
        globalFlags = dataObj.globalFlags;
    }//method
    
    //--------------------------------------------------------------------------
    //Justs loads the asset from the disk.
    public void loadTownAssets(){
        //Do this if using a 
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        //Spatial gameLevel = assetManager.loadModel("main.scene");
        //Do this if going to extract zip files
        townMasterSpatial = assetManager.loadModel(TOWN_MODEL_PATH);
    }//method
    
    //--------------------------------------------------------------------------
    
    public void showLevel1Towns(){
        if(globalFlags.DEBUG_TOWN_MODELS_VISIBLE){
            _townSpatials = new Spatial[_LEVEL_1_TOWN_LOCATIONS.length];
            _townQuats = new Quaternion[_LEVEL_1_TOWN_LOCATIONS.length];

            for(short i = 0; i < _LEVEL_1_TOWN_LOCATIONS.length; ++i){
                _townSpatials[i] = townMasterSpatial.clone();
                _townSpatials[i].setLocalTranslation(_LEVEL_1_TOWN_LOCATIONS[i]);

                _townQuats[i] = new Quaternion();
                _townQuats[i].fromAngleAxis(_LEVEL_1_TOWN_ROTATIONS[i], Vector3f.UNIT_Y);
                _townSpatials[i].setLocalRotation(_townQuats[i]);

                _townSpatials[i].setLocalScale(LEVEL_1_TOWN_SCALE);
                rootNode.attachChild(_townSpatials[i]);
            }//for
        }//if
    }//method
    
    //--------------------------------------------------------------------------
    
    public void setTownLocation(short index, Vector3f location){
        if(index < _townSpatials.length)
            _townSpatials[index].setLocalTranslation(location);
    }
    
    //--------------------------------------------------------------------------
    
    
    
    
    
}//class
