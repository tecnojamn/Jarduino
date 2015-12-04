/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HibernateConf;

import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;

/**
 * @author Deepak Kumar 
 * * Web: http://www.roseindia.net
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
            try {
            	Configuration cfg = new Configuration().configure("/HibernateConf/hibernate.cfg.xml");        	
            	StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
            	sb.applySettings(cfg.getProperties());
            	StandardServiceRegistry standardServiceRegistry = sb.build();           	
            	sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);      	
            } catch (Throwable th) {
                    System.err.println("Enitial SessionFactory creation failed" + th.getMessage());
                    throw new ExceptionInInitializerError(th);
            }
    }
    public static SessionFactory getSessionFactory() {
            return sessionFactory;
    }
}
