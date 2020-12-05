package userInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.fileEditing.newProjectDialog;
import userInterface.textEditing.TextEditorContainer;

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
	private JButton runGlobalButton;
	private JButton terminateProcessButton;
	private JButton runLocalButton;
	
	private TextEditorContainer textEditorContainer;
	

	private void enableSaveButtons() {

		save.setEnabled(true);
		saveAll.setEnabled(true);
		
		saveMenuItem.setEnabled(true);
		saveAllMenuItem.setEnabled(true);
	}

	public MenuToolbar(DeveloperMainFrame developerMainFrame , TextEditorContainer textEditorContainer) {
		this.textEditorContainer = textEditorContainer;
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
		 save.setIcon(new ImageIcon("Icons\\save_icon.png"));
		toolBar.add(save);
		
		 saveAll = new JButton("Save All");
		 saveAll.setIcon(new ImageIcon("Icons\\saveAll_icon.png"));
		toolBar.add(saveAll);
		
		runLocalButton = new JButton("Run Locally");
		runLocalButton.setIcon(new ImageIcon("Icons\\runLocal_icon.png"));
		toolBar.add(runLocalButton);
		
		runGlobalButton = new JButton("Run Globally");
		runGlobalButton.setIcon(new ImageIcon("Icons\\runGlobal_icon.png"));
		toolBar.add(runGlobalButton);
		
		terminateProcessButton = new JButton("Terminate Run");
		terminateProcessButton.setIcon(new ImageIcon("Icons\\terminateProcess_icon.png"));
		toolBar.add(terminateProcessButton);
		
		
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
		
		
		
		runLocalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAll();
				developerComponent.startLocalRunningThread(); 
			

			

			}

		});
		
		runGlobalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
		//TO-DO RUN GLOBAL 

			}

		});
		terminateProcessButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				developerComponent.terminateProcess();

			}

		});

		disableTerminate();
		disableGlobalRun();
		menuBar.setVisible(true);
		this.setVisible(true);

	}
	
	private void enableTerminate() {
		
		terminateProcessButton.setEnabled(true);

		
	}

	private void disableTerminate() {
		
		terminateProcessButton.setEnabled(false);

		
	}
	
	private void enableLocalRun() {
		
		runLocalButton.setEnabled(true);
		
	}
	
	private void disableLocalRun() {
		
		runLocalButton.setEnabled(false);

		
	}
	private void disableGlobalRun() {
		runGlobalButton.setEnabled(false);
	}
	
	private void enableGlobalRun() {
		runGlobalButton.setEnabled(true);
	}
	
	
	private void terminateProcess() {
		developerComponent.terminateProcess(); 
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		
		case DISABLE_TERMINATE:
			disableTerminate();
			break;
		case ENABLE_TERMINATE:
			enableTerminate(); 
			break;
		case DISABLE_LOCAL_RUN:
			DEBUG.debugmessage("Coming through");
			disableLocalRun();
			break;
		case ENABLE_LOCAL_RUN:
			enableLocalRun();
			break;
		case SAFETY_STOP:
			terminateProcess(); 
			break;
		case SAFETY_SAVE:
			saveAll(); 
			break;
	 
		default:
			break;
		}

	}
	

	
	private void save() {
		

		if(textEditorContainer.getCurrentTabName() != null && textEditorContainer.getContents() != null) {
		try {
			developerComponent.saveCurrentFile(textEditorContainer.getCurrentTabName(), textEditorContainer.getContents());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		}
		
	}
	
	private void saveAll() {
		DEBUG.debugmessage("SE HA PULSADO EL BOTON SAVEALL");
		String[] contents = textEditorContainer.getAllContents(); 
		String[] names = textEditorContainer.getAllNames(); 
		if(names.length != 0 && contents.length != 0) {
		
				try {
					developerComponent.saveAllFiles(names,contents);
				} catch (IOException e) {
					e.printStackTrace();
				}
		
		
	}
	}
}
