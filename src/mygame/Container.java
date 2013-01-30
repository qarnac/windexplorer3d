/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author cmb
 */
public class Container {
    
    private AssetManager assetManager;
    private Node entitiesNode;
    private ArrayList<ComponentAbstract> moduleList;
    private boolean readyToRun = false;
    
    //-------------------------------------------------
    
    public Container(AssetManager assetManIn){
        readyToRun = false;
        assetManager = assetManIn;
        moduleList = new ArrayList<ComponentAbstract>();
        entitiesNode = new Node();
    }
    
    //--------------------------------------------------
    
    public Node init(){
        for(int i = 0; i < moduleList.size(); ++i){
            moduleList.get(i).init();
        }//for
        readyToRun = true;
        return entitiesNode;
    }//run
    
    //--------------------------------------------------
    
    public void run(){
        if(readyToRun){
            for(int i = 0; i < moduleList.size(); ++i){
                moduleList.get(i).run();
            }//for
        }
    }//run
    
    //--------------------------------------------------
    
    public void addModule(ComponentAbstract input){
        input.addParent(this);
        moduleList.add(input);
        
    }
    
    //--------------------------------------------------
    
    public AssetManager getAssetMan(){
        return assetManager;
    }
    
    public Node getParentNode(){
        return entitiesNode;
    }
    
    public ComponentAbstract getComponentByID(String ID){
        for(int i = 0; i < moduleList.size(); ++i){
            //if the param ID is equal with the component ID.
            if(ID.equalsIgnoreCase(moduleList.get(i).getComponentID())){
                return moduleList.get(i);
            }
        }//for
        System.err.println("Container, getComponentByID: failed to find component.");
        return null;
    }
    
    
}//class
