package userInterface;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;

import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import fileManagement.customWorkSpaceElement;

public class workSpaceSelect extends JFrame {

	private JPanel contentPane;
	JPanel panel;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */

	private void readAndGenerate() {
		ArrayList<WorkSpace> ws = (ArrayList<WorkSpace>) WorkSpaceManager.getAllWorkSpaces();
		if (ws != null) {
			for (WorkSpace workspace : ws) {
				panel.add(new customWorkSpaceElement(workspace.getName(), workspace.getPath()));
			}

		}

	}

	public workSpaceSelect() {

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
//aaaa
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("LAUNCH");
		contentPane.add(lblNewLabel, BorderLayout.NORTH);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);

		JButton btnNewButton = new JButton("New WorkSpace");
		panel.add(btnNewButton);
		readAndGenerate();
		this.setVisible(true);

	}
}
