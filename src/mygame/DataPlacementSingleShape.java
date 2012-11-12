/**
 *
 * @author Ryan Moe
 * 
 * This is a data holding class for the placement of objects
 * that defind an area as non-buildable for some reason.
 * For example: you cant build a wind farm here, it costs too much!
 */
package mygame;


public class DataPlacementSingleShape {
    public short shapeType;
    //there is an object that indicates where
    //this shape is to the user, like smoke,
    //and this is that object's height.
    public float indicatorHeight;
    public float centerX, centerZ;
    public float height;
    public float diameter;
    public float lengthX, lengthZ;
    public float rotation;

    //This shape represents a reason you cant build here,
    //this is the index to that reason's string explination.
    public short reasonIndex;
    
    
}
