package scheduler.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.controller.EmployeeService;
import scheduler.model.Employee;

public class EditEmployeeDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> positionComboBox;

    private final Employee employee;
    private final EmployeeService employeeService = new EmployeeService();

    public EditEmployeeDialog(Employee employee) {
        this.employee = employee;
        setTitle("Edit Employee: " + employee.getUsername());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("First Name:"));
        firstNameField = new JTextField(employee.getFirstName());
        form.add(firstNameField);

        form.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(employee.getLastName());
        form.add(lastNameField);

        form.add(new JLabel("Phone Number:"));
        phoneField = new JTextField(employee.getPhoneNumber());
        form.add(phoneField);

        form.add(new JLabel("Email:"));
        emailField = new JTextField(employee.getEmailAddress());
        form.add(emailField);

        form.add(new JLabel("Position:"));
        positionComboBox = new JComboBox<>(new String[]{"Technician", "Supervisor"});
        positionComboBox.setSelectedItem(employee.getPosition());
        form.add(positionComboBox);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    employeeService.updateEmployee(
                        employee.getUsername(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        positionComboBox.getSelectedItem().toString()
                    );
                    JOptionPane.showMessageDialog(EditEmployeeDialog.this, "Employee updated.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditEmployeeDialog.this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        add(buttons, BorderLayout.SOUTH);
    }
}
