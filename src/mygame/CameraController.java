/**
 * @author Ryan
 * 
 * Controls the camera movement.  
 * Note: CameraPhysics is used to emulate different camera movement profiles,
 *       use that to make movement vectors and pass them in to here.
 * 
 * Note: There is an invisible object that ties the camera into the jme3's
 *       built in physics system, so terrain collision is handled.
 */
package mygame;
 
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;


public class CameraController{
    
    //--------------------------------------------------------------------------
    private Main mainHandle;
    
    private Camera cam;
    
    private Vector3f positionVector;
        private Vector3f originalPositionVectorClone;
    private Quaternion quat;
    
    private CharacterControl cameraPhysicsBody;
    CapsuleCollisionShape capsuleShape;
    
    private final short MAX_HEIGHT = 250;
    //private final float CAMERA_START_X = -2000f;
    //private final float CAMERA_START_Y = 10f;
    //private final float CAMERA_START_Z = -2000f;
    /*
     * Causey comments: These are the starting values for the camera relative
     * to the first level correct? If so these will need to not be hard coded
     * when we add additional levels.
     */
    private final float CAMERA_START_X = -2064f;
    private final float CAMERA_START_Y = 16f;
    private final float CAMERA_START_Z = -3037f;
    
    //--------------------------------------------------------------------------
    public CameraController(Main mainHandle){
        this.mainHandle = mainHandle;
        
        cam = mainHandle.getCamera();
        positionVector = new Vector3f(CAMERA_START_X,CAMERA_START_Y,CAMERA_START_Z);
        cam.setLocation(positionVector);

        //PHYSICS STUFF
        capsuleShape = new CapsuleCollisionShape(10f, 10f, 1);
        cameraPhysicsBody = new CharacterControl(capsuleShape, 0.05f);
        cameraPhysicsBody.setPhysicsLocation(new Vector3f(CAMERA_START_X, CAMERA_START_Y, CAMERA_START_Z));
        cameraPhysicsBody.setGravity(0);
        
    }//method
    
    //--------------------------------------------------------------------------
    //The integration with the jme3 physics system is crappy, but it works for now.
    public void moveCamera(Vector3f move, float rotation, float tiltX, float tiltZ){
        //debugging outputs
        if(mainHandle.DEBUG_GLOBALS_OBJ.DEBUG_OUTPUT_CAM_POSITION){
            System.out.println("----------------------------\nPosition Vector: " + positionVector);
            System.out.println("Move Vector: " + move + "\n----------------------------\n");
        }
        //remember the previous position vector.
        originalPositionVectorClone = positionVector.clone();
        //Movement
        positionVector = positionVector.add(move);
        //Did we reach the height limit?
        if(positionVector.y > MAX_HEIGHT){
            positionVector.y = MAX_HEIGHT;
        }
        
        //move the physics body, walk in the direction of the difference
        //between the original position vector and the new one.
        /*
         * Causey Comments: Moe! I am failing at vector math over here....
         * This obviously works but as far as I understood vector maths wouldn't
         * subtracting positionVector from its original clone set the walk in
         * the oppposite direction from where we want to walk? Help!
         */
        cameraPhysicsBody.setWalkDirection(positionVector.subtract(originalPositionVectorClone));
        
        //System.out.println("move Vector: " + move);
        //System.out.println("position Vector: " + positionVector);
        
        //Put camera onto its physics body.
        cam.setLocation(cameraPhysicsBody.getPhysicsLocation().clone());
        
        //reset position vector to reflect what the phsyics engine actually moved.
        positionVector = cameraPhysicsBody.getPhysicsLocation().clone();
        
        //Rotation and Tilt
        //Works...
        //quat = new Quaternion();
        //quat.fromAngleAxis(rotation, Vector3f.UNIT_Y);
        //cam.setRotation(quat);
        //Testing...
        quat = new Quaternion();
        quat.fromAngles(tiltZ, rotation, tiltX);
        cam.setRotation(quat);
        
        //System.out.println("Camera Location: " + cam.getLocation() + "\nCamera Tilt X and Z: " + tiltX + " " + tiltZ + "\n-----------------------------------");
    }

    
    //--------------------------------------------------------------------------
    //Copy of old moveCamera method, trying out new data struct.
    public void moveCamera(DataCameraController dataStructIn){
        //debugging outputs
        if(mainHandle.DEBUG_GLOBALS_OBJ.DEBUG_OUTPUT_CAM_POSITION){
            System.out.println("----------------------------\nPosition Vector: " + positionVector);
            System.out.println("Move Vector: " + dataStructIn.movementVector + "\n----------------------------\n");
        }
        //remember the previous position vector.
        originalPositionVectorClone = positionVector.clone();
        //Movement
        positionVector = positionVector.add(dataStructIn.movementVector);
        //Did we reach the height limit?
        if(positionVector.y > MAX_HEIGHT){
            positionVector.y = MAX_HEIGHT;
        }
        
        //move the physics body, walk in the direction of the difference
        //between the original position vector and the new one.
        /*
         * Causey Comments: Moe! I am failing at vector math over here....
         * This obviously works but as far as I understood vector maths wouldn't
         * subtracting positionVector from its original clone set the walk in
         * the oppposite direction from where we want to walk? Help!
         */
        cameraPhysicsBody.setWalkDirection(positionVector.subtract(originalPositionVectorClone));
        
        //System.out.println("move Vector: " + move);
        //System.out.println("position Vector: " + positionVector);
        
        //Put camera onto its physics body.
        cam.setLocation(cameraPhysicsBody.getPhysicsLocation().clone());
        
        //reset position vector to reflect what the phsyics engine actually moved.
        positionVector = cameraPhysicsBody.getPhysicsLocation().clone();
        
        //Rotation and Tilt
        //Works...
        //quat = new Quaternion();
        //quat.fromAngleAxis(rotation, Vector3f.UNIT_Y);
        //cam.setRotation(quat);
        //Testing...
        quat = new Quaternion();
        quat.fromAngles(dataStructIn.tiltZ, dataStructIn.rotationYTotal, dataStructIn.tiltX);
        cam.setRotation(quat);
        
        //System.out.println("Camera Location: " + cam.getLocation() + "\nCamera Tilt X and Z: " + tiltX + " " + tiltZ + "\n-----------------------------------");
    }
    
    
    //--------------------------------------------------------------------------
    /*
    //Not working, need to update the camphysics also...
    private void setCameraPosition(float x, float y, float z){
        positionVector.x = x;
        positionVector.y = y;
        positionVector.z = z;
        cam.setLocation(positionVector);
    }
     * 
     */
    
    /*
     * Causey Comments: this function is not used anywhere. What is it for?
     */
    public void rotateCamera(float x, float y, float z){
        float[] angles = new float[]{x,y,z};
    }
    
    
    /*
     * Getter Function for returning the camera character control
     */
    public CharacterControl getCameraPhysicsBody(){
        return cameraPhysicsBody;
    }
       
}//class
