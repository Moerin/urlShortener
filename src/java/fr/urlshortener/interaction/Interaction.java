/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.interaction;

/**
 *
 * @author Sebastien
 */
public interface Interaction {
    
    // Methode qui recupere la valeur par l'id
    public Object findById();
    
    // Methode qui recupere l'id par la valeur
    public Object findByValue();
    
    // Methode qui supprime une donnée
    public void delete();
}
