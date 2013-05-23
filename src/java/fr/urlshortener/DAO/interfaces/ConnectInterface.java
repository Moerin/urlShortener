/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO.interfaces;

/**
 *
 * @author Sebastien
 */
public interface ConnectInterface {
    
    public void start(); // gerer les exceptions
    
    public void stop(); // gestion de l'etat
    
    // boolean verifier si une instance existe deja
    //public void check();
    
//    public void restart();
    
}
