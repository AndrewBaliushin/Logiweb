package com.tsystems.javaschool.logiweb.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class provides access to {@link EntityManagerFactory} for persistent unit 'logiweb'.
 * EntityManagerFactory is treated as singleton and lazy initialized.
 * 
 * @author Andrey Baliushin
 */
public final class LogiwebJpaHelper {
    
    private static final String PERSISTENCE_UNIT = "logiweb";
    
    private static EntityManagerFactory emf;
    
    private LogiwebJpaHelper() { }
    
    private static synchronized void createEmfInstance() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
    }
    
    /**
     * Get singleton instance of EntityManagerFactory for 'logiweb' persistence-unit.
     * 
     * @return unique instance of EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null) {
            createEmfInstance();
        }
        return emf;
    }    
}
