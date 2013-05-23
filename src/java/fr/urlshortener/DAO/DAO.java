/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO;

import fr.urlshortener.DAO.pattern.ConnectInterface;
import fr.urlshortener.DAO.pattern.CrudInterface;
import fr.urlshortener.DAO.pattern.QueryInterface;

/**
 *
 * @author Sebastien
 */
public abstract class DAO<T> implements CrudInterface<T>, ConnectInterface, QueryInterface<T> {
     
}
