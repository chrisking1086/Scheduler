package scheduler.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import scheduler.controller.CustomerService;
import scheduler.model.Customer;

public class EditCustomerDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextArea addressArea;

    private final CustomerService customerService = new CustomerService();

    public EditCustomerDialog(Customer customer) {
        setTitle("Edit Customer: " + customer.getCustomerId());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("First Name:"));
        firstNameField = new JTextField(customer.getFirstName());
        form.add(firstNameField);

        form.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(customer.getLastName());
        form.add(lastNameField);

        form.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(customer.getPhoneNumber());
        form.add(phoneNumberField);

        form.add(new JLabel("Address:"));
        addressArea = new JTextArea(customer.getAddress());
        form.add(new JScrollPane(addressArea));

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    customerService.updateCustomer(
                        customer.getCustomerId(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        phoneNumberField.getText(),
                        addressArea.getText()
                    );
                    JOptionPane.showMessageDialog(EditCustomerDialog.this, "Customer updated.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditCustomerDialog.this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        buttons.add(saveBtn);
        buttons.add(cancelBtn);
        add(buttons, BorderLayout.SOUTH);
    }
}
