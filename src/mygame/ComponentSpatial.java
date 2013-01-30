/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author cmb
 */
public class ComponentSpatial extends ComponentAbstract{

    public final String ID = "spatial";
    
    private Node spatialNode = null;
            private Spatial spatial = null;
                
    private String path = null;
    private float spatialScale = 1;
    
    //-------------------------------------------
    
    public void setPath(String path){
        this.path = path;
    }
    
    //-------------------------------------------
    
    public void setLocalTranslation(Vector3f trans){
        if(spatialNode == null){
            System.err.println("ComponentSpatial: run container.init() before"
                    + "setting a local translation.");
            return;
        }
        spatialNode.setLocalTranslation(trans);
    }
    
    
    //-------------------------------------------
    
    public void setLocalTranslation(float x, float y, float z){
        if(spatialNode == null){
            System.err.println("ComponentSpatial: run container.init() before"
                    + "setting a local translation.");
            return;
        }
        spatialNode.setLocalTranslation(x, y, z);
    }
    
   //-------------------------------------------
    
    public Vector3f getLocalTranslation(){
        if(spatialNode == null){
            System.err.println("ComponentSpatial: run container.init() before"
                    + "getting a local translation!");
            return new Vector3f(0,0,0);
        }
        return spatialNode.getLocalTranslation();
    }
    
    //-------------------------------------------
    
    @Override
    public void init() {
        if(path == null){
            System.err.println("ComponentSpatial: path not set!!!");
            return;
        }
            
        spatial = parent.getAssetMan().loadModel(path);
        spatial.setLocalScale(spatialScale);
        
        spatialNode = new Node();
        spatialNode.attachChild(spatial);
        parent.getParentNode().attachChild(spatial);
        
    }

    //-------------------------------------------
    
    @Override
    public void run() {
        
    }
    
    //-------------------------------------------

    @Override
    public String getComponentID() {
        return ID;
    }
    
}//class
