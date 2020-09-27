package TFG;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

/*Clase que contiene todos los botones del menu superior de la aplicacion y que implementa sus 
 * comportamientos
 */
@SuppressWarnings("serial")
public class MenuToolbar extends JPanel implements PropertyChangeListener {

	private JButton folder;
	private JButton save;
	private JButton saveAll;
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private DeveloperMainFrame developerMainFrame;

	private void enableSaveButtons() {

		save.setEnabled(true);
		saveAll.setEnabled(true);
	}

	public MenuToolbar(DeveloperMainFrame developerMainFrame) {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE MENUTOOLBAR");
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		this.developerMainFrame = developerMainFrame;

		folder = new JButton("Open Folder");
		save = new JButton("Save File");
		saveAll = new JButton("Save All");

		save.setEnabled(false);
		saveAll.setEnabled(false);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(folder);
		add(save);
		add(saveAll);

		folder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DEBUG.debugmessage("SE HA PULSADO EL BOTON FOLDER");
				uiController.run(() -> developerComponent.selectFocusedFolder());
				// uiController.run(() -> developerComponent.getAllFiles());

			}

		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DEBUG.debugmessage("SE HA PULSADO EL BOTON SAVE");

				String contents = developerMainFrame.getEditorPanelContents();
				uiController.run(() -> {
					try {
						developerComponent.saveCurrentFile(contents);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});

			}

		});

		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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

		});

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
}
