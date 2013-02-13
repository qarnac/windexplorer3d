/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author nukesforbreakfast
 */
public class FlyingCameraControl extends AbstractControl{

    public FlyingCameraControl()
    {
        //analyze what we need from the constructor and place it here.
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        //this will be where we update the camera location relative to the input
        //of the user. We will also need to handle physics here in this update
        //loop for the tilting of the camera which CamerPhysics currently handles.
        //Perhaps these two should be split?
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //leave blank because this is advanced stuff we need not touch
    }

    public Control cloneForSpatial(Spatial spatial) {
        //do the needed cloning for spatial here.
    }
    
}
