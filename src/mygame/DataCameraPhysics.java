/**
 *
 * @author Ryan
 * 
 * This class holds the variables read in from a config file.
 * 
*/

package mygame;


public class DataCameraPhysics {
    
    public float maxVelocity;
    public int impulseXZ;//change in force by time.
    public int impulseY;//change in force by time.
    
    public float frictionX;
    public float frictionY;
    public float frictionZ;
    
    public float frictionYRotation; 
    
    public float rotationYImpulse;
    public float rotationYVelocityMax;
    
    public int mass;//resists the effects of forces.
}
