package com.intracom.ems.workshop;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ApplLogicException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApplLogicException(String message) {
        super(message);
    }
}
