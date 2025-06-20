/*
 * Service Class for managing customer objects table in the database
 */
package scheduler.controller;

import scheduler.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

	private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public CustomerService() {
        // Empty Constructor
    }

    // Create add new customer to database
    public void addCustomer(Customer customer) {
        String sql = """
        		INSERT INTO customers(
        		customerId, 
        		firstName, 
        		lastName, 
        		phoneNumber, 
        		address
        		) 
        		VALUES(?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Return a single customer object by customer Id
    public Customer getCustomerByCustomerId(String customerId) {
        String sql = "SELECT * FROM customers WHERE customerId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getString("customerId"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phoneNumber"),
                    rs.getString("address")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Return a list of all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("customerId"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("phoneNumber"),
                    rs.getString("address")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    // Update customer information based on customerId
    public void updateCustomer(String customerId, String firstName, String lastName, String phoneNumber, String address) {
        String sql = """
        		UPDATE customers SET
        		firstName = ?, 
        		lastName = ?, 
        		phoneNumber = ?, 
        		address = ? 
        		WHERE customerId = ?
  		""";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, address);
            pstmt.setString(5, customerId);

            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new IllegalArgumentException("Customer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Delete customer from database
    public void deleteCustomer(String customerId) {
        String sql = "DELETE FROM customers WHERE customerId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new IllegalArgumentException("Customer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    // Retrieve customer name based on customerId for Manage Appointment Dialog
    public String getCustomerInfo(String customerId) {
    	
    	return getCustomerByCustomerId(customerId).getFirstName() + 
    			" " + getCustomerByCustomerId(customerId).getLastName() + 
    			": " + getCustomerByCustomerId(customerId).getPhoneNumber();
    }
}
