package userInterface.fileNavigation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;

import core.DEBUG;
import fileManagement.CustomWorkSpaceElement;
import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import userInterface.fileEditing.AddWorkSpaceDialog;

@SuppressWarnings("serial")
/**
 * UI class used as an entrypoint to this program. It contains workspace
 * representation objects so the user can select , locate or delete workspaces
 * and can pop up a series of dialogs for the user to create new workspaces and
 * manage deletion and location operations
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class workSpaceSelect extends JFrame {

	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JButton newWorkSpaceButton;
	private JPanel panel;
	private WorkSpaceManager wsm;
	@SuppressWarnings("unused")
	private JFrame self;
	private JScrollPane scrollPanel;
	private JButton noWorkSpaceButton;
	Dimension minSize = new Dimension(0, 0);
	Dimension prefSize = new Dimension(0, 0);
	Dimension maxSize = new Dimension(0, 0);
	LinkedList<Component> selectPanelComponents;

	public workSpaceSelect() {
		
		
		this.setTitle("Select a workspace");
		try {
			this.setIconImage(ImageIO.read((workSpaceSelect.class.getResource("/resources/images/window_Icon.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}



		self = this;
		wsm = WorkSpaceManager.getInstance();

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
		}

		selectPanelComponents = new LinkedList<>();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

		noWorkSpaceButton = new JButton("Start without WorkSpace");

		GridBagConstraints gbc_noWorkspaceButton = new GridBagConstraints();
		gbc_noWorkspaceButton.anchor = GridBagConstraints.WEST;
		gbc_noWorkspaceButton.insets = new Insets(0, 0, 5, 0);
		gbc_noWorkspaceButton.gridx = 0;
		gbc_noWorkspaceButton.gridy = 1;
		contentPane.add(noWorkSpaceButton, gbc_noWorkspaceButton);

		newWorkSpaceButton = new JButton("New WorkSpace");
		newWorkSpaceButton.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnNewButton1 = new GridBagConstraints();
		gbc_btnNewButton1.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton1.gridx = 0;
		gbc_btnNewButton1.gridy = 2;
		contentPane.add(newWorkSpaceButton, gbc_btnNewButton1);

		panel = new JPanel();
		scrollPanel = new JScrollPane(panel);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setSize(panel.getPreferredSize());

		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.contentPane.add(scrollPanel, gbc_panel);
		readAndGenerate();

		newWorkSpaceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DEBUG.debugmessage("SET TO TRUE");
				openAddWorkSpaceDialog();

			}

		});

		noWorkSpaceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				WorkSpaceManager.getInstance().startMainApp();
				dispose();

			}

		});

		setSize(500, 500);
		setResizable(false);

		this.setVisible(true);

	}

	/**
	 * Method used to refresh this frame
	 */
	public void refresh() {
		panel.removeAll();

		readAndGenerate();

		panel.updateUI();

	}

	/**
	 * Method called to read from the xml list to look for workspaces and create
	 * their visual representations in order to add them to the panel
	 */
	private void readAndGenerate() {

		panel.add(new Box.Filler(minSize, prefSize, maxSize));

		ArrayList<WorkSpace> ws = (ArrayList<WorkSpace>) wsm.getAllWorkSpaces();
		if (ws != null) {
			for (int i = 0; i < ws.size(); i++) {
				WorkSpace workspace = ws.get(i);
				CustomWorkSpaceElement cwse = new CustomWorkSpaceElement(workspace.getName(), workspace.getPath(),
						this);
				cwse.tempID = i;
				cwse.setAlignmentY(Component.TOP_ALIGNMENT);
				cwse.setAlignmentX(Component.LEFT_ALIGNMENT);

				panel.add(cwse);

				panel.add(new Box.Filler(minSize, prefSize, maxSize));

			}

		}

	}

	/**
	 * Called to pop open a dialog for the user to add a workspace
	 */
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

}
