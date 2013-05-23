/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.configuration;

/**
 *
 * @author Sebastien
 */
public class Configuration {
    
    private String path;

    public Configuration(String path) { // TODO : Ou passer par un objet Data?
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
}
