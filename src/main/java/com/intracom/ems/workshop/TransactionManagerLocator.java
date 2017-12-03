package com.intracom.ems.workshop;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

public class TransactionManagerLocator {
    private static class Singleton {
        private static final TransactionManager INSTANCE = locate();
        
        private static TransactionManager locate() {
            InitialContext ctx = null;
            try {
                ctx = new InitialContext();
                return (TransactionManager) ctx.lookup("java:jboss/TransactionManager");
            }
            catch(NamingException exc) {
                throw new RuntimeException("Failed to locate TransactionManager", exc);           
            }
            finally {
                if (ctx != null) {
                    try {
                        ctx.close();
                    }
                    catch (NamingException exc) {
                    }
                }
            }
        }
    }

    public static TransactionManager get() {
        return Singleton.INSTANCE;
    }
}
