/*
 * Dialog for creating a new customer and adding to database through database handler
 */

package scheduler.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import scheduler.controller.CustomerService;
import scheduler.model.Customer;

public class AddCustomerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField firstName;
	private JTextField lastName;
	private JTextField phoneNumber;
	private JTextArea address;

	/**
	 * Create the dialog.
	 */
	public AddCustomerDialog() {
		setTitle("Add New Customer");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{				JButton addCustomerButton = new JButton("Add");
				addCustomerButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					    CustomerService customerService = new CustomerService();

					    try {
					        String fName = firstName.getText();
					        String lName = lastName.getText();
					        String phone = phoneNumber.getText();
					        String addr = address.getText();

					        if (fName == null || fName.trim().isEmpty() ||
					            lName == null || lName.trim().isEmpty() ||
					            phone == null || phone.trim().isEmpty() ||
					            addr == null || addr.trim().isEmpty()) {

					            JOptionPane.showMessageDialog(AddCustomerDialog.this,
					                    "All fields are required.",
					                    "Missing Input",
					                    JOptionPane.WARNING_MESSAGE);
					            return;
					        }

					        Customer customer = new Customer(fName.trim(), lName.trim(), phone.trim(), addr.trim());
					        customerService.addCustomer(customer);
					        JOptionPane.showMessageDialog(AddCustomerDialog.this, "Customer added!");
					        dispose();

					    } catch (IllegalArgumentException ex) {
					        JOptionPane.showMessageDialog(AddCustomerDialog.this,
					                ex.getMessage(),
					                "Invalid Input",
					                JOptionPane.ERROR_MESSAGE);
					    } catch (Exception ex) {
					        JOptionPane.showMessageDialog(AddCustomerDialog.this,
					                "An unexpected error occurred: " + ex.getMessage(),
					                "Error",
					                JOptionPane.ERROR_MESSAGE);
					    }
					}
				});
				addCustomerButton.setActionCommand("Add");
				buttonPane.add(addCustomerButton);
				getRootPane().setDefaultButton(addCustomerButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(10, 10, 1, 10));
			getContentPane().add(panel, BorderLayout.CENTER);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{156, 86, 0};
			gbl_panel.rowHeights = new int[]{20, 0, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JLabel txtpnFirstName = new JLabel();
				txtpnFirstName.setText("First Name");
				GridBagConstraints gbc_txtpnFirstName = new GridBagConstraints();
				gbc_txtpnFirstName.insets = new Insets(0, 0, 5, 5);
				gbc_txtpnFirstName.gridx = 0;
				gbc_txtpnFirstName.gridy = 0;
				panel.add(txtpnFirstName, gbc_txtpnFirstName);
			}
			{
				firstName = new JTextField();
				GridBagConstraints gbc_firstName = new GridBagConstraints();
				gbc_firstName.insets = new Insets(0, 0, 5, 0);
				gbc_firstName.fill = GridBagConstraints.HORIZONTAL;
				gbc_firstName.gridx = 1;
				gbc_firstName.gridy = 0;
				panel.add(firstName, gbc_firstName);
				firstName.setColumns(10);
			}
			{
				JLabel txtpnLastName = new JLabel();
				txtpnLastName.setText("Last Name");
				GridBagConstraints gbc_txtpnLastName = new GridBagConstraints();
				gbc_txtpnLastName.insets = new Insets(0, 0, 5, 5);
				gbc_txtpnLastName.gridx = 0;
				gbc_txtpnLastName.gridy = 1;
				panel.add(txtpnLastName, gbc_txtpnLastName);
			}
			{
				lastName = new JTextField();
				GridBagConstraints gbc_lastName = new GridBagConstraints();
				gbc_lastName.insets = new Insets(0, 0, 5, 0);
				gbc_lastName.fill = GridBagConstraints.BOTH;
				gbc_lastName.gridx = 1;
				gbc_lastName.gridy = 1;
				panel.add(lastName, gbc_lastName);
				lastName.setColumns(10);
			}
			{
				JLabel txtpnPhoneNumber = new JLabel();
				txtpnPhoneNumber.setText("Phone Number");
				GridBagConstraints gbc_txtpnPhoneNumber = new GridBagConstraints();
				gbc_txtpnPhoneNumber.insets = new Insets(0, 0, 5, 5);
				gbc_txtpnPhoneNumber.gridx = 0;
				gbc_txtpnPhoneNumber.gridy = 2;
				panel.add(txtpnPhoneNumber, gbc_txtpnPhoneNumber);
			}
			{
				phoneNumber = new JTextField();
				GridBagConstraints gbc_phoneNumber = new GridBagConstraints();
				gbc_phoneNumber.insets = new Insets(0, 0, 5, 0);
				gbc_phoneNumber.fill = GridBagConstraints.HORIZONTAL;
				gbc_phoneNumber.gridx = 1;
				gbc_phoneNumber.gridy = 2;
				panel.add(phoneNumber, gbc_phoneNumber);
				phoneNumber.setColumns(10);
			}
			{
				JLabel txtpnAddress = new JLabel();
				txtpnAddress.setText("Address");
				GridBagConstraints gbc_txtpnAddress = new GridBagConstraints();
				gbc_txtpnAddress.insets = new Insets(0, 0, 0, 5);
				gbc_txtpnAddress.gridx = 0;
				gbc_txtpnAddress.gridy = 3;
				panel.add(txtpnAddress, gbc_txtpnAddress);
			}
			{
				address = new JTextArea();
				GridBagConstraints gbc_address = new GridBagConstraints();
				gbc_address.fill = GridBagConstraints.BOTH;
				gbc_address.gridx = 1;
				gbc_address.gridy = 3;
				panel.add(address, gbc_address);
			}
		}
	}

}
