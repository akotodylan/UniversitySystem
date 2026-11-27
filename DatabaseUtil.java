package com.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Phase 3: The Database Connection (LO1 - Separation of Concerns)
 * Acts as the bridge between the JavaFX application and MySQL.
 */
public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    // CHANGE THIS TO YOUR ACTUAL MYSQL PASSWORD
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
