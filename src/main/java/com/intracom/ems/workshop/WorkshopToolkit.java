package com.intracom.ems.workshop;

import javax.sql.DataSource;

public class WorkshopToolkit {
    private static final class Singleton {
        private static final WorkshopToolkit INSTANCE = new WorkshopToolkit();
    }

    public static final int ERROR_CODE = 599;

    public static WorkshopToolkit inst() {
        return Singleton.INSTANCE;
    }

    private DataSource dataSource = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
