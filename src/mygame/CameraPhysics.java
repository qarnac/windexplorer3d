/*
 * @author Ryan
 * 
 * Manages the physics behind the CameraController to make movement
 * act is certain ways, such as acceleration and momentum.  Use this to generate
 * the movementVector and RotateYTotal values, and pass them to the camera controller
 * in the main update.
 * 
 * NOTE: the velocity vector's coordinate system is that of the camera's local system,
 *       so a positive x value for velocity will move the camera left to what it
 *       sees, not left relative in the world space direction.
 * 
 * NOTE: THIS IS NOT FINISHED.
 */
package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;


public class CameraPhysics {
    
    //--------------------------------------------------------------------------
    private short profileID;
    private Main mainHandle;
    
    DataCameraPhysics dataCam;
    
    private float tpf;//time per frame
    
    private Vector3f userInput;//x y and z represent putton presses. 1 or 0.
    private Vector2f userRotationY;//user input for rotation, x=left y=right
    
    private Vector3f velocity;//in camera space.
    
    private Vector3f movementVector;//in game space.
            private Quaternion quatLookAt;//to get velocity into game space.
            
    private float rotationYTotal;
        private float rotationYVelocity;
        
    //a data struct for passing vars to the camera controller.
    private DataCameraController controllerData;
        
    //This is the velocity at which the friction algorithm sets
    //the velocity to zero, to prevent bouncing.
    private final float FRICTION_SENSITIVITY = .3f;
    private final float ROTATIONAL_FRICTION_SENSITIVITY = .0003f;
        
    //CAMERA TILT
    private float tiltX, tiltZ;
    private final float MAX_TILT_X = 0.3f;
    private final float MAX_TILT_Z = 0.3f;
    private final float TILT_ACCEL = 0.4f;
    private final float TILT_FRICTION = 1.2f;
    private final float TILT_FRICTION_THRESHOLD = 0.005f;

    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public CameraPhysics(Main mainHandle){
        this.mainHandle = mainHandle;
        dataCam = new DataCameraPhysics();
        
        //---------------------------
        //DEFAULTS: use default until told otherwise.
        //All values below are the default values.
        //---------------------------
        profileID = 0;
        tpf = 0;
        
        //USER INPUT-----------------
        userInput = new Vector3f(0,0,0);
        userRotationY = new Vector2f(0,0);

        //MOVEMENT-------------------
        velocity = new Vector3f(0,0,0);
            dataCam.maxVelocity = 10;
            dataCam.impulseXZ = 15;
            dataCam.impulseY = 5;
            
            movementVector = new Vector3f(0,0,0);
                quatLookAt = new Quaternion();
                quatLookAt.fromAngleAxis(0, Vector3f.UNIT_Y);
                
            dataCam.mass = 100;
        
        //ROTATION------------------
                
        rotationYTotal = 0;
            dataCam.rotationYImpulse = (float) .08;
            rotationYVelocity = 0;
                dataCam.rotationYVelocityMax = (float) .08;
        
        //FRICTION------------------
        dataCam.frictionX = (float) 2;
        dataCam.frictionY = (float) 4;
        dataCam.frictionZ = (float) 2;
        
        dataCam.frictionYRotation = (float) 3.0;
        
        //TILT---------------------
        tiltX = 0;
        tiltZ = 0;
        
        //init the data struct
        controllerData = new DataCameraController(new Vector3f(), 0, 0, 0);
    }
    
    //--------------------------------------------------------------------------
    //@param tpf = time per frame.
    //@param the original direction the camera is looking at, in the XZ plane only.
    public void update(float tpfIn, Vector3f camOriginalLookAtDir){  
        tpf = tpfIn;
        //keep userInput under 1
        if(userInput.length() > 1){
            userInput = userInput.normalize();
        }
        
        //--------------------------------
        //FRICTION and TILT
        //if the user has no input on an axis,
        //then apply friction to that axis.
        if(userInput.x == 0)
            applyFrictionX();
        if(userInput.y == 0)
            applyFrictionY();
        if(userInput.z == 0)
            applyFrictionZ();
        
        //--------------------------------
        //USER MOVEMENT
        
        //only do this if the user is going slower than the max velocity.
        if(FastMath.abs(velocity.length()) < dataCam.maxVelocity){
            //parallel to ground
            velocity.x += (userInput.x * tpf * dataCam.impulseXZ);
            velocity.z += (userInput.z * tpf * dataCam.impulseXZ);
            velocity.y += (userInput.y * tpf * dataCam.impulseY);
        }
        
        //Need to do something here, otherwise when the user
        //is at max speed they can change directions without slowing down...
        
        //--------------------------------
        //ROTATION
        //--------------------------------
        //user is turning.
        //Cant remember why exactly I did it like this,
        //should have commented it that day!  d'oh!
        if( FastMath.abs(userRotationY.x - userRotationY.y) > .2){
            //Limit to the defined max rotation speed.
            if(FastMath.abs(rotationYVelocity) < dataCam.rotationYVelocityMax){
                //note: left rotation is positive.
                rotationYVelocity += (((userRotationY.x - userRotationY.y) * dataCam.rotationYImpulse) * tpf);
            }//if
        }//if
        
        //rotational friction
        else{
            applyFrictionRotationalY();
        }//else
        
        rotationYTotal += rotationYVelocity;
        
        //--------------------------------
        manageTilt();
        //--------------------------------
        //Important step:
        //make a quaternion that rotates the velocity vector
        //from camera space to world space.  Only on X-Z plane right now.
        quatLookAt.lookAt(camOriginalLookAtDir, Vector3f.UNIT_Y);
        movementVector = quatLookAt.mult(velocity);
    }
    

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    //FRICTION------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private void applyFrictionX(){
        //Dont Overshoot due to framerate crapping up.
        if(tpf > .5){
            velocity.x = 0;
            return;
        }//if
        //Done Moving
        if(FastMath.abs(velocity.x) < FRICTION_SENSITIVITY){
            velocity.x  = 0;
        }
        //Normal Friction
        else{
            velocity.x /= 1 + ( tpf * dataCam.frictionX );
        }
    }//method
    
    private void applyFrictionY(){
        //Dont Overshoot due to framerate crapping up.
        if(tpf > .5){
            velocity.y = 0;
            return;
        }//if
        //Done Moving
        if(FastMath.abs(velocity.y) < FRICTION_SENSITIVITY){
            velocity.y  = 0;
        }
        //Normal Friction
        else{
            velocity.y /= 1 + ( tpf * dataCam.frictionY );
        }
    }//method
    
    private void applyFrictionZ(){
        //Dont Overshoot due to framerate crapping up.
        if(tpf > .5){
            velocity.z = 0;
            return;
        }//if
        //Done Moving
        if(FastMath.abs(velocity.z) < FRICTION_SENSITIVITY){
            velocity.z  = 0;
        }
        //Normal Friction
        else{
            velocity.z /= ( 1 + ( tpf * dataCam.frictionZ ));
        }
    }//method
    
    //--------------------------------------------------------------------------
    //with no user input, slows the rotation speed down.
    private void applyFrictionRotationalY(){
        //lag compensation.
        if(tpf > .5){
            rotationYVelocity = 0;
            return;
        }//if
        //done rotating
        if(FastMath.abs(rotationYVelocity) < ROTATIONAL_FRICTION_SENSITIVITY){
            rotationYVelocity = 0;
        }
        else{
            rotationYVelocity /= (1 + (dataCam.frictionYRotation * tpf));
        }//else
    }
    
    //--------------------------------------------------------------------------
    //TILT----------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    //If the user moves a direction on the XZ plane, the helicopter
    //should tilt that direction.
    private void manageTilt(){
        //X--------------------
        //Adding to tilt
        if( FastMath.abs(userInput.x) > 0.01){
            if(FastMath.abs(tiltX) < MAX_TILT_X)
                tiltX -= ((tpf * TILT_ACCEL) * userInput.x);
        }//if
        
        //Removing tilt
        else{
            if(FastMath.abs(tiltX) < TILT_FRICTION_THRESHOLD)
                tiltX = 0;
            else{
                tiltX /= (1 + (tpf * TILT_FRICTION));
            }//else
        }//else
        
        //Z--------------------
        //Adding to tilt
        if(FastMath.abs(userInput.z) > 0.01){
            if(FastMath.abs(tiltZ) < MAX_TILT_Z)
                tiltZ += ((tpf * TILT_ACCEL) * userInput.z);
        }//if
        
        //Removing tilt
        else{
            if(FastMath.abs(tiltZ) < TILT_FRICTION_THRESHOLD)
                tiltZ = 0;
            else{
                tiltZ /= (1 + (tpf * TILT_FRICTION));
            }//else
        }//else
    }//method
    
    
    //--------------------------------------------------------------------------
    //PUBLIC METHODS------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public void setUserInput(float x, float y, float z){
        userInput.set(x, y, z);
    }
    public void setUserInput(Vector3f in){
        userInput = in.clone();
    }
    public void setUserInputX(float x){
        userInput.x = x;
    }
    public void setUserInputY(float y){
        userInput.y = y;
    }
    public void setUserInputZ(float z){
        userInput.z = z;
    }
    
    //--------------------------------------------------------------------------
    
    public void addToUserInput(float x, float y, float z){
        userInput = userInput.add(x, y, z);
    }
    public void addToUserInput(Vector3f in){
        userInput = userInput.add(in);
    }
    
    //--------------------------------------------------------------------------
    
    public void setUserInputRotationLeftAroundY(float in){
        userRotationY.x = in;
    }
    public void setUserInputRotationRightAroundY(float in){
        userRotationY.y = in;
    }
    
    //--------------------------------------------------------------------------
    
    public Vector3f getMovementVector(){
        return movementVector;
    }//
    public float getRotationY(){
        return rotationYTotal;
    }//

    public float getTiltX(){
        return tiltX;
    }
    
    public float getTiltZ(){
        return tiltZ;
    }
    
    public DataCameraController getCameraControllerStruct(){
        controllerData.movementVector = this.movementVector;
        controllerData.rotationYTotal = this.rotationYTotal;
        controllerData.tiltX = this.tiltX;
        controllerData.tiltZ = this.tiltZ;
        return controllerData;
    }
    
    
    
    //--------------------------------------------------------------------------
    //Change the camera physics profile.  
    //Comes from the camera config file, defined in MasterConfig.txt
    //@return 0 = profile found, 1 = not found.  <-- NOT IMPLEMENTED YET.
    public byte setPhysicsProfile(short index){
        profileID = index;
        dataCam = mainHandle.confReader.getCameraVars(index);
        
        //Make sure that incoming index is valid.
        if (dataCam == null){
            return 1;
        }
        
        //restart everything fresh after a new profile is set.
        resetMovementVars();
        
        return 0;
    }
    
    //--------------------------------------------------------------------------
    //reset all current movement variables,
    //rendering the camera motionless.
    //has not been tested during gameplay yet...
    public void resetMovementVars(){
        userInput = new Vector3f(0,0,0);
        userRotationY = new Vector2f(0,0);
        //MOVEMENT-------------------
        velocity = new Vector3f(0,0,0);  
        movementVector = new Vector3f(0,0,0);
            quatLookAt = new Quaternion();
            quatLookAt.fromAngleAxis(0, Vector3f.UNIT_Y);
        //ROTATION------------------    
        rotationYTotal = 0;
            rotationYVelocity = 0;
    }
}//class



//OLD---------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------





/***OLD*****
 * 
 * /*
            //Done Moving
            if(FastMath.abs(velocity.length()) < .51){
                velocity.x = velocity.y = velocity.z = 0;
            }
            
            //Dont Overshoot
            else if(FastMath.abs(velocity.length()) < 2){
                velocity = velocity.subtract(velocity.normalize().mult(tpf*2));
            }
            
            //Normal Friction
            else{
                velocity = velocity.subtract(velocity.normalize().mult(tpf * friction));
            }

 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * /*
    //USER INPUT-----------------
    private Vector3f userInput;//x y and z represent putton presses. 1 or 0.
    private Vector2f userRotationY;//user input for rotation, x=left y=right
    
    //MOVEMENT-------------------
    private Vector3f velocity;//in camera space.
        private float maxVelocity;
        private int impulseXZ;//change in force by time.
        private int impulseY;//change in force by time.
        
        private Vector3f movementVector;//in game space.
            private Quaternion quatLookAt;//to get velocity into game space.
            
    private int mass;//resists the effects of forces.
    
    //ROTATION------------------
    private float rotationYTotal;
        private float rotationYImpulse;
        private float rotationYVelocity;
            private float rotationYVelocityMax;
        
    //FRICTION------------------
    //private float friction;//generic friction value to constantly slow down the mass.
    private float frictionX;
    private float frictionY;
    private float frictionZ;
    
    private float frictionYRotation;
     * 
     * 
     */
