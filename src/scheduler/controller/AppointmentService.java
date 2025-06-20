package scheduler.controller;

import scheduler.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public AppointmentService() {
        
    }

    public void addAppointment(Appointment apptointment) {
        String sql = "INSERT INTO appointments (appointmentId, title, date, description, taskId, customerId) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, apptointment.getAppointmentId());
            pstmt.setString(2, apptointment.getTitle());
            pstmt.setString(3, apptointment.getDate());
            pstmt.setString(4, apptointment.getDescription());
            pstmt.setString(5, apptointment.getTaskId());
            pstmt.setString(6, apptointment.getCustomerId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add appointment: " + e.getMessage(), e);
        }
    }

    public void deleteAppointment(String appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointmentId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, appointmentId);
            int rows = pstmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Appointment not found.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete appointment: " + e.getMessage(), e);
        }
    }

    public void updateAppointment(String appointmentId, String title, String date, String description, String customerId, String taskId) {
        String sql = "UPDATE appointments SET title = ?, date = ?, description = ?, customerId = ?, taskId = ? WHERE appointmentId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

        	pstmt.setString(1, title);
            pstmt.setString(2, date);
            pstmt.setString(3, description);
            pstmt.setString(4, customerId);
            pstmt.setString(5, taskId);
            pstmt.setString(6, appointmentId);

            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new IllegalArgumentException("Appointment not found.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update appointment: " + e.getMessage(), e);
        }
    }

    public Appointment getAppointment(String appointmentId) {
        String sql = "SELECT * FROM appointments WHERE appointmentId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Appointment(
                    rs.getString("appointmentId"),
                    rs.getString("title"),
                    rs.getString("date"),
                    rs.getString("description"),
                    rs.getString("taskId"),
                    rs.getString("customerId")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve appointment: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Appointment> getAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Appointment(
                    rs.getString("appointmentId"),
                    rs.getString("title"),
                    rs.getString("date"),
                    rs.getString("description"),
                    rs.getString("taskId"),
                    rs.getString("customerId")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch appointments: " + e.getMessage(), e);
        }

        return list;
    }
}
