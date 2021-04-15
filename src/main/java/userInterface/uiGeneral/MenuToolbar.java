package userInterface.uiGeneral;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import commandController.CommandController;
import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;
import userInterface.uiFileEditing.newProjectDialog;
import userInterface.uiTextEditing.TextEditorContainer;

/**
 * Class implementing the top bar menu of this program , contains useful
 * features such as saving saving all running code globally running code locally
 * terminating running processes run configuration creating new Projects
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class MenuToolbar extends JPanel implements PropertyChangeListener {
	JButton save;
	JButton saveAll;

	private JButton runGlobalButton;
	private JButton terminateProcessButton;
	private JButton runLocalButton;
	private TextEditorContainer textEditorContainer;
	private JButton runConfigButton;
	private JButton newProjectButton;
	private PropertyChangeMessenger support;

	/**
	 * 
	 * @param textEditorContainer The object containing the text editor interface
	 *                            classes
	 */
	public MenuToolbar(TextEditorContainer textEditorContainer) {
		support = PropertyChangeMessenger.getInstance();
		this.textEditorContainer = textEditorContainer;
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(toolBar);

		// Prepare all of the buttons
		save = new JButton("Save");
		save.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/save_icon.png")));
		toolBar.add(save);

		saveAll = new JButton("Save All");
		saveAll.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/saveAll_icon.png")));
		toolBar.add(saveAll);

		runLocalButton = new JButton("Run Locally");
		runLocalButton.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/runLocal_icon.png")));
		toolBar.add(runLocalButton);

		runGlobalButton = new JButton("Run Globally");
		runGlobalButton.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/runGlobal_icon.png")));
		toolBar.add(runGlobalButton);

		terminateProcessButton = new JButton("Terminate Run");
		terminateProcessButton.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/terminateProcess_icon.png")));
		toolBar.add(terminateProcessButton);

		runConfigButton = new JButton("Run configuration");
		runConfigButton.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/runConfig_icon.png")));
		toolBar.add(runConfigButton);

		newProjectButton = new JButton("Add Project");
		newProjectButton.setIcon(new ImageIcon(MenuToolbar.class.getResource("/resources/images/projectFolder_icon.png")));
		toolBar.add(newProjectButton);

		newProjectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new newProjectDialog();

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

		runLocalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				support.notify(ObserverActions.DISABLE_TEXT_EDITOR,null);

				saveAll();
				
				CommandController.developerComponent.startLocalRunningThread();

			}

		});

		runConfigButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				if(CommandController.developerComponent.server != null) {
				CommandController.developerComponent.triggerRunConfig(true);
				}else {
					CommandController.developerComponent.triggerRunConfig(false);

				}

			}

		});

		runGlobalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				support.notify(ObserverActions.DISABLE_TEXT_EDITOR,null);
				support.notify(ObserverActions.DISABLE_SAVE_BUTTONS,null);

				saveAll();
				disableSaveButtons();
				if(CommandController.developerComponent.server != null) {
				CommandController.developerComponent.startRunGlobal(true);


				}
				else if (CommandController.developerComponent.client != null) {
					support.notify(ObserverActions.CLEAR_CONSOLE, null);
					disableSaveButtons();
					disableGlobalRun(); 
					
					CommandController.developerComponent.requestGlobalRun(); 
				}
			}

		});
		terminateProcessButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				CommandController.developerComponent.terminateProcess();

			}

		});

		disableTerminate();
		disableGlobalRun();
		
		if(CommandController.developerComponent.workSpace == null) {
			this.disableLocalRun();
			this.disableSaveButtons();
			this.disableRunConfig();
			newProjectButton.setEnabled(false);

		}
		this.setVisible(true);

	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener in order for
	 * this class to listen to ui notifications
	 */
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
		case ENABLE_GLOBAL_RUN:
			enableGlobalRun();
			break;
		case DISABLE_GLOBAL_RUN:
			disableGlobalRun();
			break;
		case DISABLE_LOCAL_RUN:
			disableLocalRun();
			break;
		case ENABLE_LOCAL_RUN:
			enableLocalRun();
			enableRunConfig();
			break;
		case DISABLE_RUN_CONFIG:
			disableRunConfig();
			break;
		case ENABLE_SAVE_BUTTONS:
			enableRunConfig();
			enableSaveButtons();
			break;
		case DISABLE_SAVE_BUTTONS:
			disableRunConfig(); 
			disableSaveButtons();
			break;
		case SAFETY_STOP:
			terminateProcess();
			break;
		case SAFETY_SAVE:
			saveAll();
			break;
		case DISABLE_NEW_PROJECT:
			newProjectButton.setEnabled(false);
			break;
		case ENABLE_NEW_PROJECT:
			newProjectButton.setEnabled(true);
			break; 

		default:
			break;
		}

	}
	
	private void enableRunConfig() {
		runConfigButton.setEnabled(true);
		
	}
	
	private void disableRunConfig() {
		
		runConfigButton.setEnabled(false);

	}
	

	private void disableSaveButtons() {
		save.setEnabled(false);
		saveAll.setEnabled(false);
	}

	private void enableSaveButtons() {
	
		save.setEnabled(true);
		saveAll.setEnabled(true);
	
	}

	/**
	 * Support method to enable the terminate process button
	 */
	private void enableTerminate() {

		terminateProcessButton.setEnabled(true);

	}

	/**
	 * Support method to disable the terminate process button
	 */
	private void disableTerminate() {

		terminateProcessButton.setEnabled(false);

	}

	/**
	 * Support method to enable the run locally button
	 */
	private void enableLocalRun() {

		runLocalButton.setEnabled(true);

	}

	/**
	 * Support method to disable the local run button
	 */
	private void disableLocalRun() {

		runLocalButton.setEnabled(false);

	}

	/**
	 * Support method to disable the global run method
	 */
	private void disableGlobalRun() {
		runGlobalButton.setEnabled(false);
	}

	/**
	 * Support method to enable the global run method
	 */
	private void enableGlobalRun() {
		runGlobalButton.setEnabled(true);
	}

	/**
	 * Support method to terminate the running process
	 */
	private void terminateProcess() {
		CommandController.developerComponent.terminateProcess();
	}

	/**
	 * Support method to save the current focused file
	 */
	private void save() {

		if (textEditorContainer.getCurrentTabName() != null && textEditorContainer.getContents() != null) {
			try {
				CommandController.developerComponent.saveCurrentFile(textEditorContainer.getCurrentTabName(),
						textEditorContainer.getContents());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Support method to save all files
	 */
	private void saveAll() {
		String[] contents = textEditorContainer.getAllContents();
		String[] names = textEditorContainer.getAllNames();
		if (names.length != 0 && contents.length != 0) {

		
				CommandController.developerComponent.saveAllFiles(names, contents);
		
		}
	}
}
