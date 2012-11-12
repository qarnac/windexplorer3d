/**
 * @author Ryan
 * 
 * Definds and Listens for user input.
 */

package mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;


public class InputController implements AnalogListener, ActionListener{
    
    private Main mainHandle;
    private InputManager IM;
    private short mappingID;
    
    public final short HELICOPTER_MAP_ID = 0;
    
    //--------------------------------------------------------------------------
    
    public InputController(Main mainHandle){
        this.mainHandle = mainHandle;
        IM = mainHandle.getInputManager();
    }
    
    //--------------------------------------------------------------------------
    
    //@param mapID is the different maps available.  
    //0 = helicopter.
    public void mapInputs(short mapID){
        mappingID = mapID;
        //Get rid of any mapping already existing.
        IM.clearMappings();
        //Helilcopter mappings
        if(mappingID == HELICOPTER_MAP_ID){
            IM.addMapping("EscKey",  new KeyTrigger(KeyInput.KEY_ESCAPE));
            IM.addMapping("Wkey",  new KeyTrigger(KeyInput.KEY_W));
            IM.addMapping("Akey",  new KeyTrigger(KeyInput.KEY_A));
            IM.addMapping("Skey",  new KeyTrigger(KeyInput.KEY_S));
            IM.addMapping("Dkey",  new KeyTrigger(KeyInput.KEY_D));
            IM.addMapping("Qkey",  new KeyTrigger(KeyInput.KEY_Q));
            IM.addMapping("Ekey",  new KeyTrigger(KeyInput.KEY_E));
            IM.addMapping("Pkey",  new KeyTrigger(KeyInput.KEY_P));//up
            IM.addMapping("Lkey",  new KeyTrigger(KeyInput.KEY_L));//down
            IM.addMapping("SpaceKey", new KeyTrigger(KeyInput.KEY_SPACE));
            //new MouseButtonTrigger(MouseInput.BUTTON_LEFT)
            
            // Add the names to the action listener.
            IM.addListener(this, new String[]{"EscKey", "Wkey", "Akey", "Skey", "Dkey", "SpaceKey", "Qkey", "Ekey","Pkey","Lkey"});
        }//if
        
        
        
    }//method
    
    //--------------------------------------------------------------------------
    //AnalogListener are triggered repeatedly and gradually. 
    public void onAnalog(String name, float value, float tpf) {
        
    }
    
    //--------------------------------------------------------------------------
    //ActionListener are digital either-or actions – "Pressed or released? On or off?" 
    public void onAction(String name, boolean isPressed, float tpf) {
        //System.out.println("IC: input name: " + name + "   isPressed: " + isPressed );
        
        //Helicrapter mappings
        if(mappingID == HELICOPTER_MAP_ID){
            if (name.equals("EscKey")) {
                mainHandle.stopGameAndGoToMenu();
            }
            //------------------------------
            //FORWARD
            if (name.equals("Wkey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputZ(1);
                else
                    mainHandle.camPhys.setUserInputZ(0);
            }

            //BACKWARD
            if (name.equals("Skey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputZ(-1);
                else
                    mainHandle.camPhys.setUserInputZ(0);
            }

            //LEFT
            if (name.equals("Akey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputX(1);
                else
                    mainHandle.camPhys.setUserInputX(0);
            }

            //RIGHT
            if (name.equals("Dkey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputX(-1);
                else
                    mainHandle.camPhys.setUserInputX(0);
            }

            //UP
            if (name.equals("Pkey")) {
                if(isPressed){
                    mainHandle.camPhys.setUserInputY(1);
                    mainHandle.soundsManager.setVolume(
                            mainHandle.soundsManager.HELI_BLADES_INDEX, 
                            mainHandle.soundsManager.HELI_BLADES_HIGH_VOLUME);
                }//if
                else{
                    mainHandle.camPhys.setUserInputY(0);
                    mainHandle.soundsManager.setVolume(
                            mainHandle.soundsManager.HELI_BLADES_INDEX, 
                            mainHandle.soundsManager.HELI_BLADES_DEFAULT_VOLUME);
                }//else
            }//if

            //DOWN
            if (name.equals("Lkey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputY(-1);
                else
                    mainHandle.camPhys.setUserInputY(0);
            }//if

            //ROTATE LEFT
            if (name.equals("Qkey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputRotationLeftAroundY(1);
                else
                    mainHandle.camPhys.setUserInputRotationLeftAroundY(0);
            }//if

            //ROTATE RIGHT
            if (name.equals("Ekey")) {
                if(isPressed)
                    mainHandle.camPhys.setUserInputRotationRightAroundY(1);
                else
                    mainHandle.camPhys.setUserInputRotationRightAroundY(0);
            }//if

            //LAND
            if (name.equals("SpaceKey")) {
                if(isPressed){
                    //see if there were any raycast results this frame.
                    if(mainHandle.placementRaycastHit){
                        mainHandle.fromIC_userWantsToLand();
                    }//if
                }//if
                
            }//if
        
        }//if helicopter mapping

        
        //more mappings go here...
        
        
        
    }//method
    
    //--------------------------------------------------------------------------
    
    public void removeInputs(){
        IM.clearMappings();
    }
    
}//class
