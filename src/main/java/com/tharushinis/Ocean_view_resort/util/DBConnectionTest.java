package com.tharushinis.Ocean_view_resort.util;

import java.sql.Connection;

public class DBConnectionTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful!");
                conn.close();
            } else {
                System.out.println("Database connection failed!");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database:");
            e.printStackTrace();
        }
    }
}

