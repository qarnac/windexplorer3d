/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author cmb
 */
public class ComponentTownSound{
    
    private AudioNode audioNode;
    private String path;
    
    public ComponentTownSound(Node parentNode, AssetManager assetMan, 
                              String path, float volume, boolean looping){
        
        try{
            this.path = path;
            audioNode = new AudioNode(assetMan, path);
            audioNode.setVolume(volume);
            audioNode.setLooping(looping);
            parentNode.attachChild(audioNode);
        }
        catch(Exception e){
            System.out.println("TownSound: Asset Not Found.");
        }
        finally{
            
        }
        
    }//method
    
    
    public AudioNode getAudioNode(){
        return audioNode;
    }
    
}//class
