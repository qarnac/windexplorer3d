/**
 *
 * @author Ryan
 * 
 * Shows a brief intro before each level.
 * Also used to show the game over screen.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;


public class SceneManager {
    
    //--------------------------------------------------------------------------
    private Main mainHandle;
    private Node guiNode;
    private AssetManager assetMan;
    private AppSettings settings;
    
    private boolean introPlaying;
    private float currentPageRunTime;
    private float currentPageViewTime;
    
    private short currentSceneIndex;
    private DataScene currentSceneDataObj;
        private short currentPageIndex;
    
    private Picture pic;
    
    public final short LEVEL_1_INTRO_INDEX = 0;
    public final short LEVEL_1_ENDTRO_LOSE_INDEX = 1;
    public final short LEVEL_1_WIN_INDEX = 2;
    
    //--------------------------------------------------------------------------
    //Just inits some stuff
    public SceneManager(Main mainHandle, AppSettings settingsIn){
        this.mainHandle = mainHandle;
        guiNode = mainHandle.getGuiNode();
        assetMan = mainHandle.getAssetManager();
        settings = settingsIn;
        introPlaying = false;
        currentPageRunTime = 0;
        currentPageViewTime = 0;
        pic = new Picture("IntroPage");
        currentPageIndex = 0;
    }
    
    //--------------------------------------------------------------------------
    //Supply the index to the scene you want to play.
    //This will get all the vars needed from the config file 
    //and, geebus willing, will display the scene.
    public void startScene(short levelIndex){
        currentSceneIndex = levelIndex;
        currentSceneDataObj = mainHandle.confReader.getIntroSceneData(currentSceneIndex);
        //gets incremented to 0 right away...
        currentPageIndex = -1;
        showNextPage();
        //must be last to prevent update() from being
        //called before showNextPage is finished.
        introPlaying = true;
    }
    
    //--------------------------------------------------------------------------
    //called from main update loop.
    public void update(float tpf){
        if(introPlaying){
            currentPageRunTime += tpf;
            //see if we need to go to next page.
            if(currentPageRunTime > currentPageViewTime){
                showNextPage();
            }//if
        }//if
    }//method
    
    
    
    public boolean getIsRunning(){
        return introPlaying;
    }
    
    
    //--------------------------------------------------------------------------
    //Private methods-----------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private void showNextPage(){
        currentPageRunTime = 0;
        //If there are more pages left to show.
        if(++currentPageIndex < currentSceneDataObj._pageNames.length){
            currentPageViewTime = currentSceneDataObj._pageViewTimes[currentPageIndex];
            //pic = new Picture("IntroPage");
            pic.setImage(assetMan, currentSceneDataObj._pageNames[currentPageIndex], true);
            pic.setWidth(settings.getWidth());
            pic.setHeight(settings.getHeight());
            pic.setPosition(0, 0);
            guiNode.attachChild(pic);
        }//if
        
        //Scene is done playing, start game!
        else{
            introPlaying = false;
            guiNode.detachAllChildren();
            mainHandle.sceneReportingInFinished(currentSceneIndex);
        }//else
    }//method
    
    
    
    
}//class
