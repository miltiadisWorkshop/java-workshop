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
            /*
             * 1st Exercise. Write your code for first exercise here. The
             * response should carry the following string: Participant: THE NAME
             * OF Participant Workshop: Java SE/EE Workshop Place: Central
             * Library of NTUA City: Athens Coordinator: Intracom Telecom S.A.
             */
        }
    }

    private String responseAsJson(String participantName) {
        /*
         * 3rd Exersice Write code that will return the previous response in a
         * JSON format.
         */
        throw new RuntimeException("Not Implemented");
    }
}
