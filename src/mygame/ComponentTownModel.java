/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author cmb
 */
public class ComponentTownModel {
    
    private String path;
    private Spatial townSpatial;
    private Quaternion townRotation;
    
    
    
    public ComponentTownModel(Node parentNode, AssetManager assetManager, 
            String path, float rotation, Vector3f translation, float scale){
        this.path = path;
        townSpatial = assetManager.loadModel(path);
        
        //Rotate BEFORE translate
        townRotation = new Quaternion();
        townRotation.fromAngleAxis(rotation, Vector3f.UNIT_Y);
        townSpatial.setLocalRotation(townRotation);
        
        townSpatial.setLocalTranslation(translation);
        
        townSpatial.setLocalScale(scale);
        
        parentNode.attachChild(townSpatial);
    }//method
    
    
}
