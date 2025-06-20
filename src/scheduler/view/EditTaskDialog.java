package scheduler.view;

import scheduler.controller.TaskService;
import scheduler.model.Task;
import scheduler.model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditTaskDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<TaskStatus> statusComboBox;

    private final Task task;
    private final TaskService taskService = new TaskService();

    public EditTaskDialog(Task task) {
        this.task = task;
        setTitle("Edit Task: " + task.getTaskId());
        setSize(400, 280);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Task Name:"));
        nameField = new JTextField(task.getName());
        form.add(nameField);

        form.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(TaskStatus.values());
        statusComboBox.setSelectedItem(task.getStatus());
        form.add(statusComboBox);

        form.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(task.getDescription(), 3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descriptionArea));

        getContentPane().add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(this::handleSave);
        cancelBtn.addActionListener(e -> dispose());

        buttons.add(saveBtn);
        buttons.add(cancelBtn);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void handleSave(ActionEvent e) {
        try {
            String name = nameField.getText();
            String description = descriptionArea.getText();
            TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

            taskService.updateTask(task.getTaskId(), name, description, status);
            JOptionPane.showMessageDialog(this, "Task updated.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
