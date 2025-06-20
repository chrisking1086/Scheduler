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

import scheduler.controller.EmployeeService;
import scheduler.model.Employee;

public class ManageEmployeeDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JPanel contentPanel = new JPanel();
    private EmployeeService employeeService = new EmployeeService();
    
    // For Refreshing list after editing
    private void loadTableData(DefaultTableModel model) {
        model.setRowCount(0); // clear existing rows
        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            model.addRow(new Object[]{
                employee.getEmployeeId(),
                employee.getUsername(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getEmailAddress(),
                employee.getPosition()
            });
        }
    }
    
    private int getMaxColumnWidth(JTable table, int columnIndex) {
    	// Initial max width
        int maxWidth = 50;

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

    public ManageEmployeeDialog() {
    	setType(Type.UTILITY);
        setTitle("Manage Employees");
        setBounds(100, 100, 700, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        String[] columns = {"Employee ID", "Username", "First Name", "Last Name", "Phone Number", "Email", "Position"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        
        // Generate employee list from database for populating table
        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            Object[] row = {
                employee.getEmployeeId(),
                employee.getUsername(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getEmailAddress(),
                employee.getPosition()
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

        // Double-click to edit
        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String username = (String) table.getValueAt(table.getSelectedRow(), 1);
                    Employee employee = employeeService.getEmployeeByUsername(username);
                    if (employee != null) {
                        EditEmployeeDialog editDialog = new EditEmployeeDialog(employee);
                        editDialog.setModal(true); // Ensure modal
                        editDialog.setVisible(true);
                        loadTableData((DefaultTableModel) table.getModel()); // Refresh table data
                    }
                }
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDelete());
        buttonPane.add(deleteButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
        getRootPane().setDefaultButton(closeButton);
    }
    
    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        String username = (String) table.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete employee: " + username + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            employeeService.deleteEmployee(username);
            loadTableData((DefaultTableModel) table.getModel());
        }
    }
    
}
