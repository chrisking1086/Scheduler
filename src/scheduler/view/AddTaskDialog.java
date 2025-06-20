package scheduler.view;

import scheduler.controller.TaskService;
import scheduler.model.Task;
import scheduler.model.TaskStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTaskDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();
    private JTextField nameField;
    private JComboBox<TaskStatus> statusComboBox;
    private JTextArea descriptionArea;

    /**
     * Create the dialog.
     */
    public AddTaskDialog() {
        setTitle("Add New Task");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton addTaskButton = new JButton("Add");
        addTaskButton.setActionCommand("Add");
        buttonPane.add(addTaskButton);
        getRootPane().setDefaultButton(addTaskButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        // Form Panel
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{156, 86, 0};
        gbl_panel.rowHeights = new int[]{20, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        // Task Name
        JTextPane nameLabel = new JTextPane();
        nameLabel.setText("Task Name");
        nameLabel.setEditable(false);
        GridBagConstraints gbc_nameLabel = new GridBagConstraints();
        gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_nameLabel.gridx = 0;
        gbc_nameLabel.gridy = 0;
        panel.add(nameLabel, gbc_nameLabel);

        nameField = new JTextField();
        GridBagConstraints gbc_nameField = new GridBagConstraints();
        gbc_nameField.insets = new Insets(0, 0, 5, 0);
        gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_nameField.gridx = 1;
        gbc_nameField.gridy = 0;
        panel.add(nameField, gbc_nameField);

        // Task Status
        JTextPane statusLabel = new JTextPane();
        statusLabel.setText("Status");
        statusLabel.setEditable(false);
        GridBagConstraints gbc_statusLabel = new GridBagConstraints();
        gbc_statusLabel.insets = new Insets(0, 0, 5, 5);
        gbc_statusLabel.gridx = 0;
        gbc_statusLabel.gridy = 1;
        panel.add(statusLabel, gbc_statusLabel);

        statusComboBox = new JComboBox<>(TaskStatus.values());
        GridBagConstraints gbc_statusCombo = new GridBagConstraints();
        gbc_statusCombo.insets = new Insets(0, 0, 5, 0);
        gbc_statusCombo.fill = GridBagConstraints.HORIZONTAL;
        gbc_statusCombo.gridx = 1;
        gbc_statusCombo.gridy = 1;
        panel.add(statusComboBox, gbc_statusCombo);

        // Task Description
        JTextPane descLabel = new JTextPane();
        descLabel.setText("Description");
        descLabel.setEditable(false);
        GridBagConstraints gbc_descLabel = new GridBagConstraints();
        gbc_descLabel.insets = new Insets(0, 0, 0, 5);
        gbc_descLabel.gridx = 0;
        gbc_descLabel.gridy = 2;
        panel.add(descLabel, gbc_descLabel);

        descriptionArea = new JTextArea();
        GridBagConstraints gbc_description = new GridBagConstraints();
        gbc_description.fill = GridBagConstraints.BOTH;
        gbc_description.gridx = 1;
        gbc_description.gridy = 2;
        panel.add(descriptionArea, gbc_description);

        // Action Listeners
        TaskService taskService = new TaskService();

        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String description = descriptionArea.getText();
                    TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

                    Task task = new Task(name, description, status);
                    taskService.addTask(task);
                    JOptionPane.showMessageDialog(AddTaskDialog.this, "Task added!");
                    dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(AddTaskDialog.this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }
}
