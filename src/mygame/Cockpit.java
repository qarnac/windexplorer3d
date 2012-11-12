/*
 * @author Ryan Moe
 * 
 * Loads a cockpit onto the screen.  This image does not move, it is stuck
 * in the middle of the screen.  Hope you have an alpha channel in the image...
 */

package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.util.ArrayList;

public class Cockpit {
    
    //--------------------------------------------------------------------------
    private Main mainHandle;
    private String assetPath;
    private AssetManager assetMan;
    private AppSettings settings;
    private Node guiNode;
    private Picture hudPic, bladesPic;
    

    
    
    
    //The number of chars per direction,
    //used for adding text to the HUD.  
    private final int HUT_MAX_CHAR_PER_LINE = 18;
    private final int HUD_MAX_LINE_COUNT = 8;
    
    //Timer Stuff
    private final String HUD_TIMER_INTRO = "Time Remaining: ";
    private boolean HUTTimerShowing;
    private short HUDTimeRemaining;
    
    private String HUDvariableText;
    
    private String HUDfinalDisplayingText;
    
    //for displaying text to hud and locking
    //out every new text entry.
    private short HUDtextLockoutTime;
    private float tpfSecondCounter;
    
    
    
    //--------------------------------------------------------------------------
    
    public final float COCKPIT_Y_OFFSET = .45f;
    public final float HUD_X_OFFSET = .370f;
    public final float HUD_Y_OFFSET = .46f;
    
    //--------------------------------------------------------------------------

    public Cockpit(Main mainHandle, String assetPathIn, AppSettings settingsIn){
        this.mainHandle = mainHandle;
        assetPath = assetPathIn;
        assetMan = mainHandle.getAssetManager();
        settings = settingsIn;
        guiNode = mainHandle.getGuiNode();
        
        //null until loaded
        hudPic = null;
        
        HUDvariableText = null;
        
        HUTTimerShowing = false;
        HUDTimeRemaining = 0;
        
        HUDtextLockoutTime = 0;
        tpfSecondCounter = 0;
        
        HUDfinalDisplayingText = "";
    }
    
    //--------------------------------------------------------------------------
    //Loads in an image from the path provided from the assetPath.
    public void loadHud(){
        //HUD
        hudPic = new Picture("HUD Picture");
        hudPic.setImage(assetMan, assetPath, true);
        hudPic.setWidth(settings.getWidth());
        hudPic.setHeight(settings.getHeight());
        hudPic.setPosition(0, -(settings.getHeight()*COCKPIT_Y_OFFSET) );
        //guiNode.attachChild(hudPic);
        
        //making a font
        BitmapFont myFont = mainHandle.getAssetManager().loadFont("Interface/Fonts/CourierNew.fnt");
        
        //making text for hud
        BitmapText hudText = new BitmapText(myFont, false);
        hudText.setName("HUDtext");
        hudText.setSize(myFont.getCharSet().getRenderedSize());                 // font size
        hudText.setColor(ColorRGBA.Black);                                       // font color
        hudText.setText("");       // the text
        hudText.setLocalTranslation( (settings.getWidth() * HUD_X_OFFSET),  
                                     (hudText.getLineHeight()+(settings.getHeight()*HUD_Y_OFFSET)),
                                      0);
        guiNode.attachChild(hudText);
    }
    
    public void showCockpit(){
        guiNode.attachChild(hudPic);   
    }
    
    //--------------------------------------------------------------------------
    
    //If you dont want to load() a different image, 
    //just restore the previous one.
    //@return 0 = things worked out,
    //        1 = you're an idiot, load it first.
    public byte reattachPreviousLoaded(){
        if(hudPic == null)
            return 1;
        
        guiNode.attachChild(hudPic);
        return 0;
    }//method
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    /**
     * Sets the text on the HUD. 
     * @param in
     * @return 0 = all good.  1 = lockout in effect.  2 = string is too long.
     */
    public byte setHUDtext(String in){
        //only do anything if there isn't a lockout.
        if(this.HUDtextLockoutTime == 0){
            HUDvariableText = in;
            int length = HUDvariableText.trim().length();
            short lineCounter = 1;

            StringBuffer SB = new StringBuffer(HUDvariableText);

            for(int i = HUT_MAX_CHAR_PER_LINE; i < length; i += HUT_MAX_CHAR_PER_LINE){
                //This string is too long to display.
                if(lineCounter++ > HUD_MAX_LINE_COUNT){
                    return 2;
                }//if
                SB.insert(i, "\n");   
                //to compensate for the added \n
                ++i;
            }//for


            //((BitmapText)guiNode.getChild("HUDtext")).setText(SB.toString());
            HUDvariableText = SB.toString();
            //buildAndShowText();
            return 0;
        }//if
        
        //lockout in effect
        else{
            return 1;
        }//else
        
    }//method
    
    
    
    
    //--------------------------------------------------------------------------
    //The text coming in does not get edited,
    //so make sure you have \n in the right places.
    /**
     * 
     * @param in the text to display
     * @return 0 = all ok.  1 = lockout in effect.
     */
    public byte setHUDtextUnedited(String in){
        if(this.HUDtextLockoutTime == 0){
            HUDvariableText = in;
            //buildAndShowText();
            return 0;
        }//if
        //lockout in effect
        else{
            return 1;
        }
        
        
    }//method
    
    //--------------------------------------------------------------------------
    /**
     * This will display text on the HUD with a lockout,
     * preventing anything else from being displayed until
     * the lockout has finished.
     * @param text The text you want to display.
     * @param timeIn The time to lockout everything else from displaying.
     */
    public void setHUDtextWithLockout(String text, short timeIn){
        HUDvariableText = text;
        HUDtextLockoutTime = timeIn;
        //buildAndShowText();
    }//method
  
    //--------------------------------------------------------------------------

    public void buildAndShowText(){
        if(HUDvariableText == null)
            HUDvariableText = "";
        
        if(HUTTimerShowing){
            HUDfinalDisplayingText = HUD_TIMER_INTRO + HUDTimeRemaining + "\n\n"
                               + HUDvariableText;
        }//if
        
        else{
            HUDfinalDisplayingText = HUDvariableText;
        }
        ((BitmapText)guiNode.getChild("HUDtext")).setText(HUDfinalDisplayingText);
    }
    
    //TIMER STUFF---------------------------------------------------------------
    
    //This has nothing to do with the timer's functioning,
    //it is just what number to display in the HUD.
    //Timer class manages the time, main sends the results
    //here to be displayed.
    public void setTimeRemaining(short in){
        HUDTimeRemaining = in;
    }
    
    
    public void setTimerToShow(boolean in){
        HUTTimerShowing = in;
    }
    
    
    
    //--------------------------------------------------------------------------
    
    public void update(float tpf){
        tpfSecondCounter += tpf;
        if(tpfSecondCounter >= 1){
            tpfSecondCounter = 0;
            if(HUDtextLockoutTime > 0)
                --HUDtextLockoutTime;
        }//if
            
    }//method
   

    
    //--------------------------------------------------------------------------
    //NOT BEING USED, looks like crapola right now.
    public void loadBlades(){
        //cyl = new Cylinder(32,32,10f,1f,true);
        bladesPic = new Picture("Blades Picture");
        bladesPic.setImage(assetMan, "/Textures/HelicopterBlades.png", true);
        bladesPic.setWidth( (settings.getWidth() * 1.0f) );
        bladesPic.setHeight( (settings.getHeight() * 1.0f) );
        bladesPic.rotateUpTo(new Vector3f(0.0f, 0.2f, -1.0f).normalize());
        //bladesPic.setPosition(0, -(settings.getHeight()*cockpitYOffset) );
        bladesPic.setPosition(0, 0);
        guiNode.attachChild(bladesPic);
    }
    
}//class



