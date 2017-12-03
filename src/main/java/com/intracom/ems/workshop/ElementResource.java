package com.intracom.ems.workshop;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intracom.ems.workshop.model.Ne;

@Path("/ne")
public class ElementResource {
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getElements() {
        List<Ne> nes;
        try {
            nes = getNes();
        }
        catch (SQLException e) {
            throw new WorkshopException("Database Error: " + e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootObj = mapper.createObjectNode();
        ArrayNode elementsArray = rootObj.putArray("elements");
        for(Ne ne : nes) {
            elementsArray.add(mapper.valueToTree(ne));
        }
        return rootObj.toString();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createElement(final InputStream in) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode reqNode = null;
        try {
            reqNode = mapper.readTree(in);
        } catch (IOException e1) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        String ip = reqNode.path("ip").asText();
        double lon = reqNode.path("lon").asDouble();
        double lat = reqNode.path("lat").asDouble();
        try {
            InitialContext iCtx = new InitialContext();
            ElementSession elementSession = (ElementSession) iCtx.lookup(
                    "java:module/ElementSessionBean!com.intracom.ems.workshop.ElementSession");
            elementSession.addElement(ip, lon, lat);
            ObjectNode resObject = mapper.createObjectNode();
            resObject.put("status", 0);
            return Response.ok(resObject.toString(), MediaType.APPLICATION_JSON_TYPE).build();
        } catch (NamingException e) {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (ApplLogicException e) {
            throw new WorkshopException(e.getMessage());
        }
    }

    private List<Ne> getNes() throws SQLException {
        DataSource dataSource = WorkshopToolkit.inst().getDataSource();
        try(Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet res = stmt.executeQuery(
                        "SELECT ip, lon, lat FROM element")) {
            List<Ne> list = new ArrayList<>();
            while(res.next()) {
                String ip = res.getString(1);
                double lon = res.getDouble(2);
                double lat = res.getDouble(3);
                Ne ne = new Ne();
                ne.setIp(ip);
                ne.setLon(lon);
                ne.setLat(lat);
                list.add(ne);
            }
            return list;
        }
    }
}
