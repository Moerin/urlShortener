/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.service;

import org.vostok.management.annotations.MBean;

/**
 *
 * @author Sebastien
 */
@MBean
public class Service2 implements Service2MBean {
    
    public String ping(String str){
        return "Service2 does ping : " + str;
    }
}
