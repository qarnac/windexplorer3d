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
    private ComponentTownModel townModel;
    private AssetManager assetMan;
    
    public ContainerTown(Node parentNode, AssetManager assetMan){
        this.parentNode = parentNode;
        townModel = null;
        this.assetMan = assetMan;
    }
    
    
    public void initModel(String path, float rotation, Vector3f translation, float scale){
        townModel = new ComponentTownModel(parentNode, assetMan, path, rotation, translation, scale);
    }
    
    
    
    
}
