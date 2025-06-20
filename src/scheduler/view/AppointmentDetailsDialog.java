package scheduler.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.model.Appointment;

public class AppointmentDetailsDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AppointmentDetailsDialog(Appointment appointment) {
        setTitle("Appointment Details: " + appointment.getTitle());
        setSize(400, 280);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(new JLabel("Title: " + appointment.getTitle()));
        contentPanel.add(new JLabel("Date & Time: " + appointment.getDate()));
        contentPanel.add(new JLabel("Description: " + appointment.getDescription()));
        contentPanel.add(new JLabel("Customer ID: " + appointment.getCustomerId()));
        contentPanel.add(new JLabel("Task ID: " + appointment.getTaskId()));

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Close");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
