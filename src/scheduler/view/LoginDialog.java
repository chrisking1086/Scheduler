package scheduler.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import scheduler.controller.LoginService;

public class LoginDialog extends JDialog {

	
    private static final long serialVersionUID = 1L;

    
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginDialog() {
    	try {
    	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	}
        setTitle("Login");
        setModal(true);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Username:"));
        usernameField = new JTextField();
        form.add(usernameField);

        form.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        form.add(passwordField);

        getContentPane().add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        loginButton.addActionListener(this::handleLogin);
        cancelButton.addActionListener(e -> dispose());

        buttons.add(loginButton);
        buttons.add(cancelButton);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoginService loginService = new LoginService();
        boolean success = loginService.authenticateUser(username, password);
        

        if (success) {
        	loginSuccessful = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isFirstRun() {
        String sql = "SELECT COUNT(*) FROM employees";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:scheduler.db");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            return rs.next() && rs.getInt(1) == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return true; // assume first run on error
        }
    }
    
    private boolean loginSuccessful = false;

    public boolean wasLoginSuccessful() {
        return loginSuccessful;
    }
}
