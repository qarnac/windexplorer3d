/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author nukesforbreakfast
 */
public class SmokeControl extends AbstractControl{
    
    AssetManager mainAssetManager;
    ParticleEmitter indicator;
    Node root;
    
    /*@Params
     * AssetManager: get the assetManager from main to allow loading of assets
     * rootNode: get the root node so we can attach children to it if need be.
     */
    public SmokeControl(AssetManager assetMan, Node rootNode)
    {
        mainAssetManager = assetMan;
        root = rootNode;
        
        //create a new smoke plume
        indicator = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material material = new Material(mainAssetManager, "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", mainAssetManager.loadTexture("Effects/smoke3.png"));
        indicator.setMaterial(material);
        indicator.setImagesX(1); 
        indicator.setImagesY(1); // 2x2 texture animation
        indicator.setStartColor(new ColorRGBA(0f, 0f, 1f, 1f));
        indicator.setEndColor(  new ColorRGBA(.5f, .5f, .7f, .5f));

        indicator.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 200, 0));
        indicator.setStartSize(30f);
        indicator.setEndSize(70f);
        indicator.setGravity(0, 0, 0);
        indicator.setLowLife(1f);
        indicator.setHighLife(3f);
        indicator.getParticleInfluencer().setVelocityVariation(0.3f);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //support moving the smoke with the spatial here
        indicator.setLocalTranslation(spatial.getLocalTranslation());
        indicator.setLocalRotation(spatial.getLocalRotation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't override this because its advanced stuff we do not need to use
        //yet
    }

    //needed to make sure we can save/load when that functionality is implemented
    public Control cloneForSpatial(Spatial spatial) {
        final SmokeControl clone = new SmokeControl(mainAssetManager, root);
        clone.setSpatial(spatial);
        clone.initSmoke();
        return clone;
    }
    
    //attach the smoke to make it viewable.
    public void initSmoke()
    {
        Node temp = spatial.getParent();
        if(temp == null)
        {
            root.attachChild(indicator);
        }
        else
        {
            temp.attachChild(indicator);
        }
    }
    
}