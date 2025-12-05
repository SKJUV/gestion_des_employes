package com.gestion.etu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to obtain JDBC connections.
 *
 * Important: do NOT cache a single Connection object and return it to callers
 * who then close it via try-with-resources. Instead this method returns a new
 * Connection for each call; callers should close the connection when finished.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_employes?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root123"; // Changez le mot de passe

    private DatabaseConnection() {
        // private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        // Explicitly load the driver (optional with modern drivers but harmless)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // driver should be available on the classpath via the MySQL connector dependency
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
