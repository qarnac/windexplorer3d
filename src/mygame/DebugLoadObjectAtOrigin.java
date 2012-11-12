/*
 * BY: Ryan Moe
 * 
 * Shows a model that you specify at the origin of the world.
 * Used for
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author codemorebetter
 */
public class DebugLoadObjectAtOrigin {
    
    
    private Node node;
        private Spatial spatial;
    
    
    /**
         * 
         * @param inModelPath Path of model relative to assets folder
         * @param assetManager asset manager reference, from main
         * @param rootNode root node reference, from main
         */
    public DebugLoadObjectAtOrigin(String inModelPath, Main mainReference){
        
        //load asset
        spatial = mainReference.getAssetManager().loadModel(inModelPath);
        
        //attach asset to new node
        node = new Node();
        node.attachChild(spatial);
        node.setLocalTranslation(0, 50, 0);
        
        //show asset
        mainReference.getRootNode().attachChild(node);
        
    }
    
    
    
    
    
    
}
