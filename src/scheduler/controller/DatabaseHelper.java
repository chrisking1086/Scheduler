/*
 * Initializes Database if tables do not exist
 */
package scheduler.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public DatabaseHelper() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Employees
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
            		employeeId Text PRIMARY KEY,
                    username TEXT UNIQUE,
                    firstName TEXT,
                    lastName TEXT,
                    phoneNumber TEXT,
                    emailAddress TEXT,
                    position TEXT,
                    password TEXT
                );
            """);

            // Tasks
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    taskId TEXT PRIMARY KEY,
                    name TEXT,
                    description TEXT,
                    status TEXT
                );
            """);

            // Customers
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    customerId TEXT PRIMARY KEY,
                    firstName TEXT,
                    lastName TEXT,
                    phoneNumber TEXT,
                    address TEXT
                );
            """);

            // Appointments
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS appointments (
                    appointmentId TEXT PRIMARY KEY,
                    title TEXT,
                    date TEXT,
                    description TEXT,
                    taskId TEXT,
                    customerId TEXT
                );
            """);

        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed: " + e.getMessage(), e);
        }
    }
}
