/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author cmb
 */

public class ContainerTown {
    
    private Node parentNode;
    private AssetManager assetMan;
    
    //Spatial Vars
    private Spatial townSpatial;
    private Quaternion townRotation;
        String modelPath;
        float modelRotation;
        Vector3f modelTranslation;
        float modelScale;
        
    //sound vars
    private AudioNode audioNode;
        private String soundPath;
        private float soundVolume;
        private boolean soundLooping;
    
    
    
    public ContainerTown(Node parentNode, AssetManager assetMan, String modelPath, 
                        float modelRotation, Vector3f modelTranslation, float modelScale,
                        String soundPath, float soundVolume, boolean soundLooping){
        this.parentNode = parentNode;
        this.assetMan = assetMan;
        
        this.modelPath = modelPath;
        this.modelRotation = modelRotation;
        this.modelTranslation = modelTranslation;
        this.modelScale = modelScale;
        
        this.soundPath = soundPath;
        this.soundVolume = soundVolume;
        this.soundLooping = soundLooping;
    }
    
    
    
    public void initModel(){
        try{
            townSpatial = assetMan.loadModel(modelPath);

            //Rotate BEFORE translate
            townRotation = new Quaternion();
            townRotation.fromAngleAxis(modelRotation, Vector3f.UNIT_Y);
            townSpatial.setLocalRotation(townRotation);

            townSpatial.setLocalTranslation(modelTranslation);

            townSpatial.setLocalScale(modelScale);
            
            parentNode.attachChild(townSpatial);
        }
        catch(Exception e){
            System.out.println("Error loading a town.");
        }
    }
    
    
    
    public void initSound(){
        try{
            audioNode = new AudioNode(assetMan, soundPath);
            audioNode.setVolume(soundVolume);
            audioNode.setLooping(soundLooping);
            parentNode.attachChild(audioNode);
            audioNode.play();
        }
        catch(Exception e){
            audioNode = null;
            System.out.println("ContainerTown: Sound Asset Not Found.");
        }
    }
    
    
    
}//class
