package scheduler.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import scheduler.model.Employee;


public class EmployeeService {
	private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public EmployeeService() {
        
    }

    public void addEmployee(Employee employee) {
    	
    	String sql = "INSERT INTO employees(employeeId, username, password, firstName, lastName, phoneNumber, emailAddress, position) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    	
        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

        	pstmt.setString(1, employee.getEmployeeId());
        	pstmt.setString(2, employee.getUsername());
        	pstmt.setString(3, employee.getPassword()); // already hashed by model
        	pstmt.setString(4, employee.getFirstName());
        	pstmt.setString(5, employee.getLastName());
        	pstmt.setString(6, employee.getPhoneNumber());
        	pstmt.setString(7, employee.getEmailAddress());
        	pstmt.setString(8, employee.getPosition());
        	System.out.println("Inserting employee: " + employee.getUsername());
        	System.out.println("With hashed password: " + employee.getPassword());
            pstmt.executeUpdate();

        } catch (SQLException e) {
        	System.err.println("Failed to insert employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteEmployee(String employeeId) {
        String sql = "DELETE FROM employees WHERE employeeId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Employee not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates employee information
    public void updateEmployee(String username, String firstName, String lastName, String phoneNumber, String email, String position) {
        String sql = "UPDATE employees SET firstName=?, lastName=?, phoneNumber=?, emailAddress=?, position=? WHERE username=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, position);
            pstmt.setString(6, username);
            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new IllegalArgumentException("Employee not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployeeByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
            	return new Employee(
            			rs.getString("employeeId"),
               		    rs.getString("username"),
               		    rs.getString("password"), // hashed password from DB
               		    rs.getString("firstName"),
               		    rs.getString("lastName"),
               		    rs.getString("phoneNumber"),
               		    rs.getString("emailAddress"),
               		    rs.getString("position")
            		);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Populate a list of all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new Employee(
                    rs.getString("employeeId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phoneNumber"),
                    rs.getString("emailAddress"),
                    rs.getString("position")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }
    			
}

