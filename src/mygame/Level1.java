/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to house all of the information necessary for level one and initialize
 * the level
 * @author nukesforbreakfast
 */
public class Level1 {
    
    private Terrain terrainForLevel;
    private Main handleToMain;
    private BulletAppState physicsState;
    //Handles basic lighting, only the sun right now.
    private SunController sunController;
    private CameraController camController;
    private CameraPhysics camPhys;
    
    //Reading stuff for loadCameraConfig() function
    private BufferedReader bufferedIn;
    private String line;
    //Camera config vars
    private short numberOfProfiles;
    private DataCameraPhysics[] _CamVars;
    
    
    
    public Level1(Main mainHandle)
    {
        handleToMain = mainHandle;
        
        camController = new CameraController(handleToMain);
        
        camPhys = new CameraPhysics(mainHandle);
        
        
        terrainForLevel = new Terrain(mainHandle);
        terrainForLevel.load(handleToMain.getCamera());
        handleToMain.getRootNode().attachChild(terrainForLevel.getTerrainNode());
        
        sunController = new SunController(handleToMain);
        sunController.loadSun();
        sunController.setSunDirection(new Vector3f(-2f,-1f,2f).normalizeLocal());
        
        physicsState = new BulletAppState();
        handleToMain.getStateManager().attach(physicsState);
        physicsState.getPhysicsSpace().add(terrainForLevel.getTerrainQuad());
        physicsState.getPhysicsSpace().add(terrainForLevel.getTerrainRigidBody());
        physicsState.getPhysicsSpace().add(camController.getCameraPhysicsBody());
        physicsState.getPhysicsSpace().enableDebug(handleToMain.getAssetManager());
    }
    
    public void moveCamera(float tpf, Vector3f tempVect)
    {
        camPhys.update(tpf, tempVect);
        camController.moveCamera(camPhys.getCameraControllerStruct());
    }
    
    public CameraPhysics getCameraPhys()
    {
        return camPhys;
    }
    
    public byte loadCameraFile(String configPath){
        //config file not found
        if (configPath == null){
            return 1;
        }//if    
            //inputStream =  new FileInputStream(configFolderPath + cameraConfigName);
            //bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configPath)));
            try {
                getNextValidLine();
                numberOfProfiles = Short.valueOf(line);
                _CamVars = new DataCameraPhysics[numberOfProfiles];
                System.out.println("Camera Config Reader: numberOfProfiles: " + numberOfProfiles);
                for(short i = 0; i < numberOfProfiles; ++i ){
                    _CamVars[i] = new DataCameraPhysics();
                    
                    getNextValidLine();
                    _CamVars[i].maxVelocity = Float.valueOf(line); 
                    getNextValidLine();
                    _CamVars[i].impulseXZ = Integer.valueOf(line);                   
                    getNextValidLine();
                    _CamVars[i].impulseY = Integer.valueOf(line);                 
                    getNextValidLine();
                    _CamVars[i].frictionX = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionY = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionZ = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionYRotation = Float.valueOf(line);      
                    getNextValidLine();
                    _CamVars[i].rotationYImpulse = Float.valueOf(line);    
                    getNextValidLine();
                    _CamVars[i].rotationYVelocityMax = Float.valueOf(line);    
                    getNextValidLine();
                    _CamVars[i].mass = Integer.valueOf(line);
                }//for
                
            } //try
            catch (IOException ex) {
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        
        return 0;
    }
    
    private void getNextValidLine() throws IOException{
        line = bufferedIn.readLine();
        
        //reject empty lines, spaces or comments.
        while(line.equalsIgnoreCase("") || line.startsWith("//") || line.equalsIgnoreCase(" ") || line == null){
            line = bufferedIn.readLine().trim();
        }//while
        
        //System.out.println("LINE: " + line);
        while(line.endsWith("++")){
            line = line.replace("++", "");
            line += "\n" + bufferedIn.readLine();
        }
        
    }//method
    
}
