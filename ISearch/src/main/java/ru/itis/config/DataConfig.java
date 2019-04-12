package ru.itis.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataConfig {
    private static final String DB_DRIVER_CLASSNAME = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/isearch";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "parol123";

    private static volatile DataConfig instance;

    private DriverManagerDataSource dataSource;

    private DataConfig() {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER_CLASSNAME);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
    }

    public static DataConfig getInstance() {
        DataConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (DataConfig.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataConfig();
                }
            }
        }
        return localInstance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
