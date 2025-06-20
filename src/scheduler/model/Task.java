package scheduler.model;

public class Task {

    private final String taskId;
    private String name;
    private String description;
    private TaskStatus status;

    // Constructor for new task (generates ID)
    public Task(String name, String description, TaskStatus status) {
        this(generateNewTaskId(name), name, description, status);
    }

    // Constructor for loading existing task
    public Task(String taskId, String name, String description, TaskStatus status) {
        if (taskId == null || taskId.length() > 12) {
            throw new IllegalArgumentException("Task ID is required and must be under 12 characters.");
        }
        this.taskId = taskId;
        setName(name);
        setDescription(description);
        setStatus(status);
    }

    public static String generateNewTaskId(String name) {
        String prefix = name != null && name.length() >= 2
            ? name.substring(0, 2).toUpperCase()
            : "TS";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        return prefix + timestamp;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.length() > 30) {
            throw new IllegalArgumentException("Task name is required and must be under 30 characters.");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > 100) {
            throw new IllegalArgumentException("Description is required and must be under 100 characters.");
        }
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Task status cannot be null.");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " - " + description + " [" + status + "]";
    }
}
