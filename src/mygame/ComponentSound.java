/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author cmb
 */
public class ComponentSound extends ComponentAbstract{
    
    public final float DEFAULT_VOLUME = (float) 0.2;
    public final String ID = "sound";
    
    String soundPath = null;
    private AudioNode audioNode;
    private float volume = -1;
    private boolean isPlaying = false;
    
    //------------------------------------------------
    
    public void setVolume(float volume){
        this.volume = volume;
    }
    
    //------------------------------------------------
    
    public void setFileName(String pathInput){
        soundPath = pathInput;
    }
    
    //------------------------------------------------
    public void setLooping(boolean isLooping){
        audioNode.setLooping(isLooping);
    }
        
    //------------------------------------------------
    
    public void play(){
        //if(audioNode.){
        if(audioNode != null){
            audioNode.play();
        }
        //}
    }
    
    //------------------------------------------------
    
    @Override
    public void init() {
        
        //did someone set the path?
        if(soundPath == null){
            System.err.println("ComponentSound: path not set!");
            return;
        }//if
        
        //did the programmer set the volume?
        if(volume == -1){
            volume = DEFAULT_VOLUME;
        }
        
        audioNode = new AudioNode(parent.getAssetMan(), soundPath);
        audioNode.setLooping(true);
        audioNode.setVolume(volume);
        
        parent.getParentNode().attachChild(audioNode);
    }//init
    
    //-----------------------------------------------

    @Override
    public void run() {
        this.play();
    }
    
    
    //-----------------------------------------------

    @Override
    public String getComponentID() {
        return ID;
    }
    
    
    
}//class
