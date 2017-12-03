package com.intracom.ems.workshop;

public interface ElementSession {
    void addElement(String ip, double lon, double lat)
        throws ApplLogicException;
}
