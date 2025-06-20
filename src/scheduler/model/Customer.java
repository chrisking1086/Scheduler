package scheduler.model;

public class Customer {

    private final String customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    // Constructor for creating a new customer (generates ID)
    public Customer(String firstName, String lastName, String phoneNumber, String address) {
        this(generateNewCustomerId(firstName, lastName), firstName, lastName, phoneNumber, address);
    }

    // Constructor for loading existing customer from database
    public Customer(String customerId, String firstName, String lastName, String phoneNumber, String address) {
        if (customerId == null || customerId.length() > 12) {
            throw new IllegalArgumentException("Customer ID is required and must be under 12 characters.");
        }
        this.customerId = customerId;
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setAddress(address);
    }

    public static String generateNewCustomerId(String firstName, String lastName) {
        String initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        return initials + timestamp;
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }

    // Setters with validation
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 20) {
            throw new IllegalArgumentException("First name is required and must be under 20 characters.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 20) {
            throw new IllegalArgumentException("Last name is required and must be under 20 characters.");
        }
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        if (address == null || address.length() > 50) {
            throw new IllegalArgumentException("Address is required and must be under 50 characters.");
        }
        this.address = address;
    }
}
