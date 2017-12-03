package com.intracom.ems.workshop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import org.apache.commons.validator.routines.InetAddressValidator;

@Stateless
@Local
public class ElementSessionBean implements ElementSession {
    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    private DataSource dataSource;

    @Override
    public void addElement(String ip, double lon, double lat)
        throws ApplLogicException {
        if(!InetAddressValidator.getInstance().isValid(ip)) {
            throw new ApplLogicException("Not valid IP");
        }
        try(Connection con = dataSource.getConnection();) {
            try(Statement stmt = con.createStatement();
                    ResultSet res = stmt.executeQuery(
                            "SELECT ip from element WHERE ip='" + ip + "'");) {
                if(res.next()) {
                    throw new ApplLogicException("IP has been already added");
                }
                stmt.execute("INSERT INTO element(ip, lon, lat) VALUES ("
                        + "'" + ip + "', " + lon + ", " + lat + ")");
                stmt.execute("INSERT INTO port(ip, port) VALUES ("
                        + "'" + ip + "', 'ODU 1')");
                stmt.execute("INSERT INTO port(ip, port) VALUES ("
                        + "'" + ip + "', 'ODU 2')");
            }
            catch (SQLException exc) {
                exc.printStackTrace();
                throw new ApplLogicException(exc.getMessage());
            }
        }
        catch(SQLException exc)
        {
            throw new EJBException("Cannot obtain a database connection");
        }
    }
}
