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

import scheduler.controller.TaskService;
import scheduler.model.Task;

public class ManageTaskDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private TaskService taskService = new TaskService();
    
    // For Refreshing list after editing
    private void loadTableData(DefaultTableModel model) {
        model.setRowCount(0); // clear existing rows
        List<Task> tasks = taskService.getAllTasks();
        for (Task task: tasks) {
            model.addRow(new Object[]{
                task.getTaskId(),
                task.getName(),
                task.getDescription(),
                task.getDescription()
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

    public ManageTaskDialog() {
    	setType(Type.UTILITY);
        setTitle("Manage Tasks");
        setBounds(100, 100, 616, 400);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        String[] columns = {"Task ID", "Title", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };

        List<Task> tasks = taskService.getAllTasks();
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                task.getTaskId(),
                task.getName(),
                task.getDescription(),
                task.getStatus()
            });
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
                    String taskId = (String) table.getValueAt(table.getSelectedRow(), 0);
                    Task task = taskService.getTask(taskId);
                    if (task != null) {
                        EditTaskDialog editDialog = new EditTaskDialog(task);
                        editDialog.setModal(true); // Ensure modal
                        editDialog.setVisible(true);
                        loadTableData((DefaultTableModel) table.getModel()); // Refresh table data
                    }
                }
            }
        });

        // Buttons
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
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        String taskId = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete task: " + taskId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            taskService.deleteTask(taskId);
            loadTableData((DefaultTableModel) table.getModel());
        }
    }
}
