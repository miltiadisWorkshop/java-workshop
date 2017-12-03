package com.intracom.ems.workshop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

public class AppContextListener implements ServletContextListener {
    @Resource(lookup="java:jboss/datasources/ExampleDS")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WorkshopToolkit.inst().setDataSource(dataSource);
        try (Connection con = dataSource.getConnection()) {
            TransactionManager txMgr = TransactionManagerLocator.get();
            boolean txEnded = false;
            txMgr.begin();
            try (Statement stmt = con.createStatement()){
                stmt.execute("CREATE TABLE IF NOT EXISTS element("
                        + "ip VARCHAR(15) NOT NULL, "
                        + "lon NUMERIC(11, 8) NOT NULL, "
                        + "lat NUMERIC(10, 8) NOT NULL, "
                        + "CONSTRAINT element_pk PRIMARY KEY(ip));");
                stmt.execute("CREATE TABLE IF NOT EXISTS port("
                        + "ip VARCHAR(15) NOT NULL, "
                        + "port VARCHAR(255) NOT NULL, "
                        + "CONSTRAINT port_pk PRIMARY KEY(ip, port));");
                stmt.execute("CREATE TABLE IF NOT EXISTS link("
                        + "ipa VARCHAR(15) NOT NULL, "
                        + "porta VARCHAR(255) NOT NULL, "
                        + "ipb VARCHAR(15) NOT NULL, "
                        + "portb VARCHAR(255) NOT NULL)");
                boolean doInserts;
                try (ResultSet res = stmt.executeQuery(
                        "SELECT count(*) FROM element")) {
                    res.next();
                    doInserts = res.getInt(1) == 0;
                }
                if(doInserts) {
                    stmt.execute("INSERT INTO element(ip, lon, lat) "
                            + "VALUES('172.28.1.1', 23.758503, 37.970490)");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.1.1', 'ODU 1')");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.1.1', 'ODU 2')");
                    stmt.execute("INSERT INTO element(ip, lon, lat) "
                            + " VALUES('172.28.10.1', 23.769251, 37.980079)");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.10.1', 'ODU 1')");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.10.1', 'ODU 2')");
                    stmt.execute("INSERT INTO link(ipa, porta, ipb, portb) "
                            + "VALUES('172.28.1.1', 'ODU 2', "
                            + "'172.28.10.1', 'ODU 1')");
                    stmt.execute("INSERT INTO element(ip, lon, lat) "
                            + " VALUES('172.28.12.1', 23.750374, 37.988349)");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.12.1', 'ODU 1')");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.12.1', 'ODU 2')");
                    stmt.execute("INSERT INTO link(ipa, porta, ipb, portb) "
                            + "VALUES('172.28.10.1', 'ODU 2', "
                            + "'172.28.12.1', 'ODU 1')");
                    stmt.execute("INSERT INTO element(ip, lon, lat) "
                            + " VALUES('172.28.14.1', 23.741199, 37.986328)");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.14.1', 'ODU 1')");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.14.1', 'ODU 2')");
                    stmt.execute("INSERT INTO link(ipa, porta, ipb, portb) "
                            + "VALUES('172.28.12.1', 'ODU 2', "
                            + "'172.28.14.1', 'ODU 1')");
                    stmt.execute("INSERT INTO element(ip, lon, lat) "
                            + " VALUES('172.28.20.1', 23.735775, 37.979307)");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.20.1', 'ODU 1')");
                    stmt.execute("INSERT INTO port(ip, port) "
                            + "VALUES('172.28.20.1', 'ODU 2')");
                    stmt.execute("INSERT INTO link(ipa, porta, ipb, portb) "
                            + "VALUES('172.28.14.1', 'ODU 2', "
                            + "'172.28.20.1', 'ODU 1')");
                }
                txMgr.commit();
                txEnded = true;
            }
            finally {
                if(!txEnded) {
                    // Exception was thrown
                    txMgr.rollback();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
