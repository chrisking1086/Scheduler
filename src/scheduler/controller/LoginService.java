package scheduler.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import scheduler.model.Employee;

public class LoginService {

	private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public LoginService() {
    }

    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM employees WHERE username = ?";
        String passwordToValidate = Employee.hashPassword(password); //Hash the password
        try (Connection conn = DriverManager.getConnection(DB_URL);
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {

            	pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password"); // Get the hashed password
                return storedPassword.equals(passwordToValidate);
            } else {
                return false; // username not found
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
