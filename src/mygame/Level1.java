/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;

/**
 * Class to house all of the information necessary for level one and initialize
 * the level
 * @author nukesforbreakfast
 */
public class Level1 {
    
    private Terrain terrainForLevel;
    private Main handleToMain;
    private BulletAppState physicsState;
    
    public Level1(Main mainHandle, CharacterControl bodyForCamera)
    {
        handleToMain = mainHandle;
        terrainForLevel = new Terrain(mainHandle);
        terrainForLevel.load(handleToMain.getCamera());
        handleToMain.getRootNode().attachChild(terrainForLevel.getTerrainNode());
        
        physicsState = new BulletAppState();
        handleToMain.getStateManager().attach(physicsState);
        physicsState.getPhysicsSpace().add(terrainForLevel.getTerrainQuad());
        physicsState.getPhysicsSpace().add(terrainForLevel.getTerrainRigidBody());
        physicsState.getPhysicsSpace().add(bodyForCamera);
        physicsState.getPhysicsSpace().enableDebug(handleToMain.getAssetManager());
    }
    
//    public Terrain getTerrain()
//    {
//        return terrainForLevel;
//    }
    
    
}
