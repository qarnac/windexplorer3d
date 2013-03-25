/**
 * @author Ryan Moe
 * 
 * This reads in a config file and stores the paths if found.
 * Eventually this will be converted to some sort of xml or tag parsing thing.
 */

package mygame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfigReader {
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private String configFolderPath;
    
    //Reading Vars
    //private InputStream inputStream;
    /*
     * moving to level1.java
     */
    private BufferedReader bufferedIn;
    private String line;
    
    //OUTPUT VARS
    //--------------------------
    
    //Config File Names/paths---
    private String cameraConfigFileName;
    private String introScreenFileName;
    private String PlacementRejectionFileName;
    private String treeModelsFileName;
    
    //Terrain Vars--------------
    
    //Land and Sky
    private String skyboxPath;
    private String terrainAlphaPath;
    private String terrainHeightMapPath;
    private String terrainGrassTexPath;
    private String terrainDirtTexPath;
    private String terrainRoadTexPath;
    
    //Camera Vars---------------
    /*
     * moving to level1.java
     * private short numberOfProfiles;
     * private DataCameraPhysics[] _CamVars;
     */
    
    //intro screen vars---------
    private int numberOfScenes;
    private DataScene[] _introScene;
    
    //Placement Rejection Vars--
    private DataPlacementLevelShapes[] _placementsForEachLevel;
    
    //Tree stuff
    private DataTreeImageInfo _treeImageInfo[];
    private DataTreeBillboardLevel _treesPerLevel[];
    
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //Path to the config reader
    public ConfigReader(String filePathIn){
        configFolderPath = filePathIn;
        cameraConfigFileName = null;
        introScreenFileName = null;
    }
    
    public ConfigReader(){
        cameraConfigFileName = null;
        introScreenFileName = null;
    }
    
    //Path to the config reader
    //NOTE: just the path to the file, dont
    //incluse the file name itself.
    public void setFile(String filePathIn){
        configFolderPath = filePathIn;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
     * @return 0 = good, 1 = IOException, 
     * 
     */
    public byte loadMasterFile(){
        try{
            //init
            //inputStream =  new FileInputStream(masterFilePath + "MasterConfig.txt");
            //inputStream =  new FileInputStream(this.getClass().getResourceAsStream("s") );
            //bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configFolderPath + "MasterConfig.txt")));
             
            /* example usage:
            * line = bufferedIn.readLine();  or   getNextValidLine();
            * StringTokenizer t = new StringTokenizer(line);
            * String currentTolken = t.nextToken();
            */
            //config file not found
            getNextValidLine();
            if(line == null)
                return 1;
            
            //Camera Config File Name
            cameraConfigFileName = line;
            
            //Intro Screen File Name
            getNextValidLine();
            introScreenFileName = line;
            
            //Placement Rejection shapes file
            getNextValidLine();
            PlacementRejectionFileName = line;
            
            //Trees config file
            getNextValidLine();
            treeModelsFileName = line;
            
            //Skybox
            getNextValidLine();
            skyboxPath = line;
            
            //splatter textures
            getNextValidLine();
            terrainAlphaPath = line;
            
            //height map
            getNextValidLine();
            terrainHeightMapPath = line;
            
            //grass texture
            getNextValidLine();
            terrainGrassTexPath = line;
            
            //dirt texture
            getNextValidLine();
            terrainDirtTexPath = line;
            
            //road texture
            getNextValidLine();
            terrainRoadTexPath = line;

            
            
            bufferedIn.close();
            //inputStream.close();
        }//try
        
        catch (IOException e) {
            return 1;
	}//catch
        
        return 0;
    }   
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
     * moving into level1.java
     *
    public byte loadCameraFile(){
        //config file not found
        if (cameraConfigFileName == null){
            return 1;
        }//if    
            //inputStream =  new FileInputStream(configFolderPath + cameraConfigName);
            //bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configFolderPath + cameraConfigFileName)));
            try {
                getNextValidLine();
                numberOfProfiles = Short.valueOf(line);
                _CamVars = new DataCameraPhysics[numberOfProfiles];
                System.out.println("Camera Config Reader: numberOfProfiles: " + numberOfProfiles);
                for(short i = 0; i < numberOfProfiles; ++i ){
                    _CamVars[i] = new DataCameraPhysics();
                    
                    getNextValidLine();
                    _CamVars[i].maxVelocity = Float.valueOf(line); 
                    getNextValidLine();
                    _CamVars[i].impulseXZ = Integer.valueOf(line);                   
                    getNextValidLine();
                    _CamVars[i].impulseY = Integer.valueOf(line);                 
                    getNextValidLine();
                    _CamVars[i].frictionX = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionY = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionZ = Float.valueOf(line);               
                    getNextValidLine();
                    _CamVars[i].frictionYRotation = Float.valueOf(line);      
                    getNextValidLine();
                    _CamVars[i].rotationYImpulse = Float.valueOf(line);    
                    getNextValidLine();
                    _CamVars[i].rotationYVelocityMax = Float.valueOf(line);    
                    getNextValidLine();
                    _CamVars[i].mass = Integer.valueOf(line);
                }//for
                
            } //try
            catch (IOException ex) {
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        
        return 0;
    }
    */
    
    
    //--------------------------------------------------------------------------
    
    public byte loadIntroScreenConfig(){
        //config file not found
        if (introScreenFileName == null){
            return 1;
        }//if    
            //inputStream =  new FileInputStream(configFolderPath + introScreenName);
            //bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configFolderPath + introScreenFileName)));
            try {
                getNextValidLine();
                //This is how many levels have intro scenes.
                numberOfScenes = Integer.parseInt(line);
                _introScene = new DataScene[numberOfScenes];
                //For each level scene, fill out it's page names.
                for(int i = 0; i < numberOfScenes; ++i){
                    getNextValidLine();
                    short numberOfPages = Short.parseShort(line);
                    _introScene[i] = new DataScene();
                    _introScene[i]._pageNames = new String[numberOfPages];
                    _introScene[i]._pageViewTimes = new float[numberOfPages];
                    //Fill out each page's path
                    int ii;
                    for(ii = 0; ii < numberOfPages; ++ii){
                        getNextValidLine();
                        _introScene[i]._pageNames[ii] = line;
                    }//for
                    //Fill out each page's view time
                    for(ii = 0; ii < numberOfPages; ++ii){
                        getNextValidLine();
                        _introScene[i]._pageViewTimes[ii] = Float.parseFloat(line);
                    }//for
                }//for  
            } //try
            catch (IOException ex) {
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        return 0;
    }
    
    
    //--------------------------------------------------------------------------
    
    public byte loadPlaceRejectConfig(){
        //config file not found
        if (PlacementRejectionFileName == null){
            return 1;
        }//if    
            //inputStream =  new FileInputStream(configFolderPath + PlacementRejectionName);
            //bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configFolderPath + PlacementRejectionFileName)));
            
            try {
                getNextValidLine();
                //number of levels accounted for
                short levelCount = Short.parseShort(line);
                _placementsForEachLevel = new DataPlacementLevelShapes[levelCount];
                
                //Fill out the objects for each Level.
                for(short i = 0; i < levelCount; ++i){
                    short ii;
                    _placementsForEachLevel[i] = new DataPlacementLevelShapes();
                    
                    getNextValidLine();
                    short levelsRejReasonCount = Short.parseShort(line);
                    _placementsForEachLevel[i]._reasons = new String[levelsRejReasonCount];
                    
                    //Fill out the reasons you cant build there array.
                    for(ii = 0; ii < levelsRejReasonCount; ++ii){
                        getNextValidLine();
                        _placementsForEachLevel[i]._reasons[ii] = line;
                    }//fir
                    
                    getNextValidLine();
                    short shapeCount = Short.parseShort(line);
                    _placementsForEachLevel[i]._shapesInThisLevel = new DataPlacementSingleShape[shapeCount];
                    
                    //----------------------------------------------------------
                    //Fill out the shapes for this level
                    for(ii = 0; ii < shapeCount; ++ii){
                        _placementsForEachLevel[i]._shapesInThisLevel[ii] = new DataPlacementSingleShape();
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].shapeType = Short.parseShort(line);
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].reasonIndex = Short.parseShort(line);
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].indicatorHeight = Float.parseFloat(line);
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].centerX = Float.parseFloat(line);
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].centerZ = Float.parseFloat(line);
                        
                        getNextValidLine();
                        _placementsForEachLevel[i]._shapesInThisLevel[ii].height = Float.parseFloat(line);
                        
                        //shape is a rectangle
                        if(_placementsForEachLevel[i]._shapesInThisLevel[ii].shapeType == 0){
                            getNextValidLine();
                            _placementsForEachLevel[i]._shapesInThisLevel[ii].lengthX = Float.parseFloat(line);
                            getNextValidLine();
                            _placementsForEachLevel[i]._shapesInThisLevel[ii].lengthZ = Float.parseFloat(line);
                            getNextValidLine();
                            _placementsForEachLevel[i]._shapesInThisLevel[ii].rotation = Float.parseFloat(line);
                        }//if
                        //shape is a circle
                        else if(_placementsForEachLevel[i]._shapesInThisLevel[ii].shapeType == 1){
                            getNextValidLine();
                            _placementsForEachLevel[i]._shapesInThisLevel[ii].diameter = Float.parseFloat(line);
                        }//eif
                        
                    }//for
                    
                    //----------------------------------------------------------
                    
                }//for
                
            } //try
            catch (IOException ex) {
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        
        
        return 0;
    }
    
    
    
    //--------------------------------------------------------------------------
    
    
    public byte loadTreeModelInfo(){
        //config file not found
        if (treeModelsFileName == null){
            return 1;
        }//if    
            bufferedIn = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(configFolderPath + treeModelsFileName)));
            
            try {
                getNextValidLine();
                //number of tree images
                short count = Short.parseShort(line);
                _treeImageInfo = new DataTreeImageInfo[count];
                
                //loop through the number of tree images
                for(short i = 0; i < count; ++i){
                    getNextValidLine();
                    _treeImageInfo[i].pathToTreeModel = line;
                    getNextValidLine();
                    _treeImageInfo[i].defaultSizeX = Short.parseShort(line);
                    getNextValidLine();
                    _treeImageInfo[i].defaultSizeY = Short.parseShort(line);
                }//for number of tree images
                
                getNextValidLine();
                //number levels
                count = Short.parseShort(line);
                _treesPerLevel = new DataTreeBillboardLevel[count];
                
                //loop through the number of levels
                for(short i = 0; i < count; ++i){
                    
                }//for
                
                
            } //try
            catch (IOException ex) {
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
                return 1;
            }
        
        return 0;
    }
    
    
    

    //--------------------------------------------------------------------------
    //GETTERS AND SETTERS-------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public DataTerrain getTerrainData(){
        DataTerrain returnMe = new DataTerrain();
            returnMe.alphaPath = terrainAlphaPath;
            returnMe.dirtTexPath = terrainDirtTexPath;
            returnMe.grassTexPath = terrainGrassTexPath;
            returnMe.heightMapPath = terrainHeightMapPath;
            returnMe.roadTexPath = terrainRoadTexPath;
            returnMe.skyboxPath = skyboxPath;
        return returnMe;
    }
    
    public String getCameraConfigName(){
        return cameraConfigFileName;
    }
    
    public String getPlaceRejecConfigName(){
        return PlacementRejectionFileName;
    }
    
    /*
     * moving to level1.java
     *
    public DataCameraPhysics getCameraVars(short index){
        if(index >= numberOfProfiles){
            return null;
        }
        return _CamVars[index];
    }
    */
    
    public DataScene getIntroSceneData(short index){
        return _introScene[index];
    }
    
    public DataPlacementLevelShapes getPlacementLevelData(short index){
        return _placementsForEachLevel[index];
    }
    

    
    
    
    //--------------------------------------------------------------------------
    //INTERNAL TOOLS------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
     * Moving to level1.java
     */
    private void getNextValidLine() throws IOException{
        line = bufferedIn.readLine();
        
        //reject empty lines, spaces or comments.
        while(line.equalsIgnoreCase("") || line.startsWith("//") || line.equalsIgnoreCase(" ") || line == null){
            line = bufferedIn.readLine().trim();
        }//while
        
        //System.out.println("LINE: " + line);
        while(line.endsWith("++")){
            line = line.replace("++", "");
            line += "\n" + bufferedIn.readLine();
        }
        
    }//method
    
    
}//class
