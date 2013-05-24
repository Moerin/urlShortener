/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastien
 */
public class Configuration {
    
    private String path;
    private String URI;
    
    private boolean fileExist = false;
    
    Logger logger = LoggerFactory.getLogger(Configuration.class);
    
    /**
     * Constructeur utilise pour creer une configuration local
     * @param path
     * @param index 
     */
    public Configuration() {
//        File f = new File(path);
//        if(f.exists()){
//            fileExist = true;
//        }else{
//            logger.error("Database file not found");
//            System.err.println("Database file not found");
//        }
    }
    
    public String getURI() {
        return URI;
    }

    /**
     * Pour chemin d'acces type URI
     * @return 
     */
    public void setURI(String URI) {
        this.URI = URI;
    }
    
    
    public String getPath() {
        return path;
    }
    
    /**
     * Pour chemin d'acces local
     * @return 
     */
    public void setPath(String path) {
        this.path = path;
    }
    
//    public boolean isFileExist() {
//        return fileExist;
//    }
}
