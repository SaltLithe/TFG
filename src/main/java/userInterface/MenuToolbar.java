package userInterface;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.WorkSpace;
import userInterface.fileEditing.newProjectDialog;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import java.awt.Component;

/*Clase que contiene todos los botones del menu superior de la aplicacion y que implementa sus 
 * comportamientos
 */
@SuppressWarnings("serial")
public class MenuToolbar extends JPanel implements PropertyChangeListener {
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private DeveloperMainFrame developerMainFrame;
	private JMenuBar menuBar;

	JMenuItem saveMenuItem;
	JMenuItem saveAllMenuItem;
	
	JButton save;
	JButton saveAll;
	JMenuItem newProjectMenuItem;
	private void enableSaveButtons() {

		save.setEnabled(true);
		saveAll.setEnabled(true);
		
		saveMenuItem.setEnabled(true);
		saveAllMenuItem.setEnabled(true);
	}

	public MenuToolbar(DeveloperMainFrame developerMainFrame) {
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE MENUTOOLBAR");
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		this.developerMainFrame = developerMainFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		menuBar = new JMenuBar();
		menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setAlignmentY(Component.TOP_ALIGNMENT);
		mnNewMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
		menuBar.add(mnNewMenu);
		
		newProjectMenuItem = new JMenuItem("New Project");
		mnNewMenu.add(newProjectMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Open Project");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		saveMenuItem = new JMenuItem("Save");
		mnNewMenu.add(saveMenuItem);
		
		saveAllMenuItem = new JMenuItem("Save all");
		mnNewMenu.add(saveAllMenuItem);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(toolBar);
		
		 save = new JButton("Save");
		toolBar.add(save);
		
		 saveAll = new JButton("Save All");
		toolBar.add(saveAll);
		
		
		newProjectMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newProjectDialog d = new newProjectDialog(uiController , developerComponent);

			}

		});
		
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				save(); 

			}

		});
		
		

		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
					saveAll(); 
			}

		});
		
		saveAllMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
					saveAll(); 
			}

		});
		
		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
					save(); 
			}

		});
		
	menuBar.setVisible(true);
		//this.setSize(this.getPreferredSize());
		this.setVisible(true);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		@SuppressWarnings("unchecked")
		ArrayList<Object> eventlist = (ArrayList<Object>) evt.getNewValue();
		ObserverActions action = (ObserverActions) eventlist.get(0);
		switch (action) {
		case ENABLE_SAVE_BUTTONS:
			enableSaveButtons();
			break;
		default:
			break;
		}

	}
	
	private void save() {
		String contents = developerMainFrame.getEditorPanelContents();
		uiController.run(() -> {
			try {
				developerComponent.saveCurrentFile(contents, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	
	public void saveAll() {
		DEBUG.debugmessage("SE HA PULSADO EL BOTON SAVEALL");
		String contents = developerMainFrame.getEditorPanelContents();
		uiController.run(() -> {
			try {
				developerComponent.saveAllFiles(contents);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
}
