package userInterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;

import core.DEBUG;
import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import fileManagement.customWorkSpaceElement;

public class workSpaceSelect extends JFrame {

	private JPanel contentPane;
	LinkedList<Component> selectPanelComponents;
	private JLabel lblNewLabel;
	private JButton newWorkSpaceButton;
	private JScrollPane panel;
	private boolean canUpdate = false;
	
	Dimension minSize = new Dimension(0, 0);
	Dimension prefSize = new Dimension(0, 0);
	Dimension maxSize = new Dimension(0, 0);

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */

	private void readAndGenerate() {
		panel.add(new Box.Filler(minSize, prefSize, maxSize));

		ArrayList<WorkSpace> ws = (ArrayList<WorkSpace>) WorkSpaceManager.getAllWorkSpaces();
		if (ws != null) {
			for (WorkSpace workspace : ws) {
				customWorkSpaceElement cwse = new customWorkSpaceElement(workspace.getName(), workspace.getPath());
				cwse.setAlignmentY(Component.TOP_ALIGNMENT);
				cwse.setAlignmentX(Component.LEFT_ALIGNMENT);

				panel.add(cwse);
				
				panel.add(new Box.Filler(minSize, prefSize, maxSize));

			}

		}

	}

	public workSpaceSelect() {

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
//aaaaç
		selectPanelComponents = new LinkedList<Component>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 85, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 21, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblNewLabel = new JLabel("LAUNCH");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		newWorkSpaceButton = new JButton("New WorkSpace");
		newWorkSpaceButton.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		contentPane.add(newWorkSpaceButton, gbc_btnNewButton);

		panel = new JScrollPane(); 
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		readAndGenerate();

		
		
		newWorkSpaceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
				DEBUG.debugmessage("SET TO TRUE");
				openAddWorkSpaceDialog();
				
				

			}

			

		});

		setSize(600, 600);
		setResizable(false);

		this.setVisible(true);

	}
	private void openAddWorkSpaceDialog() {
		AddWorkSpaceDialog d = new AddWorkSpaceDialog(this);
		d.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosed(WindowEvent e) {
		    	refresh(); 
		    }
		});
		d.setVisible(true);
		
	}

	private void refresh() {
		DEBUG.debugmessage("Refreshing");
		panel.removeAll();
		
		readAndGenerate(); 
		
		panel.updateUI();
	
	}


}
