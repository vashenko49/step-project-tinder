package com.tinder.dataSource;

import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import org.apache.commons.dbcp2.BasicDataSource;

public final class ConnectionPool {
    private static volatile BasicDataSource dataSource;
    private static final Object mutex = new Object();

    public static BasicDataSource getDataSource() throws ErrorConnectionToDataBase {
        if (dataSource == null) {
            synchronized (mutex) {
                try {
                    ConfigFile configFile = ConfigFile.getInstance();
                    BasicDataSource ds = new BasicDataSource();
                    ds.setUrl(configFile.getValueByKey("db.url"));
                    ds.setUsername(configFile.getValueByKey("db.user"));
                    ds.setPassword(configFile.getValueByKey("db.password"));
                    ds.setMinIdle(10);
                    ds.setMaxIdle(15);
                    ds.setMaxOpenPreparedStatements(100);
                    dataSource = ds;
                } catch (ConfigFileException e) {
                    throw new ErrorConnectionToDataBase("Error  prepared to connection to data base ;" + e.getMessage());
                }

            }
        }
        return dataSource;
    }
}
