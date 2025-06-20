package scheduler.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Employee {

    private final String employeeId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String position;

    // Constructor for creating a new Employee (auto-generates ID, hashes password)
    public Employee(String username, String password, String firstName, String lastName, String phoneNumber, String emailAddress, String position) {
        this(generateNewEmployeeId(firstName, lastName), username, hashPassword(password), firstName, lastName, phoneNumber, emailAddress, position);
    }

    // Constructor for loading existing Employee
    public Employee(String employeeId, String username, String hashedPassword, String firstName, String lastName, String phoneNumber, String emailAddress, String position) {
        if (employeeId == null || employeeId.length() > 12) {
            throw new IllegalArgumentException("Employee ID is required and must be under 12 characters.");
        }
        this.employeeId = employeeId;
        setUsername(username);
        this.password = hashedPassword;
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmailAddress(emailAddress);
        setPosition(position);
    }

    public static String generateNewEmployeeId(String firstName, String lastName) {
        String initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        return initials + timestamp;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getPosition() { return position; }

    // Setters with validation
    public void setUsername(String username) {
        if (username == null || username.length() > 10) {
            throw new IllegalArgumentException("Username is required and must be under 10 characters.");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 16) {
            throw new IllegalArgumentException("Password must be between 8 and 16 characters.");
        }
        this.password = hashPassword(password);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 10) {
            throw new IllegalArgumentException("First name is required and must be under 10 characters.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 10) {
            throw new IllegalArgumentException("Last name is required and must be under 10 characters.");
        }
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.length() > 50) {
            throw new IllegalArgumentException("Email address is required and must be under 50 characters.");
        }
        this.emailAddress = emailAddress;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
