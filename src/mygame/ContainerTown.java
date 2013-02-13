/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author cmb
 */

public class ContainerTown {
    
    private Node parentNode;
    private AssetManager assetMan;
    
    private ComponentTownModel townModel;
    private ComponentTownSound townSound;
    
    
    
    public ContainerTown(Node parentNode, AssetManager assetMan){
        this.parentNode = parentNode;
        this.assetMan = assetMan;
        townModel = null;
    }
    
    
    
    public void initModel(String path, float rotation, Vector3f translation, float scale){
        townModel = new ComponentTownModel(parentNode, assetMan, path, rotation, translation, scale);
    }
    
    
    
    public void initSound(String path, float volume, boolean looping){
        townSound = new ComponentTownSound(parentNode, assetMan, path, volume, looping);
    }
    
    
    public ComponentTownSound getSoundManager(){
        return townSound;
    }
    
    
}//class
