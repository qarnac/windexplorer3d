/*
 * @author Ryan Moe
 * 
 * This is a data holding class for the placement of objects
 * that defind an area as non-buildable for some reason.
 * For example: you cant build a wind farm here, it costs too much!
 */
package mygame;

import com.jme3.scene.Node;

public class DataPlacementLevelShapes {
    
    //These shapes represents a reason you cant build here,
    //these are all the different reasons.
    public String[] _reasons;
    //all the shapes that exist in this level.
    public DataPlacementSingleShape _shapesInThisLevel[];
    
    
}
