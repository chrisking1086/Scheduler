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

public class AddAppointmentDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextArea descriptionArea;
    private JComboBox<CustomerItem> customerSelector;
    private JComboBox<Task> taskSelector;

    public AddAppointmentDialog() {
        setTitle("Add New Appointment");
        setModal(true);
        setSize(450, 520);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(formPanel, BorderLayout.CENTER);

        // Title
        GridBagConstraints gbcTitleLabel = new GridBagConstraints();
        gbcTitleLabel.gridx = 0;
        gbcTitleLabel.gridy = 0;
        gbcTitleLabel.anchor = GridBagConstraints.WEST;
        gbcTitleLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Title:"), gbcTitleLabel);

        titleField = new JTextField(20);
        GridBagConstraints gbcTitleField = new GridBagConstraints();
        gbcTitleField.gridx = 0;
        gbcTitleField.gridy = 1;
        gbcTitleField.fill = GridBagConstraints.HORIZONTAL;
        gbcTitleField.insets = new Insets(0, 6, 6, 6);
        formPanel.add(titleField, gbcTitleField);

        // Date
        GridBagConstraints gbcDateLabel = new GridBagConstraints();
        gbcDateLabel.gridx = 0;
        gbcDateLabel.gridy = 2;
        gbcDateLabel.anchor = GridBagConstraints.WEST;
        gbcDateLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbcDateLabel);

        dateField = new JTextField(20);
        GridBagConstraints gbcDateField = new GridBagConstraints();
        gbcDateField.gridx = 0;
        gbcDateField.gridy = 3;
        gbcDateField.fill = GridBagConstraints.HORIZONTAL;
        gbcDateField.insets = new Insets(0, 6, 6, 6);
        formPanel.add(dateField, gbcDateField);

        // Time
        GridBagConstraints gbcTimeLabel = new GridBagConstraints();
        gbcTimeLabel.gridx = 0;
        gbcTimeLabel.gridy = 4;
        gbcTimeLabel.anchor = GridBagConstraints.WEST;
        gbcTimeLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Time (HH:MM):"), gbcTimeLabel);

        timeField = new JTextField(20);
        GridBagConstraints gbcTimeField = new GridBagConstraints();
        gbcTimeField.gridx = 0;
        gbcTimeField.gridy = 5;
        gbcTimeField.fill = GridBagConstraints.HORIZONTAL;
        gbcTimeField.insets = new Insets(0, 6, 6, 6);
        formPanel.add(timeField, gbcTimeField);

        // Description
        GridBagConstraints gbcDescriptionLabel = new GridBagConstraints();
        gbcDescriptionLabel.gridx = 0;
        gbcDescriptionLabel.gridy = 6;
        gbcDescriptionLabel.anchor = GridBagConstraints.WEST;
        gbcDescriptionLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Description:"), gbcDescriptionLabel);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        GridBagConstraints gbcDescriptionArea = new GridBagConstraints();
        gbcDescriptionArea.gridx = 0;
        gbcDescriptionArea.gridy = 7;
        gbcDescriptionArea.fill = GridBagConstraints.BOTH;
        gbcDescriptionArea.weightx = 1.0;
        gbcDescriptionArea.weighty = 1.0;
        gbcDescriptionArea.insets = new Insets(0, 6, 6, 6);
        formPanel.add(scrollPane, gbcDescriptionArea);

        // Customer
        GridBagConstraints gbcCustomerLabel = new GridBagConstraints();
        gbcCustomerLabel.gridx = 0;
        gbcCustomerLabel.gridy = 8;
        gbcCustomerLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Customer:"), gbcCustomerLabel);

        customerSelector = new JComboBox<>();
        GridBagConstraints gbcCustomerSelector = new GridBagConstraints();
        gbcCustomerSelector.gridx = 0;
        gbcCustomerSelector.gridy = 9;
        gbcCustomerSelector.fill = GridBagConstraints.HORIZONTAL;
        gbcCustomerSelector.insets = new Insets(0, 6, 6, 6);
        formPanel.add(customerSelector, gbcCustomerSelector);

        // Task
        GridBagConstraints gbcTaskLabel = new GridBagConstraints();
        gbcTaskLabel.gridx = 0;
        gbcTaskLabel.gridy = 10;
        gbcTaskLabel.anchor = GridBagConstraints.WEST;
        gbcTaskLabel.insets = new Insets(6, 6, 0, 6);
        formPanel.add(new JLabel("Task:"), gbcTaskLabel);

        taskSelector = new JComboBox<>();
        GridBagConstraints gbcTaskSelector = new GridBagConstraints();
        gbcTaskSelector.gridx = 0;
        gbcTaskSelector.gridy = 11;
        gbcTaskSelector.fill = GridBagConstraints.HORIZONTAL;
        gbcTaskSelector.insets = new Insets(0, 6, 6, 6);
        formPanel.add(taskSelector, gbcTaskSelector);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        addButton.addActionListener(this::handleAdd);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        populateSelectors();
    }

    private void populateSelectors() {
        CustomerService customerService = new CustomerService();
        TaskService taskService = new TaskService();

        for (Customer customer : customerService.getAllCustomers()) {
            customerSelector.addItem(new CustomerItem(
                customer.getCustomerId(),
                customerService.getCustomerInfo(customer.getCustomerId())
            ));
        }

        for (Task task : taskService.getAllTasks()) {
            taskSelector.addItem(task);
        }
    }

    private void handleAdd(ActionEvent e) {
        try {
            String title = titleField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String description = descriptionArea.getText().trim();
            CustomerItem selectedCustomer = (CustomerItem) customerSelector.getSelectedItem();
            Task selectedTask = (Task) taskSelector.getSelectedItem();

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || description.isEmpty()
                    || selectedCustomer == null || selectedTask == null) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Appointment appointment = new Appointment(
                title,
                date + " , " + time,
                description,
                selectedTask.getTaskId(),
                selectedCustomer.getId()
            );

            new AppointmentService().addAppointment(appointment);
            JOptionPane.showMessageDialog(this, "Appointment added successfully.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper class for Customer ComboBox items
    private static class CustomerItem {
        private final String id;
        private final String name;

        public CustomerItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
