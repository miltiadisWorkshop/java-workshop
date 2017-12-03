package com.intracom.ems.workshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public class HelpServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String acceptHeader = req.getHeader("Accept").toLowerCase();
        BufferedReader br = null;
        String participantName = null;
        try {
            br = req.getReader();
            participantName = IOUtils.toString(br);
        } catch (IOException exc) {
            resp.setStatus(500);
            return;
        }
        if(participantName.split(" ").length < 2) {
            try {
                resp.setContentType("text/plain; charset=UTF-8");
                resp.setStatus(WorkshopToolkit.ERROR_CODE);
                resp.getWriter().print("Please provide your Full Name");
            } catch (IOException e) {
                resp.setStatus(500);
            }
            return;
        }
        PrintWriter pw = null;
        try {
            resp.setCharacterEncoding("UTF-8");
            pw = resp.getWriter();
        } catch (IOException exc) {
            resp.setStatus(500);
            return;
        }
        switch (acceptHeader) {
        case "application/json":
            pw.print(responseAsJson(participantName));
            resp.setContentType("application/json");
            break;
        default:
            resp.setContentType("text/plain");
            pw.println("Participant: " + participantName);
            pw.println("Workshop: Java EE Workshop");
            pw.println("Place: Central Library of NTUA");
            pw.println("City: Athens");
            pw.println("Coordinator: Intracom Telecom S.A.");
            // should call flush() on writer?
        }
    }

    private String responseAsJson(String participantName) {
        throw new RuntimeException("Not Implemented");
    }
}
