package scheduler.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import scheduler.controller.CustomerService;
import scheduler.model.Customer;

public class ManageCustomerDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JPanel contentPanel = new JPanel();
    private CustomerService customerService = new CustomerService();
    
    // For Refreshing list after editing
    private void loadTableData(DefaultTableModel model) {
        model.setRowCount(0); // clear existing rows
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            model.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getAddress()
            });
        }
    }
    
    private int getMaxColumnWidth(JTable table, int columnIndex) {
        int maxWidth = 50; // minimum starting width

        for (int row = 0; row < table.getRowCount(); row++) {
            Object value = table.getValueAt(row, columnIndex);
            if (value != null) {
                Component component = table.getDefaultRenderer(value.getClass())
                                      .getTableCellRendererComponent(table, value, false, false, row, columnIndex);
                maxWidth = Math.max(component.getPreferredSize().width + 10, maxWidth);
            }
        }

        return maxWidth;
    }

    public ManageCustomerDialog() {
    	setType(Type.UTILITY);
        setTitle("Manage Customers");
        setBounds(100, 100, 600, 300);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout(0, 0));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        String[] columns = {"Customer ID", "First Name", "Last Name", "Phone", "Address"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getAddress()
            };
            tableModel.addRow(row);
        }

        table = new JTable(tableModel);
        
        // Auto-resize all columns to fit content
        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(getMaxColumnWidth(table, column));
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String customerId = (String) table.getValueAt(table.getSelectedRow(), 0);
                    Customer customer = customerService.getCustomerByCustomerId(customerId);
                    if (customer != null) {
                        EditCustomerDialog editDialog = new EditCustomerDialog(customer);
                        editDialog.setModal(true); // Ensure modal
                        editDialog.setVisible(true);
                        loadTableData((DefaultTableModel) table.getModel()); // Refresh table data
                    }
                }
            }
        });
        
        

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDelete());
        buttonPane.add(deleteButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    
    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an Appointment to mark completed.");
            return;
        }

        String customerId = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this customer: " + customerId + "?",
            "Confirm deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            customerService.deleteCustomer(customerId);
            loadTableData((DefaultTableModel) table.getModel());
        }
    }
}
