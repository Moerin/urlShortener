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
import javax.annotation.Resource;

import org.vostok.management.annotations.MBean;

/**
 *
 * @author Sebastien
 */
@MBean
public class Request {

    private Configuration config = new Configuration();

    public void configuration(String adress){
        config.setPath(adress);
    }
    public String find(String id){
        //Data data = new Data();
        
        Long convertId = Long.parseLong(id);
        
        //data.setId(convertId);
        
        DAO<Data> dataAccess = new GraphDAO(config);
        
        Data data = dataAccess.find(convertId);
        
        return data.getValue();
    }

    public String operation() {

        Data data = new Data();
        data.setId(4000);
        
        Configuration config = new Configuration();
        config.setPath("path"); // Path a entrer
        
        DAO<Data> dataAccess = new GraphDAO(config);
        
        dataAccess.start();
        
        Data dataJsp = dataAccess.find(data.getId());
        
        dataAccess.stop();
        
        //return test;
        return dataJsp.getValue();
    }

    @PostConstruct
    public void loading() {
        Logger.getLogger(Request.class
                .getName()).log(Level.INFO, "MBean {} is running... ", Request.class.getName());
    }
}
