/*
 * Dialog to display information about application
 */
package scheduler.view;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Toolkit;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setTitle("About");
		
		setBounds(100, 100, 197, 133);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.CENTER);
		{
			JTextArea txtrApplication = new JTextArea();
			txtrApplication.setText("Scheduler Application\r\nAuthor: Chris King\r\nVersion 1.0");
			contentPanel.add(txtrApplication);
		}
		
	}

}
