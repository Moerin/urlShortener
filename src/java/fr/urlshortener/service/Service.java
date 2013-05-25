/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.service;

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
    
    @PostConstruct
    public void loading(){
        Logger.getLogger(Service.class.getName()).log(Level.INFO, "MBean Service is running... ");
    }
    
}
