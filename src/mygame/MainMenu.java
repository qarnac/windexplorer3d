package mygame;


import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;

/**
 *
 * @author Ryan
 * ************
 * Causey Comments:
 * MainMenu is essentially a wrapper class for the Nifty GUI class used here
 * to build the main menu for the game.
 */

public class MainMenu {
    
    private AssetManager assetManager;
    private AudioRenderer audioRenderer;
    private InputManager inputManager;
    private ViewPort guiViewPort;
    
    //GUI builder
    private Nifty nifty;
    
    private Main mainHandle;
    
    //--------------------------------------------------------------------------
    
    public MainMenu(Main handle){
        mainHandle = handle;
    }
    
    //--------------------------------------------------------------------------
        
    //Starts up and presents the main menu.
    //Param: Display these error messages to the user.
    public void startMainMenu(final String errors){
        //stop game from running if it is.
        //if(levelIsLoaded == false){
            //rootNode.detachAllChildren();
        //}
        //levelIsLoaded = false;
        
        assetManager = mainHandle.getAssetManager();
        inputManager = mainHandle.getInputManager();
        audioRenderer = mainHandle.getAudioRenderer();
        guiViewPort = mainHandle.getGuiViewPort();
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        //flyCam.setDragToRotate(true);
        
        //Default styles for now
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        //LAYOUT OUT THE GUI-----------------------
        // <screen>
        //final Main thisHandle = this;
        //This adds a screen and a controller to nifty,
        //The controller is in this package.
        /**********************************************
         * Causey Comments:
         * MainMenuController is instantiated in the addScreen function and is
         * passed as a parameter. This makes MainMenuController the controller
         * class for this nifty instance, ergo the controller for this MainMenu
         * instance. 
         */
        nifty.addScreen("screen_mainMenu", new ScreenBuilder("screen"){{
                        controller(new mygame.MainMenuController(mainHandle));

            // <layer>
            layer(new LayerBuilder("layer") {{
                childLayoutVertical(); // layer properties, add more...
                
                this.backgroundImage("Textures/mainMenu.png");

                // <panel>
                panel(new PanelBuilder("panel") {{
                    this.childLayoutVertical();              

                    // GUI elements--------------------
                   
                    
                    
                    // add text
                    text(new TextBuilder() {{
                        alignCenter();
                        valignCenter();
                        text("Wind Explorer in 3D");
                        font("Interface/Fonts/Default.fnt");
                        height("15%");
                        width("15%");
                    }});
                    
                    //Start Button
                    control(new ButtonBuilder("start button", "Start the Demo!"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                        visibleToMouse(true);
                        //Causey: startGame() is a function defined in the controller
                        interactOnClick("startGame()");
                    }});
                    
                    //Quit Button
                    control(new ButtonBuilder("quit button", "Quit"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                        visibleToMouse(true);
                        //Causey: quit() is a function defined in the controller
                        interactOnClick("quit()");
                    }});
                    
                    //Adding optional Error Text
                    if(errors != null){
                        alignCenter();
                        valignCenter();
                        text(new TextBuilder() {{
                        text(errors);
                        font("Interface/Fonts/Default.fnt");
                        height("5%");
                        width("15%");
                        }});
                    }

                    //.. add more GUI elements here              

                }});
                // </panel>
              }});
            // </layer>
          }}.build(nifty));
        // </screen>

        nifty.gotoScreen("screen_mainMenu"); // start the screen 
        
        //PLAY MUSIC--------------------------
        mainHandle.soundsManager.setPlaying(mainHandle.soundsManager.MENU_MUSIC_INDEX,true);
        //restore menu volume
        mainHandle.soundsManager.setVolume(mainHandle.soundsManager.MENU_MUSIC_INDEX, mainHandle.soundsManager.MENU_MUSIC_DEFAULT_VOLUME);
    }//method

    
        //--------------------------------------------------------------------------

    
    public void stopGUI(){
        if(nifty != null)
            nifty.exit();
    }
    
    
}//class
