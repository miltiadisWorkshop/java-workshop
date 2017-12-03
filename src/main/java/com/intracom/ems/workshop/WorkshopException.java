package com.intracom.ems.workshop;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WorkshopException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public WorkshopException(final String message) {
        super(Response.status(WorkshopToolkit.ERROR_CODE)
                .entity(message).build());
    }
}
