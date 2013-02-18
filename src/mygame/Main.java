/**
 * @author Ryan
 * 
 * Wind Explorer 3D
 */

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;


public class Main extends SimpleApplication {
    
    
    /*
     * MAIN - doesn't do much other than start our app class.
     */
    public static void main(String[] args) {
        //These calls should reduce the number of events in the logger (or console).
        //Logger.getLogger("de.lessvoid").setLevel(Level.SEVERE);
        //Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
        
        Main app = new Main();
            AppSettings newSetting = new AppSettings(true);
            newSetting.setFrameRate(28);
            newSetting.setFullscreen(false);
            newSetting.setResolution(640, 480);
            //could this be used for something?
            newSetting.setUseJoysticks(false);
            app.setSettings(newSetting);
        app.start();
    }//main
    
    //--------------------------------------------------------------------------
    //VARS----------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    //handles the main menu
    public MainMenu mainMenu;
    
    //reads in a .txt file full of configurations
    //used to define different aspects of the game.
    public ConfigReader confReader;
    
    //interfaces with the built in InputManager to
    //bring the user's input into the game engine.
    public InputController inputCont;
    
    //Handles moving the camera around the game world.
    //Note: don't consider this an "physics engine" of any kind.
    public CameraController camController;
    
        //This is a camera physics engine that outputs
        //new position vectors for the camera.  Pipe
        //these outputs into the camera controller.
        public CameraPhysics camPhys;
        
            //band-aid to a problem.
            //used to strip off y-axis from vector from cam phys.
            //to fix camera tilting issue .
            private Vector3f tempVect;
    
    //Handles the terrain.
    public Terrain terrainObj;
    
    //Handles basic lighting, only the sun right now.
    public SunController sunController;

    //Handles the cockpit image the user sees while
    //in a vehicle.  Also handles the HUD that displays
    //text for the user.
    public Cockpit cockpitObj;
    
        //basic timer, currently being
        //piped into the cockpit's HUD manager.
        public LevelTimer levelTimer;
    
    //Is the 3D game playing?
    //Used to turn off the main update loop
    //while the menu or intro is going.
    public boolean gamePlaying;
    
    //JME3 class.  Used for terrain
    //collision.  Could be used for more
    //in the future!
    public BulletAppState physicsState;
    
    //This is a manager for a 2D "scene" system.
    //You can display a series of images to the user
    //to show a back story or whatever.
    public SceneManager sceneManager;
    
    //For playing sounds.
    public SoundsManager soundsManager;
    
    //Uses config file to define area's the user could check
    //to build a wind farm.
    public WindFarmPlacementChecker windFarmPlacementChecker;
    
        //a hit was detected
        public boolean placementRaycastHit;
        
        //all the results from a raycast into the 
        //windfarm placement objects.
        public CollisionResults placementRaycastResults;

        
    //displays a town (and clones of said town)
    //all around the map.
    public ModelsTowns townsManager;
    
    //the level the user is on.
    public short currentLevel;
    
    //Holds general data for level one,
    //such as the game time, etc.
    public final DataLevelOneGeneral dataLevel1General = new DataLevelOneGeneral();
    
    //--------------------------------------------------------------------------
    
    //This is slowly replacing the debug flags below...
    private DebugGlobals DEBUG_GLOBALS_OBJ = new DebugGlobals();
    
    public final boolean DEBUG_INTRO_OFF = true;
    public final boolean DEBUG_OUTPUT_CAM_POSITION = false;
    public final boolean DEBUG_SHOW_COCKPIT = true;

    
    //--------------------------------------------------------------------------
    //FIRST!!!
    //Overriding default App method.
    //This is called when the app starts, so
    //initialize everything here.
    @Override
    public void simpleInitApp() {   
        setDisplayStatView(false); 
        setDisplayFps(false);
        
        //Bool describing game state,
        //main menu comes first.
        gamePlaying = false;
        currentLevel = -1; //for nothing is loaded
        
        //dont use use default camera controls
        flyCam.setEnabled(false);
        
        //set camera to see further
        cam.setFrustumFar(10000);
        //so the frustrom update takes effect.
        cam.onFrameChange();
                
        //Init the main menu.
        //Note: do this before initGameVars,
        //since that can call MainMenu stuff.
        mainMenu = new MainMenu(this);
        
        //load up some stuff before menu, cut down on
        //load time later on.
        preloadGameVars();
        
        //show the main menu
        startMainMenu(null);
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Shows the main menu to the user.
     * @param inError any errors encountered.  
     * These will come up as text to the user.
     */
    public void startMainMenu(String inError){
        //remove any leftovers.
        //mainMenu.stopGUI();//null pointers
        mainMenu.startMainMenu(inError);
    }
    


    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    /**
     * Sets up a bunch of variables needed for both the
     * 3d game and for the scene manager to the levels.
     * Note: these should be general use vars, not for just
     * a single level.
     */
    public void preloadGameVars(){
        //for playing all kinds of sounds, 
        //in game and out of game.
        DataSoundManager soundData = new DataSoundManager();
        soundData.GUINode = this.getGuiNode();
        soundData.assetMan = this.getAssetManager();
        soundData.debugGlobalsObj = this.DEBUG_GLOBALS_OBJ;
        soundsManager = new SoundsManager(soundData);
        
        //init config file reader---------
        confReader = new ConfigReader();
        //This is hard coded (for now) because you should not 
        //ever change the master config file location.
        confReader.setFile("/Interface/Configs/");
        
        //LOAD EXTERNAL CONFIG FILES--------------
        //each load function returns if it was successful or not.
        //This usually means we should return to the main menu
        //with an error message.
        
        //error in loading file, return to main menu.
        if(confReader.loadMasterFile() != 0){
            startMainMenu("master config not found");
            return;
        }//if
        
        if(confReader.loadCameraFile() != 0){
            startMainMenu("camera config not found");
            return;
        }//if

        if(confReader.loadIntroScreenConfig() != 0){
            startMainMenu("intro scene config not found.");
            return;
        }
        
        if(confReader.loadPlaceRejectConfig() != 0){
            startMainMenu("placement config not found.");
            return;
        }
        
        //This manages the shapes that represent reasons
        //not to place a wind farm somewhere in a level.
        DataWindFarmPlacementChecker WFPCdata = new DataWindFarmPlacementChecker();
        WFPCdata.assetMan = this.getAssetManager();
        WFPCdata.rootNode = this.getRootNode();
        WFPCdata.debugGlobals = this.DEBUG_GLOBALS_OBJ;
        WFPCdata.configReader = this.confReader;
        WFPCdata.gameCam = this.getCamera();
        windFarmPlacementChecker = new WindFarmPlacementChecker(WFPCdata);
        
        //Load the town model's assets.
        DataModelsTowns townData = new DataModelsTowns();
        townData.assetManager = this.getAssetManager();
        townData.rootNode = this.getRootNode();
        townData.globalFlags = this.DEBUG_GLOBALS_OBJ;
        townsManager = new ModelsTowns(townData);
        townsManager.loadTownAssets();
        
        //Manages the 2d "scenes" in the game engine.
        sceneManager = new SceneManager(this, settings);
        
        //generic timer for levels
        levelTimer = new LevelTimer();
    }//method
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    /**
     * The main update loop for the game engine.
     * Try and keep this method lightweight.
     * @param tpf "Time Per Frame"
     */
    @Override
    public void simpleUpdate(float tpf) {
        //if a scene is being displayed,
        //make sure to update the manager for
        //the time that has passed.
        if(sceneManager.getIsRunning()){
            sceneManager.update(tpf);
        }//if
        
        //hello... is this thing even on???
        if(gamePlaying){
            //the reason for the tempVect is to strip off the y var
            //from camera's direction vector.
            //There is a strange error in JME where just setting a vector's
            //component to 0 (myVec.y = 0) doesn't always work.  Might get fixed?
            tempVect = cam.getDirection();
            tempVect.y = 0;
            //The physics engine doesnt do anything other than generate some
            //internal numbers based on its algorithms.
            camPhys.update(tpf, tempVect);
            //put the numbers generated from the physics engine into the
            //object that moves the camera.
            //OLD WAY:
            //camController.moveCamera(camPhys.getMovementVector(), camPhys.getRotationY(), camPhys.getTiltX(), camPhys.getTiltZ());
            //NEW WAY(faster?  should test...):
            camController.moveCamera(camPhys.getCameraControllerStruct());
            
            cockpitObj.update(tpf);
            
            //GAME TIMER--------------------------------
            //make sure timer is even running.
            if(levelTimer.getTimerIsRunning()){
                //update the time
                levelTimer.updateTimer(tpf);
                //update the HUD
                cockpitObj.setTimeRemaining(levelTimer.timeRemaining);
                //YOU LOSE!!!  GOOD DAY SIR!!!
                if(levelTimer.timeRemaining <= 0){
                    levelTimer.setTimerRunning(false);
                    gamePlaying = false;
                    if(!DEBUG_INTRO_OFF)
                        routerForEndLossScene();
                    else
                        returnToMainMenuFromScene();
                    return;
                }//if you lose!
            }//if timer is running
            
            //------------------------------------------

            //FIND INVISIBLE PLACEMENT REJECTION BLOCKS
            //AND TELL THE USER THEY CAN LAND
            placementRaycastResults = windFarmPlacementChecker.getPlacementResult();
            placementRaycastHit = placementRaycastResults.size() > 0;

            if(placementRaycastHit){
                cockpitObj.setHUDtextUnedited("Press Space to\nland here.");
            }//if
            else{
                cockpitObj.setHUDtextUnedited("");
            }//else
                
            //show changes to HUD
            cockpitObj.buildAndShowText();
            
            listener.setLocation(cam.getLocation());
            listener.setRotation(cam.getRotation());
        }//if gamePlaying
             
    }//method
    
    
    //--------------------------------------------------------------------------

    
    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    //USE THESE METHODS TO START DIFFERENT EVENTS,
    //THEY WILL TURN ON THE CORRECT EVENT FOR A SPECIFIC LEVEL.
    
    
    //Will stop the game and show the correct
    //end scene for the current level.
    //Make sure currentLevel var is up to date...
    public void routerForEndLossScene(){
        if(currentLevel == 1){
            showLevelOneExitLoss();
        }//if
        //if(currentLevel== 2){
            //...
        //}//if
    }//method
    
    //Will stop the game and show the correct
    //end win scene for the current level.
    //Make sure currentLevel var is up to date... 
    public void routerForEndWinScene(){
        if(currentLevel == 1){
            showLevelOneWin();
        }//if
    }
    
    //Will stop the menu and show the correct
    //start scene for the current level.
    public void routerForStartScene(){
        if(currentLevel == 1){
            showLevelOneIntro();
        }
    }//method
    
    //will stop everything and begin
    //the correct level.
    public void routerForStartLevel(){
        if(currentLevel == 1){
            startLevel1();
        }
    }//method
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    
    //--------------------------------------------------------------------------
    //THESE GET CALLED FROM THE MAIN MENU CONTROLLER----------------------------
    //--------------------------------------------------------------------------
    /*
     * Causey Comments:
     * Why are these defined here instead of in the MainMenuController? Since
     * a handle to Main is passed to MainMenuController, as I understand it,
     * MainMenuController should be able to have this function internalized 
     * within itself. I could be totally wrong of course.
     */
    
    //Gets called from the main menu controller,
    //which handles gui interactions.
    public void mainMenuStartGame(){
        System.out.println("GUI: Start Game butten pressed.");
        //stop the main menu gui.
        mainMenu.stopGUI();
        guiNode.detachAllChildren();
        
        //SUPER TEMP HEY HEY HEY FIX ME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        currentLevel = 1;
        
        if(!DEBUG_INTRO_OFF){
            routerForStartScene();
            
        }//if
        else{
            routerForStartLevel();
        }
    }//method
    //--------------------------------------------------------------------------
    //Gets called from the main menu controller,
    //which handles gui interactions.
    public void mainMenuStopProgram(){
        System.out.println("GUI: Quit butten pressed.");
        //stop the main menu gui.
        mainMenu.stopGUI();
        this.stop();
    }
    //--------------------------------------------------------------------------
    //stops the game, goes back to menu.
    public void stopGameAndGoToMenu(){
        System.out.println("GAME: Esc butten pressed.");
        //stop the game.
        gamePlaying = false;
        rootNode.detachAllChildren();
        guiNode.detachAllChildren();
        soundsManager.stopAllSounds();
        inputCont.removeInputs();
        //start menu.
        startMainMenu("Game Stopped");
        //mainMenu.startMainMenu("Game Stopped");
    }
    
    
    //--------------------------------------------------------------------------
    //From Level Intro Manager--------------------------------------------------
    //--------------------------------------------------------------------------
    //The level intro/exit manager will call this method when
    //the scene it is currently palying finishes,
    //the param is the index of the scene it was playing
    //in the config file.
    public void sceneReportingInFinished(short indexOfSceneFinished){
        //level one intro scene done, start the level.
        if(indexOfSceneFinished == sceneManager.LEVEL_1_INTRO_INDEX){
            routerForStartLevel();
            return;
        }//if
        
        //level one exit loss scene done, go to main menu.
        if(indexOfSceneFinished == sceneManager.LEVEL_1_ENDTRO_LOSE_INDEX){
            returnToMainMenuFromScene();
        }//if
        
        //level one win scene done, go to main menu.
        if(indexOfSceneFinished == sceneManager.LEVEL_1_WIN_INDEX){
            returnToMainMenuFromScene();
        }//if
        
    }//method
    
    
    
    //End the current game, called from a 
    //scene ending, either a win or lose or ...
    public void returnToMainMenuFromScene(){
        System.out.println("scene is ending current game.");
        //stop the game.
        gamePlaying = false;
        rootNode.detachAllChildren();
        guiNode.detachAllChildren();
        inputCont.removeInputs();
        //start menu.
        startMainMenu("");
    }
    
    
    
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //Before the level starts up, show the intro scene to that level.
    public void showLevelOneIntro(){
        sceneManager.startScene(sceneManager.LEVEL_1_INTRO_INDEX);
    }

    public void showLevelOneExitLoss(){
        //load sound assets.
        soundsManager.loadLevelOneEndLossSceneMusic();
        //stop all other music
        soundsManager.stopAllSounds();
        //play sounds
        soundsManager.setPlaying(soundsManager.LEVEL_ONE_END_LOSS_INDEX, true);
        sceneManager.startScene(sceneManager.LEVEL_1_ENDTRO_LOSE_INDEX);
        gamePlaying = false;
    }
    
    public void showLevelOneWin(){
        //load sound assets.
        soundsManager.loadLevelOneWinSceneMusic();
        //stop all other music
        soundsManager.stopAllSounds();
        //play sounds
        soundsManager.setPlaying(soundsManager.LEVEL_ONE_WIN_INDEX, true);
        sceneManager.startScene(sceneManager.LEVEL_1_WIN_INDEX);
        gamePlaying = false;
    }
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    //level 1 vars
    Node townNode1;
    Node townNode2;
    Node townNode3;
    
    //Will start the first level
    public void startLevel1(){  
        //Controls user input
        inputCont = new InputController(this);
        //map the input to the first profile in the config file.
        inputCont.mapInputs((short)0);
        
        //init camera--------------
        camController = new CameraController(this);
        camPhys = new CameraPhysics(this);
        //change camera physics profile from default to helicopter.
        if(camPhys.setPhysicsProfile((short) 0) != 0){
            startMainMenu("An internal error has occured.");
            gamePlaying = false;
            return;
        }//if
        

        //TERRAIN AND SKY----------------
        terrainObj = new Terrain(this);
        terrainObj.load(getCamera());
        rootNode.attachChild(terrainObj.getTerrainNode());
        
        //Sun----------------------------
        
        //We only use the sun for lighting some models, like the towns.
        //Other models, like the terrain, are not lit
        sunController = new SunController(this);
        sunController.loadSun();
        sunController.setSunDirection(new Vector3f(-2f,-1f,2f).normalizeLocal());
        
        //-------------------------------
        
        //COCKPIT STUFF
        cockpitObj = new Cockpit(this, "Textures/Cockpit.png", settings);
        cockpitObj.loadHud();
        if(DEBUG_SHOW_COCKPIT){
            cockpitObj.showCockpit();
        }
        
        //EXPERIMENTAL, does not work yet!
        //cockpitObj.loadBlades();
        
        //-------------------------------
        //Built in Physics Engine,
        //we use this for terrain collision detection.
        physicsState = new BulletAppState();
        stateManager.attach(physicsState);
        physicsState.getPhysicsSpace().add(terrainObj.getTerrainQuad());
        physicsState.getPhysicsSpace().add(terrainObj.getTerrainRigidBody());
        physicsState.getPhysicsSpace().add(camController.getCameraPhysicsBody());
        physicsState.getPhysicsSpace().enableDebug(assetManager);
        
        
        //-------------------------------
        //SOUNDS
        //-------------------------------
        //stop menu music
        //soundsManager.setPlaying(soundsManager.MENU_MUSIC_INDEX, false);
        soundsManager.setVolume(soundsManager.MENU_MUSIC_INDEX, soundsManager.MENU_MUSIC_DEFAULT_VOLUME/2.5f);
        
        //start helicopter blades sound
        soundsManager.setVolume(soundsManager.HELI_BLADES_INDEX, soundsManager.HELI_BLADES_DEFAULT_VOLUME );
        soundsManager.setPlaying(soundsManager.HELI_BLADES_INDEX, true);
        
        //------------------------------
        

        levelTimer.setTimeRemaining(dataLevel1General.LEVEL_1_GAME_TIME);
        cockpitObj.setTimerToShow(true);
        levelTimer.setTimerRunning(true);
        
        
        windFarmPlacementChecker.loadShapes((short)0);
        windFarmPlacementChecker.loadSmokeOnAllObjects();
        
        //can be called else where to improve load time.
        //townsManager.loadTownAssets();
        //displays all the towns
        //townsManager.showLevel1Towns();
        
        //----------------------------------------------------------------------
        townNode1 = new Node();
        rootNode.attachChild(townNode1);
        townNode2 = new Node();
        rootNode.attachChild(townNode2);
        townNode3 = new Node();
        rootNode.attachChild(townNode3);
        
        //MAKE THE TOWN OBJECTS------
        //town1
        ContainerTown townContainer = new ContainerTown(townNode1, this.assetManager, 
                "Models/town/main.j3o", 0.0f, new Vector3f(0,0,0), 4.0f, 
                "Sounds/townSound.wav", 1.0f, true);
        townContainer.initModel();
        townContainer.initSound();
        townNode1.setLocalTranslation(new Vector3f(-1857, -3, -2680));
        
        
        //town2
        ContainerTown townContainer2 = new ContainerTown(townNode2, this.assetManager, 
                "Models/town/main.j3o", 0.0f, new Vector3f(0,0,0), 4.0f, 
                "Sounds/townSound.wav", 1.0f, true);
        townContainer2.initModel();
        townContainer2.initSound();
        townNode2.setLocalTranslation(new Vector3f(-3979, -3, 3368));
        
        //town3
        ContainerTown townContainer3 = new ContainerTown(townNode3, this.assetManager, 
                "Models/town/main.j3o", 0.0f, new Vector3f(0,0,0), 4.0f, 
                "Sounds/townSound.wav", 1.0f, true);
        townContainer3.initModel();
        townContainer3.initSound();
        townNode3.setLocalTranslation(new Vector3f(2490, -3, 3071));
        
        //ALWAYS THE LAST THING!  
        //Why: SimpleUpdate can be called durring this method.
        gamePlaying = true;
        
    }//method
    
    
//------------------------------------------------------------------------------
    
     /**
     * Called from the Input Controller,
     * the user pressed the key to initiate
     * landing on a placement indicator.
     */
    public void fromIC_userWantsToLand(){
        //first level's landing sequence
        if(currentLevel == 1){
            //The user just won
            if(placementRaycastResults.getClosestCollision().getGeometry().getName().equalsIgnoreCase(dataLevel1General.LEVEL_1_WINNING_REASON)){
                routerForEndWinScene();
            }//if

            //wrong place to land!
            else{
                //inputCont.removeInputs();      
                cockpitObj.setHUDtextWithLockout("This isn't a good\nspot to build...\n" + placementRaycastResults.getClosestCollision().getGeometry().getName(), (short)10);
            }//else
        }//if
        
    }//method
    
    
    
    
    
    
}//class




/*
 * //THIS IS FOR TESTING MAIO's MODELS
        //-------------------------------------------------------------
        
        try{
            Spatial spatial = getAssetManager().loadModel("Models/cubetest3/cube UV.j3o");
            //spatial.setLocalScale(100);
            //Material mat_stl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            //Texture tex_ml = assetManager.loadTexture("Models/cubetest/texture/wall with window.jpg");
            //mat_stl.setTexture("ColorMap", tex_ml);
            //spatial.setMaterial(mat_stl);
            
            Node testNode = new Node();
            testNode.attachChild(spatial);
            testNode.setLocalTranslation(-2369, 27, -2648);
            rootNode.attachChild(testNode);
        }
        catch(Exception e){
            System.out.println("HEYHEY!");
        }
 */