/*
 * @author Ryan Moe
 * 
 * This class controlls the main menu user inputs,
 * redirecting user input back to the main class.
 * Gets registered by the MainMenu.java class as
 * its controller.
 */

package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController extends AbstractAppState implements ScreenController {

    //--------------------------------------------------------------------------
    
    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    private Main callingHandle;
    
    //--------------------------------------------------------------------------
    
    //Param: a handle to the parent, so inputs can be relayed back.
    public MainMenuController(Main callingClass){
        callingHandle = (Main)callingClass;
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {}

    public void onEndScreen() {}
    
    //--------------------------------------------------------------------------
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
    }
    
    @Override
    public void update(float tpf) { 
        //jme update loop.
    }
    
    //-------------------------------------------------------------------------- 
    
    //called from the gui
    /*
     * Causey comments:
     * called when the Start button gets clicked on by the user within the 
     * start menu GUI.
     */
    public void startGame() {
        callingHandle.soundsManager.setPlaying(callingHandle.soundsManager.BUTTON_PRESS_INDEX, true);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        callingHandle.mainMenuStartGame(); //Causey: call this function from main
    }
    
    //called from the gui
    /*
     * Causey comments:
     * called when the quit button gets clicked on by the user within the 
     * start menu GUI.
     */
    public void quit(){
        callingHandle.soundsManager.setPlaying(callingHandle.soundsManager.BUTTON_PRESS_INDEX, true);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        callingHandle.mainMenuStopProgram(); //Causey: call this function from main
    }
    
    
}
