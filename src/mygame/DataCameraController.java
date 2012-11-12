/*
 * By:Ryan Moe
 * 
 * Used to transfer data from the camera physics engine into the camera
 * movement manager.
 */
package mygame;

import com.jme3.math.Vector3f;

public class DataCameraController {
    //vector showing where camera should move to, in game world.
    Vector3f movementVector;
    //Camera rotation around the y axis in the game world
    float rotationYTotal;
    //tilt around the x-axis
    float tiltX;
    //tilt around the y-axis
    float tiltZ;
    
    public DataCameraController(Vector3f moveVect, float rotationY, float tiltXaxis, float tiltZaxis){
        movementVector = moveVect;
        rotationYTotal = rotationY;
        tiltX = tiltXaxis;
        tiltZ = tiltZaxis;
    }
    
}//class
