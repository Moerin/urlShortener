/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO.pattern;

import java.util.Set;

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
    public T querySingle(Object obj1, Object obj2); // TODO : developper cette methode pour qu'elle permette de gerer les requetes particulieres
    
    /**
     * 
     * @param obj1
     * @param obj2
     * @return set<Data>
     */
    public Set<T> querySet(Object obj1, Object obj2);
}
