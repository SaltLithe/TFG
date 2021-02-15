package userInterface.fileEditing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.DEBUG;
import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import userInterface.fileNavigation.workSpaceSelect;

/**
 * Class used to pop a dialog for the user to add a new workspace
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings({ "serial", "unused" })
public class AddWorkSpaceDialog extends JDialog {
	private JTextField nameField;
	private JTextField pathField;
	private workSpaceSelect self;
	private WorkSpaceManager wsm;
	private JButton okButton;
	private JButton cancelButton;
	private JButton BrowseButton;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;

	public AddWorkSpaceDialog(workSpaceSelect self) {

		this.setTitle("Add a new workspace");
		try {
			this.setIconImage(ImageIO.read((workSpaceSelect.class.getResource("/resources/images/window_Icon.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		this.self = self;
		wsm = WorkSpaceManager.getInstance();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 58, 21, 359, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 37, 52, 42, 22, -6, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel(" New WorkSpace");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.anchor = GridBagConstraints.EAST;
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 1;
		getContentPane().add(panel_4, gbc_panel_4);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel_1 = new JLabel(" Name");
		panel_4.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.gridx = 2;
		gbc_panel_2.gridy = 1;
		getContentPane().add(panel_2, gbc_panel_2);
		
				nameField = new JTextField();
				nameField.setBounds(0, 11, 278, 20);
				panel_2.add(nameField);
				nameField.setColumns(30);
				nameField.setDisabledTextColor(Color.white);
				nameField.setEnabled(false);

		panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.NORTHEAST;
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 2;
		getContentPane().add(panel_3, gbc_panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel_2 = new JLabel(" Path");
		panel_3.add(lblNewLabel_2);

		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 2;
		getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		pathField = new JTextField();
		pathField.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(pathField);
		Dimension minSize = new Dimension(5, 0);
		Dimension prefSize = new Dimension(5, 0);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 0);
		panel_1.add(new Box.Filler(minSize, prefSize, maxSize));
		pathField.setColumns(30);

		BrowseButton = new JButton("Browse");
		panel_1.add(BrowseButton);

		BrowseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String path = wsm.getFilePath();
				if (path != null) {

					String search = "\\";
					int index = path.lastIndexOf(search);
					String subname = path.substring(index + 1, path.length());
					DEBUG.debugmessage(index + " " + subname);

					pathField.setText(path);

					nameField.setText(subname);
				}

			}

		});

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 4;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{47, 65, 0};
		gbl_panel.rowHeights = new int[]{23, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
						
								okButton = new JButton("OK");
								okButton.setPreferredSize(BrowseButton.getPreferredSize());
								
										GridBagConstraints gbc_okButton = new GridBagConstraints();
										gbc_okButton.anchor = GridBagConstraints.WEST;
										gbc_okButton.insets = new Insets(0, 0, 0, 5);
										gbc_okButton.gridx = 0;
										gbc_okButton.gridy = 0;
										panel.add(okButton, gbc_okButton);
										okButton.setVerticalAlignment(SwingConstants.BOTTOM);
										okButton.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent e) {

												WorkSpace ws = new WorkSpace();
												ws.setName(nameField.getText());
												ws.setPath(pathField.getText());
												boolean result = wsm.addWorkSpace(ws, self);
												if (result) {

													dispose();

												}

											}

										});
						cancelButton = new JButton("Cancel");
						GridBagConstraints gbc_cancelButton = new GridBagConstraints();
						gbc_cancelButton.anchor = GridBagConstraints.WEST;
						gbc_cancelButton.gridx = 1;
						gbc_cancelButton.gridy = 0;
						panel.add(cancelButton, gbc_cancelButton);
						cancelButton.setPreferredSize(BrowseButton.getPreferredSize());
						cancelButton.setVerticalAlignment(SwingConstants.BOTTOM);
						
								cancelButton.addActionListener(new ActionListener() {
						
									@Override
									public void actionPerformed(ActionEvent e) {
						
										dispose();
						
									}
						
								});
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setSize(this.getPreferredSize());
		this.setSize(455,225);
	}

}
