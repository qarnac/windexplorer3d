/*
 * BY: Ryan Moe
 * Loads and plays all the souns in the game.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author Ryan
 */
public class SoundsManager {
    
    //--------------------------------------------------------------------------
    //UPDATE THIS WHEN ADDING A NEW SOUND!
    private final short NUMBER_OF_AUDIO_NODES = 5;
    
    //For each sound, please include:
    //its index in the array
    //it's path to the asset relative to the /asset/ folder
    //it's default volume
    //then any extra info you need for that sound.
    
    public final short HELI_BLADES_INDEX = 0;
        public final String HELI_BLADES_PATH = "Sounds/heli_blades.wav";
        public final float HELI_BLADES_DEFAULT_VOLUME = (float).2;
        public final float HELI_BLADES_HIGH_VOLUME = (float).3;
        
      
    public final short BUTTON_PRESS_INDEX = 1;
        public final String BUTTON_PRESS_PATH = "Sounds/Button_Press.wav";
        public final float BUTTON_PRESS_DEFAULT_VOLUME = (float).9;
        
        
    public final short MENU_MUSIC_INDEX = 2;
        public final String MENU_MUSIC_PATH = "Sounds/menu_music.wav";
        public final float MENU_MUSIC_DEFAULT_VOLUME = (float).2;
        
        
    public final short LEVEL_ONE_END_LOSS_INDEX = 3;
        public final String LEVEL_ONE_END_LOSS_PATH = "Sounds/level_one_end_loss.wav";
        public final float LEVEL_ONE_END_LOSS_DEFAULT_VOLUME = (float).4;
        
        
    public final short LEVEL_ONE_WIN_INDEX = 4;
        public final String LEVEL_ONE_WIN_PATH = "Sounds/level_one_win.wav";
        public final float LEVEL_ONE_WIN_DEFAULT_VOLUME = (float).2; 
        
        
        
    
    //--------------------------------------------------------------------------
    //contains the assets for every sound.
    private AudioNode[] _audioNodes;
    
    private AssetManager assetMan;
    private Node guiNode;
    private DebugGlobals debuggingGlobals;

    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public SoundsManager(DataSoundManager incomingData){
        assetMan = incomingData.assetMan;
        guiNode = incomingData.GUINode;
        debuggingGlobals = incomingData.debugGlobalsObj;
        
        _audioNodes = new AudioNode[NUMBER_OF_AUDIO_NODES];
        
        //Gets assets for these sounds,
        //they probably will be used so load them at constructor.
        //Sounds that are not for sure needed, be sure to load them
        //when needed, not here!
        loadHeliBlades();
        loadButtonClick();
        loadMenuMusic();
    }//method
    
    //--------------------------------------------------------------------------
    /**
     * This is loaded in SM's constructor.
     */
    public void loadHeliBlades(){
        //Adding "true" as a second param would make this
        //streaming instead of buffered.
        _audioNodes[HELI_BLADES_INDEX] = new AudioNode(assetMan, HELI_BLADES_PATH);
        _audioNodes[HELI_BLADES_INDEX].setLooping(true);
        _audioNodes[HELI_BLADES_INDEX].setVolume(HELI_BLADES_DEFAULT_VOLUME);
        guiNode.attachChild(_audioNodes[HELI_BLADES_INDEX]);
    }
    
    
     /**
     * This is loaded in SM's constructor.
     */
    public void loadButtonClick(){
        //Adding "true" as a second param would make this
        //streaming instead of buffered.
        _audioNodes[BUTTON_PRESS_INDEX] = new AudioNode(assetMan, BUTTON_PRESS_PATH);
        _audioNodes[BUTTON_PRESS_INDEX].setLooping(false);
        _audioNodes[BUTTON_PRESS_INDEX].setVolume(BUTTON_PRESS_DEFAULT_VOLUME);
        guiNode.attachChild(_audioNodes[BUTTON_PRESS_INDEX]);
    }
    
    
     /**
     * This is loaded in SM's constructor.
     */
    public void loadMenuMusic(){
        //Adding "true" as a second param would make this
        //streaming instead of buffered.
        _audioNodes[MENU_MUSIC_INDEX] = new AudioNode(assetMan, MENU_MUSIC_PATH);
        _audioNodes[MENU_MUSIC_INDEX].setLooping(true);
        _audioNodes[MENU_MUSIC_INDEX].setVolume(MENU_MUSIC_DEFAULT_VOLUME);
        guiNode.attachChild(_audioNodes[MENU_MUSIC_INDEX]);
    }
    
    
    
    /**
     * Use this method to load the sound before you
     * need to play it.
     */
    public void loadLevelOneEndLossSceneMusic(){
        //Adding "true" as a second param would make this
        //streaming instead of buffered.
        _audioNodes[LEVEL_ONE_END_LOSS_INDEX] = new AudioNode(assetMan, LEVEL_ONE_END_LOSS_PATH);
        _audioNodes[LEVEL_ONE_END_LOSS_INDEX].setLooping(false);
        _audioNodes[LEVEL_ONE_END_LOSS_INDEX].setVolume(LEVEL_ONE_END_LOSS_DEFAULT_VOLUME);
        guiNode.attachChild(_audioNodes[LEVEL_ONE_END_LOSS_INDEX]);
    }
    
    
    /**
     * Use this method to load the sound before you
     * need to play it.
     */
    public void loadLevelOneWinSceneMusic(){
        //Adding "true" as a second param would make this
        //streaming instead of buffered.
        _audioNodes[LEVEL_ONE_WIN_INDEX] = new AudioNode(assetMan, LEVEL_ONE_WIN_PATH);
        _audioNodes[LEVEL_ONE_WIN_INDEX].setLooping(false);
        _audioNodes[LEVEL_ONE_WIN_INDEX].setVolume(LEVEL_ONE_WIN_DEFAULT_VOLUME);
        guiNode.attachChild(_audioNodes[LEVEL_ONE_WIN_INDEX]);
    }
    
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //If you want to control the volume of a sound.
    //Use the public final vars to determine the sound's
    //index in the array.
    public void setVolume(short index, float volume){
        _audioNodes[index].setVolume(volume);
    }
    
    //--------------------------------------------------------------------------
    //Use the public final vars to determine the sound's
    //index in the array.
    //playing: True = make it play, False = make it stop playing
    //returns 0 for "a-ok", 1 for "fuuuuuuuuuuu..."
    public byte setPlaying(short index, boolean playing){
        //turns the sound off for debugging.
        if(!debuggingGlobals.DEBUG_SOUND_OFF){
            //make sure you arn't stustustupid or something.
            if(_audioNodes[index] != null){
                if(playing) { //if it should be playing
                    _audioNodes[index].play();
                }
                else { //else it needs to be paused
                    _audioNodes[index].pause();
                }
                return 0;
            }//if
            else {
                return 1;
            }
        }//if
        return 0;
    }//method
    
    
    /**
     * All sounds will stop playing.
     */
    public void stopAllSounds(){
        for(int i = 0; i < _audioNodes.length; ++i){
            if(_audioNodes[i] != null){
                _audioNodes[i].stop();
            }//if
        }//for
    }
    
    
}//class
