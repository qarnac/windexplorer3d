/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;

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
    
    public Level1(Main mainHandle)
    {
        handleToMain = mainHandle;
        
        camController = new CameraController(handleToMain);
        
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
    
    public CameraController getCameraControl()
    {
        return camController;
    }
    
    
}
