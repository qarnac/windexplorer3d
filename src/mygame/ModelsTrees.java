/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Ryan
 */
public class ModelsTrees {
    
    //--------------------------------------------------------------------------
    
    public Main mainHandle;
    
    //--------------------------------------------------------------------------
    
    public ModelsTrees(Main inHandle){
        mainHandle = inHandle;
    }
    
    /*
    public byte loadTrees(){
        Quad q = new Quad(2, 2);
        Geometry g = new Geometry("Quad", q);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("m_Color", ColorRGBA.Blue);
        g.setMaterial(mat);

        Quad q2 = new Quad(1, 1);
        Geometry g3 = new Geometry("Quad2", q2);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("m_Color", ColorRGBA.Yellow);
        g3.setMaterial(mat2);
        g3.setLocalTranslation(.5f, .5f, .01f);

        Box b = new Box(new Vector3f(0, 0, 3), .25f, .5f, .25f);
        Geometry g2 = new Geometry("Box", b);
        g2.setMaterial(mat);

        Node bb = new Node("billboard");

        BillboardControl control=new BillboardControl();
        
        bb.addControl(control);
        bb.attachChild(g);
        bb.attachChild(g3);       
        

        n=new Node("parent");
        n.attachChild(g2);
        n.attachChild(bb);
        rootNode.attachChild(n);

        n2=new Node("parentParent");
        n2.setLocalTranslation(Vector3f.UNIT_X.mult(5));
        n2.attachChild(n);

        rootNode.attachChild(n2);

        
        return 0;
    }
    */
    
    
    
    
    
    
}
