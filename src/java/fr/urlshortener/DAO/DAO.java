/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO;

import fr.urlshortener.DAO.interfaces.ConnectInterface;
import fr.urlshortener.DAO.interfaces.CrudInterface;
import fr.urlshortener.DAO.interfaces.QueryInterface;

/**
 *
 * @author Sebastien
 */
public abstract class DAO<T> implements CrudInterface<T>, ConnectInterface, QueryInterface<T> {
     
}
