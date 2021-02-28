package userInterface.uiFileNavigation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

import userInterface.uiFileEditing.AddWorkSpaceDialog;
import userInterface.uiFileManagement.CustomWorkSpaceElement;
import userInterface.uiFileManagement.WorkSpace;
import userInterface.uiFileManagement.WorkSpaceManager;

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
	private JPanel panel_1;

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
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblNewLabel = new JLabel("LAUNCH");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
				panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
				newWorkSpaceButton = new JButton("New WorkSpace");
				panel_1.add(newWorkSpaceButton);
				
						noWorkSpaceButton = new JButton("Start without WorkSpace");
						panel_1.add(noWorkSpaceButton);
						newWorkSpaceButton.setSize(noWorkSpaceButton.getSize());
						
								noWorkSpaceButton.addActionListener(new ActionListener() {
						
									@Override
									public void actionPerformed(ActionEvent e) {
						
										WorkSpaceManager.getInstance().startMainApp();
										dispose();
						
									}
						
								});
				
						newWorkSpaceButton.addActionListener(new ActionListener() {
				
							@Override
							public void actionPerformed(ActionEvent e) {
				
								openAddWorkSpaceDialog();
				
							}
				
						});

		panel = new JPanel();
		scrollPanel = new JScrollPane(panel);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setSize(panel.getPreferredSize());

		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.contentPane.add(scrollPanel, gbc_panel);
		readAndGenerate();


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
