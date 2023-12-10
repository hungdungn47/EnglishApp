package com.example.englishapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12662519";
    static final String USER = "sql12662519";
    static final String PASS = "EmA6Z8XLRD";
    static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
