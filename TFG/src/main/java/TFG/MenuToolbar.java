package TFG;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

/*Clase que contiene todos los botones del menu superior de la aplicacion y que implementa sus 
 * comportamientos
 */
@SuppressWarnings("serial")
public class MenuToolbar extends JPanel {

	private JButton folder;
	private JButton save;
	private JButton saveAll;
	private FileExplorerToolbar fileExplorerToolbar;
	private DeveloperComponent developerComponent;

	public MenuToolbar(DeveloperComponent dc, FileExplorerToolbar fet, TextEditorToolbar textEditorToolbar) {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE MENUTOOLBAR");
		developerComponent = dc;
		fileExplorerToolbar = fet;

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
				developerComponent.selectFocusedFolder();
				fileExplorerToolbar.enableToolbarButtons();
				fileExplorerToolbar.updateAllButtons(developerComponent.getAllFiles());
				fileExplorerToolbar.enableTextEditor();
				save.setEnabled(true);
				saveAll.setEnabled(true);

			}

		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DEBUG.debugmessage("SE HA PULSADO EL BOTON SAVE");

				try {
					developerComponent.saveCurrentFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		});

		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DEBUG.debugmessage("SE HA PULSADO EL BOTON SAVEALL");
				try {
					developerComponent.saveAllFiles();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		});

	}

}
