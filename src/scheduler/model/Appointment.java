package scheduler.model;

public class Appointment {

    private final String appointmentId;
    private String title;
    private String date;
    private String description;
    private String customerId;
    private String taskId;

    // Constructor for new appointment (auto-generates ID)
    public Appointment(String title, String date, String description, String taskId, String customerId) {
        this(generateNewAppointmentId(title), title, date, description, taskId, customerId);
    }

    // Constructor for existing appointment from DB
    public Appointment(String appointmentId, String title, String date, String description, String taskId, String customerId) {
        
        this.appointmentId = appointmentId;
        setTitle(title);
        setDate(date);
        setDescription(description);
        setCustomerId(customerId);
        setTaskId(taskId);
    }

    public static String generateNewAppointmentId(String title) {
        String prefix = title != null && title.length() >= 2
            ? title.substring(0, 2).toUpperCase()
            : "AP";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(10);
        return prefix + timestamp;
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getCustomerId() { return customerId; }
    public String getTaskId() { return taskId; }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.length() > 30) {
            throw new IllegalArgumentException("Title is required and must be under 30 characters.");
        }
        this.title = title;
    }

    public void setDate(String date) {
        if (date == null || date.length() < 8) {
            throw new IllegalArgumentException("Date is required and must be in proper format.");
        }
        this.date = date;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > 100) {
            throw new IllegalArgumentException("Description is required and must be under 100 characters.");
        }
        this.description = description;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null || customerId.length() > 12) {
            throw new IllegalArgumentException("Customer ID is required and must be under 12 characters.");
        }
        this.customerId = customerId;
    }

    public void setTaskId(String taskId) {
        if (taskId == null || taskId.length() > 12) {
            throw new IllegalArgumentException("Task ID is required and must be under 12 characters.");
        }
        this.taskId = taskId;
    }
}
