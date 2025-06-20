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

import scheduler.controller.AppointmentService;
import scheduler.controller.CustomerService;
import scheduler.controller.TaskService;
import scheduler.model.Appointment;

public class ManageAppointmentDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private AppointmentService appointmentService = new AppointmentService();
    private CustomerService customerService = new CustomerService();
    private TaskService taskService = new TaskService();
    
 // For Refreshing list after editing
    private void loadTableData(DefaultTableModel model) {
        model.setRowCount(0); // clear existing rows
        List<Appointment> appointments = appointmentService.getAppointments();
        for (Appointment appointment : appointments) {
            model.addRow(new Object[]{
                appointment.getAppointmentId(),
                appointment.getTitle(),
                appointment.getDate(),
                customerService.getCustomerInfo(appointment.getCustomerId()),
                taskService.getTaskName(appointment.getTaskId())
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

    public ManageAppointmentDialog() {
        setTitle("Manage Appointments");
        setBounds(100, 100, 616, 400);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        String[] columns = {"Appointment ID", "Title", "Date & Time", "Customer Name", "Task Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };

        List<Appointment> appointments = appointmentService.getAppointments();
        for (Appointment appointment : appointments) {
            tableModel.addRow(new Object[]{
                appointment.getAppointmentId(),
                appointment.getTitle(),
                appointment.getDate(),
                customerService.getCustomerInfo(appointment.getCustomerId()),
                taskService.getTaskName(appointment.getTaskId())
            });
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
                    String appointmentId = (String) table.getValueAt(table.getSelectedRow(), 0);
                    Appointment appointment = appointmentService.getAppointment(appointmentId);
                    if (appointment != null) {
                        EditAppointmentDialog editDialog = new EditAppointmentDialog(appointment);
                        editDialog.setModal(true); // Ensure modal
                        editDialog.setVisible(true);
                        loadTableData((DefaultTableModel) table.getModel()); // Refresh table data
                    }
                }
            }
        });

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton markCompleteButton = new JButton("Mark Complete");
        markCompleteButton.addActionListener(e -> handleDelete());
        buttonPane.add(markCompleteButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
        getRootPane().setDefaultButton(closeButton);
    }
    
    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an Appointment to mark completed.");
            return;
        }

        String appointmentId = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to mark this appointment completed: " + appointmentId + "?",
            "Confirm Completion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            appointmentService.deleteAppointment(appointmentId);
            loadTableData((DefaultTableModel) table.getModel());
        }
    }
}
