package scheduler.controller;

import scheduler.model.Task;
import scheduler.model.TaskStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

	private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public TaskService() {
        
    }

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks(taskId, name, description, status) VALUES(?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, task.getTaskId());
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getStatus().name());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(String taskId) {
        String sql = "DELETE FROM tasks WHERE taskId = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, taskId);
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new IllegalArgumentException("Task not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(String taskId, String name, String description, TaskStatus status) {
        String sql = "UPDATE tasks SET name=?, description=?, status=? WHERE taskId=?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, status.name());
            pstmt.setString(4, taskId);

            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new IllegalArgumentException("Task not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Task getTask(String taskId) {
        String sql = "SELECT * FROM tasks WHERE taskId = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Task(
                    rs.getString("taskId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    TaskStatus.valueOf(rs.getString("status"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tasks.add(new Task(
                    rs.getString("taskId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    TaskStatus.valueOf(rs.getString("status"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // Retrieve task name for use in manage appointments dialog 
	public String getTaskName(String taskId) {
		String taskName = getTask(taskId).getName();
		return taskName;
	}
}
