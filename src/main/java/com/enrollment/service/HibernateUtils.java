package com.enrollment.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            
            System.out.println("\n========================================");
            System.out.println("✓ SessionFactory initialized successfully");
            System.out.println("========================================\n");
        } catch (Exception e) {
            System.out.println("✗ Failed to create SessionFactory:");
            e.printStackTrace();
            throw new RuntimeException("SessionFactory initialization failed", e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("✓ SessionFactory closed");
        }
    }
}