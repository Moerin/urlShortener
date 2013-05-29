/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.service;

import fr.urlshortener.DAO.DAO;
import fr.urlshortener.DAO.implement.GraphDAO;
import fr.urlshortener.bean.Data;
import fr.urlshortener.configuration.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.vostok.management.annotations.MBean;

/**
 *
 * @author Sebastien
 */
@MBean
public class Request {
    
    private Data data;
    
    private Configuration config = new Configuration();
    
    DAO<Data> dataAccess; // TODO : trouver ou le mettre car ce n'est pas lui qui doit s'en occuper

    public void configuration(String adress) { // TODO : Autre service a s'en occuper
        config.setPath(adress);
    }

    public Data find(String id) {
        Long convertId = Long.parseLong(id);
        data = dataAccess.find(convertId);
        return data;
    }

    public String query(String name, String value) {
        data = dataAccess.querySingle(name, value);
        return data.getValue();
    }
    
    public void create(Data data){
        dataAccess.create(data);
    }

    @PostConstruct
    public void loading() {
        Logger.getLogger(Request.class
                .getName()).log(Level.INFO, "MBean {} is running... ", Request.class.getName());
        config.setPath("path"); // TODO : Autre service a s'en occuper
        dataAccess = new GraphDAO(config);
        dataAccess.start();
    }

    @PreDestroy
    public void unloading() {
        dataAccess.stop();
    }
}
