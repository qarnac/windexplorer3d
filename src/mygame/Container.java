/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;

/**
 *
 * @author cmb
 */
public class Container {
    
    public Container(){
        moduleList = new ArrayList<ComponentAbstract>();
    }
    
    //--------------------------------------------------
    
    public void init(){
        for(int i = 0; i < moduleList.size(); ++i){
            moduleList.get(i).init();
        }//for
        
    }//run
    
    //--------------------------------------------------
    
    public void run(){
        for(int i = 0; i < moduleList.size(); ++i){
            moduleList.get(i).run();
        }//for
        
    }//run
    
    //--------------------------------------------------
    
    public void addModule(ComponentAbstract input){
        moduleList.add(input);
        
    }
    
    //--------------------------------------------------
    
    
    
    private ArrayList<ComponentAbstract> moduleList;
    
}//class
