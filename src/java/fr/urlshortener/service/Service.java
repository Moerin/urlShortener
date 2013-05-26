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
public class Service {

    @Resource
    Service2 bean;
    // Test entre deux web application jmxTest
    public String test;

    public String getTest(String test) {
        return this.test + test;
    }

    public String operation() {

        Data data = new Data();
        data.setId(4000);
        
        Configuration config = new Configuration();
        config.setPath("thing"); // Path a entrer
        
        DAO<Data> dataAccess = new GraphDAO(config);
        dataAccess.start();
        Data dataJsp = dataAccess.find(data.getId());
        
        return "test";
        //return dataJsp.getValue();
    }

    @PostConstruct
    public void loading() {
        Logger.getLogger(Service.class
                .getName()).log(Level.INFO, "MBean Service is running... ");
    }
}
