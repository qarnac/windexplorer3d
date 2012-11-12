/**
 * BY: Ryan Moe
 * 
 * Adds a sun direction lighting object to the scene. 
 */

package mygame;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author Ryan
 */
public class SunController {
    
    //--------------------------------------------------------------------------
    
    Main mainHandle;
    DirectionalLight sun;
    
    //--------------------------------------------------------------------------
    
    public SunController(Main handle){
        mainHandle = handle;
    }
        
    /**
     * Loads a sun object in the scene to a default
     * direction, use setSunLocation to change.
     * 
     * Note: this directional light does not have a 
     * position, each object will get light from the 
     * same direction.
     */
    public void loadSun(){
        sun = new DirectionalLight();
        sun.setName("sun");
        sun.setColor(ColorRGBA.White);
        //default, change per level design.
        sun.setDirection(Vector3f.UNIT_Y.negate());
        mainHandle.getRootNode().addLight(sun);
    }//method
    
    
    /**
     * Note: this directional light does not have a 
     * position, each object will get light from the 
     * same direction.
     * 
     * @param in direction the sun's light should point.
     */
    public void setSunDirection(Vector3f in){
        sun.setDirection(in);
    }//method
    
    
    /**
     * Removes the sun object from the rootNode, must
     * call loadSun() to put sun back in.
     */
    public void removeSun(){
        mainHandle.getRootNode().detachChildNamed("sun");
    }
    
    
}//class
