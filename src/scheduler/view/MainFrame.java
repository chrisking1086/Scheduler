package scheduler.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import scheduler.controller.DatabaseHelper;
import scheduler.model.Appointment;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private AppointmentListModel appointmentListModel;

    public MainFrame() {
    	try {
    	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	}
        setTitle("Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        initMenuBar();
        initAppointmentList();
    }

    // Manage menu bar
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File Menu and Item declarations
        JMenu fileMenu = new JMenu("File");
        JMenuItem newApptointmentItem = new JMenuItem("New Appointment");
        JMenuItem addCustomerItem = new JMenuItem("New Customer");
        JMenuItem addTaskItem = new JMenuItem("New Task");

        //Assign Menu Item Listeners
        newApptointmentItem.addActionListener(e -> {
            AddAppointmentDialog dialog = new AddAppointmentDialog();
            dialog.setModal(true);
            dialog.setVisible(true);
            appointmentListModel.refresh(); 
        });
        addCustomerItem.addActionListener(e -> new AddCustomerDialog().setVisible(true));
        addTaskItem.addActionListener(e -> new AddTaskDialog().setVisible(true));

        // Add File Menu Items
        fileMenu.add(newApptointmentItem);
        fileMenu.add(addCustomerItem);
        fileMenu.add(addTaskItem);
        menuBar.add(fileMenu);

        // Administration Menu Item Declarations
        JMenu adminMenu = new JMenu("Administration");
        JMenuItem addEmployeeItem = new JMenuItem("New Employee");
        JMenuItem manageEmployeesItem = new JMenuItem("Manage Employees");
        JMenuItem manageCustomersItem = new JMenuItem("Manage Customers");
        JMenuItem manageAppointmentsItem = new JMenuItem("Manage Appointments");
        JMenuItem manageTasksItem = new JMenuItem("Manage Tasks");

        // Assign Action Listeners
        addEmployeeItem.addActionListener(e -> new AddEmployeeDialog().setVisible(true));
        manageEmployeesItem.addActionListener(e -> new ManageEmployeeDialog().setVisible(true));
        manageCustomersItem.addActionListener(e -> new ManageCustomerDialog().setVisible(true));
        manageAppointmentsItem.addActionListener(e -> {
        	ManageAppointmentDialog manageAppointmentDialog = new ManageAppointmentDialog();
        	manageAppointmentDialog.setModal(true);
        	manageAppointmentDialog.setVisible(true);
        	appointmentListModel.refresh();
        });
        manageTasksItem.addActionListener(e -> new ManageTaskDialog().setVisible(true));
        
        //Add Administration menuItems
        adminMenu.add(addEmployeeItem);
        adminMenu.add(manageEmployeesItem);
        adminMenu.add(manageCustomersItem);
        adminMenu.add(manageAppointmentsItem);
        adminMenu.add(manageTasksItem);
        menuBar.add(adminMenu);

        // About Menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            AboutDialog about = new AboutDialog();
            about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            about.setVisible(true);
        });

        aboutMenu.add(aboutItem);
        menuBar.add(aboutMenu);
        
    }

    private void initAppointmentList() {
        appointmentListModel = new AppointmentListModel();
        JList<String> listView = new JList<>(appointmentListModel);
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listView.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			int index = listView.locationToIndex(e.getPoint());
        			if(index >= 0) {
        				Appointment appointment = appointmentListModel.getAppointmentAt(index);
        				new AppointmentDetailsDialog(appointment).setVisible(true);
        			}
        		}
        	}
        });
        getContentPane().add(new JScrollPane(listView), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DatabaseHelper(); // Set up DB

            LoginDialog loginDialog = new LoginDialog();

            if (loginDialog.isFirstRun()) {
                JOptionPane.showMessageDialog(null, "Welcome! Please create your first admin account.");
                AddEmployeeDialog setup = new AddEmployeeDialog();
                setup.setModal(true);
                setup.setVisible(true);
            }

            loginDialog.setVisible(true);

            if (loginDialog.wasLoginSuccessful()) {
                new MainFrame().setVisible(true);
            }
        });
    }
}
