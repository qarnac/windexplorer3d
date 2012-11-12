/*
 * BY: Ryan Moe
 * 
 * This class keeps game time for each level.  
 */
package mygame;

/**
 *
 * @author Ryan
 */
public class LevelTimer {
    
    //HUD Time Remaining Indicator

    private boolean boolTimerRunning;
    public short timeRemaining;
        private float millisecondsCount;
    private float currentLevelRunTime;
    private float levelTimeLength;//time the level should run.
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public LevelTimer(){
        boolTimerRunning = false;
        timeRemaining = 0;
            millisecondsCount = 0;
        currentLevelRunTime = 0;
        levelTimeLength = 0;
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    
    public void updateTimer(float tps){
        if(boolTimerRunning){
            millisecondsCount += tps;
            if(millisecondsCount >= 1){
                millisecondsCount = 0;
                --timeRemaining;
            }
        }//if
    }
    
    //--------------------------------------------------------------------------
    
    //just sets the time remaining var, doesn't
    //start the timer.
    public void setTimeRemaining(short in){
        if(in > 0)
            timeRemaining = in;
    }
    
    //Use this to modify time remaining while
    //timer is running.  
    //Param gets added to time remaining.
    public void modifyTimeRemaining(short in){
        timeRemaining += in;
    }
    
    //Start and Stop the timer.
    public void setTimerRunning(boolean in){
        millisecondsCount = 0;
        boolTimerRunning = in;
        
    }
    
    public void setTimerPaused(boolean in){
        boolTimerRunning = in;
    }
    
    //--------------------------------------------------------------------------
    
    public short getTimeRemaining(){
        return timeRemaining;
    }
    
    
    //--------------------------------------------------------------------------
    
    public boolean getTimerIsRunning(){
        return boolTimerRunning;
    }
    
    
}//class
