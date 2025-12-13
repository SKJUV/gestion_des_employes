package com.gestion.employes.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private final String url;
    private final String user;
    private final String password;

    public DBConnection() {
        Properties properties = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger db.properties", e);
        }
        this.url = properties.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/gestion_employes");
        this.user = properties.getProperty("jdbc.user", "root");
        this.password = properties.getProperty("jdbc.password", "");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
