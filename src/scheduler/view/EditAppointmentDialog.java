package scheduler.view;

import scheduler.controller.AppointmentService;
import scheduler.controller.CustomerService;
import scheduler.controller.TaskService;
import scheduler.model.Appointment;
import scheduler.model.Customer;
import scheduler.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditAppointmentDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField dateField;
    private JTextArea descriptionArea;
    private JComboBox<CustomerItem> customerSelector;
    private JComboBox<Task> taskSelector;

    private final Appointment appointment;
    private final AppointmentService appointmentService = new AppointmentService();

    public EditAppointmentDialog(Appointment appointment) {
        this.appointment = appointment;
        setTitle("Edit Appointment: " + appointment.getAppointmentId());
        setSize(400, 360);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Title:"));
        titleField = new JTextField(appointment.getTitle());
        form.add(titleField);

        form.add(new JLabel("Date (YYYY-MM-DD): Time (HH:MM):"));
        dateField = new JTextField(appointment.getDate());
        form.add(dateField);

        form.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(2, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setText(appointment.getDescription());
        form.add(new JScrollPane(descriptionArea));

        form.add(new JLabel("Customer:"));
        customerSelector = new JComboBox<>();
        form.add(customerSelector);

        form.add(new JLabel("Task:"));
        taskSelector = new JComboBox<>();
        form.add(taskSelector);

        populateSelectors();

        getContentPane().add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(this::handleSave);
        cancelButton.addActionListener(e -> dispose());

        buttons.add(saveButton);
        buttons.add(cancelButton);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void populateSelectors() {
        CustomerService customerService = new CustomerService();
        TaskService taskService = new TaskService();

        for (Customer customer : customerService.getAllCustomers()) {
            CustomerItem item = new CustomerItem(
                customer.getCustomerId(),
                customerService.getCustomerInfo(customer.getCustomerId())
            );
            customerSelector.addItem(item);

            if (customer.getCustomerId().equals(appointment.getCustomerId())) {
                customerSelector.setSelectedItem(item);
            }
        }

        for (Task task : taskService.getAllTasks()) {
            taskSelector.addItem(task);
            if (task.getTaskId().equals(appointment.getTaskId())) {
                taskSelector.setSelectedItem(task);
            }
        }
    }

    private void handleSave(ActionEvent e) {
        try {
            appointment.setTitle(titleField.getText());
            appointment.setDate(dateField.getText());
            appointment.setDescription(descriptionArea.getText());

            CustomerItem selectedCustomer = (CustomerItem) customerSelector.getSelectedItem();
            Task selectedTask = (Task) taskSelector.getSelectedItem();

            if (selectedCustomer == null || selectedTask == null) {
                throw new IllegalArgumentException("Both customer and task must be selected.");
            }

            appointment.setCustomerId(selectedCustomer.getId());
            appointment.setTaskId(selectedTask.getTaskId());

            appointmentService.updateAppointment(
                appointment.getAppointmentId(),
                appointment.getTitle(),
                appointment.getDate(),
                appointment.getDescription(),
                appointment.getCustomerId(),
                appointment.getTaskId()
            );

            JOptionPane.showMessageDialog(this, "Appointment updated.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner helper class for customer combo box items
    private static class CustomerItem {
        private final String customerId;
        private final String customerName;

        public CustomerItem(String id, String name) {
            this.customerId = id;
            this.customerName = name;
        }

        public String getId() {
            return customerId;
        }

        @Override
        public String toString() {
            return customerName; // show friendly label
        }
    }
}
