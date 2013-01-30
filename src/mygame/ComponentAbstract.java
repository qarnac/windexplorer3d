/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author cmb
 */
abstract class ComponentAbstract {
    
    abstract public String getComponentID();
    
    abstract public void init();
    abstract public void run();
    
    public Container parent;
    
    public void addParent(Container in){
        parent = in;
    };
    
}
