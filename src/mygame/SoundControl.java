/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author nukesforbreakfast
 */
public class SoundControl extends AbstractControl {

    private AudioNode sound;  
    private Node rootNode; 
    
    public SoundControl(AssetManager assetMan, String assetPath, boolean streamed
            , Node root)
    {
        /*
         * constructor stuff here
         * should this manage a single sound or multiple sounds?
         * if multiple sounds, pass in a data structure of sounds
         * to control(linked list likely candidate), if not just
         * pass in a single sound. Perhaps this should be the path
         * to the sound asset as read from a config file?
         */
        sound = new AudioNode(assetMan, assetPath, streamed);
        rootNode = root;
    }
    
    public SoundControl(AudioNode incomingAudio, Node root)
    {
        sound = incomingAudio;
        rootNode = root;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(this.isEnabled()) //make sure the sound control is enabled
        {
            //do positional sound updates here if needed
            if(sound.isPositional())
            {
                //these two lines will keep the sound at the same location
                //as the spatial if the sound is positional.
                sound.setLocalTranslation(spatial.getLocalTranslation());
                sound.setLocalRotation(spatial.getLocalRotation());
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //leave blank because this is advanced stuff we need not touch
    }

    public Control cloneForSpatial(Spatial spatial) {
        final SoundControl sControl = new SoundControl(sound, rootNode);
        sControl.setSpatial(spatial);
        sControl.initAudio();
        return sControl;
    }
    
    /*
     * initialize the SoundControl by making sure the audio node is attached
     * to the parent of spatial.
     * WARNING: This means you must attach the spatial to some node before
     * running this function
     */
    public void initAudio()
    {
        Node temp = spatial.getParent();
        //if this node does not have a parent
        if(temp == null)
        {
            rootNode.attachChild(sound);
        }
        else
        {
            temp.attachChild(sound);
        }
        
    }
    
    public void playSound()
    {
        sound.play();
    }
    
    public void pauseSound()
    {
        sound.pause();
    }
    
    public void stopSound()
    {
        sound.stop();
    }
    
    public void setVolume(float f)
    {
        sound.setVolume(f);
    }
}