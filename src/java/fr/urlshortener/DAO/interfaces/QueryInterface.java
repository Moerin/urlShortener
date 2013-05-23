/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO.interfaces;

import java.util.List;

/**
 *
 * @author Sebastien
 */
public interface QueryInterface<T> {
    
    /**
     * 
     * @param obj
     * @param obj
     * @return data
     */
    public T querySingle(String obj1, String obj2); // TODO : developper cette methode pour qu'elle permette de gerer les requetes particulieres
    
    /**
     * 
     * @param obj1
     * @param obj2
     * @return set<Data>
     */
    public List<T> querySet(String obj1, String obj2);
}
