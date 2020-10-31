package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.Project;

@SuppressWarnings("serial")
public class FileExplorerToolbar extends JPanel implements PropertyChangeListener {
//
	public FileExplorerPanel fileExplorerPanel;

	private JButton addClass;
	private JButton addScript;
	private DeveloperComponent developerComponent;
	private UIController uiController;
	private DeveloperMainFrame developerMainFrame;

	public FileExplorerToolbar(DeveloperMainFrame developerMainFrame) {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEEXPLORERTOOLBAR");
		fileExplorerPanel = new FileExplorerPanel();
		this.developerMainFrame = developerMainFrame;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();

		addClass = new JButton("Add Class");
		addScript = new JButton("Add Script");

		JPanel toolbarspace = new JPanel();
		toolbarspace.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbarspace.add(addClass);
		toolbarspace.add(addScript);
		setLayout(new BorderLayout());
		fileExplorerPanel.setPreferredSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));
		add(toolbarspace, BorderLayout.NORTH);
		add(fileExplorerPanel, BorderLayout.CENTER);

		addClass.setEnabled(false);
		addScript.setEnabled(false);

		// Al botón de añadir clase se le asocia con la creación de un diálogo con los
		// elementos necesarios
		// para crear una nueva clase , de momento se queda en esta misma clase
		addClass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DEBUG.debugmessage("SE HA PULSADO EL BOTÓN DE CREAR CLASE");

				JDialog d = new JDialog();

				JLabel l = new JLabel("this is a dialog box");

				d.add(l);

				d.setSize(300, 300);

				d.setLayout(new BorderLayout());
				JLabel mensaje = new JLabel("Choose a class name");
				JTextField textbox = new JTextField();
				JButton ok = new JButton("Ok");

				mensaje.setSize(new Dimension(150, 50));
				ok.setSize(new Dimension(100, 50));

				d.add(mensaje, BorderLayout.NORTH);
				d.add(textbox, BorderLayout.CENTER);
				d.add(ok, BorderLayout.SOUTH);

				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String nombrerecibido = textbox.getText();
						if (nombrerecibido != null) {
							d.dispose();
						//	uiController.run(() -> developerComponent.createNewClassFile(nombrerecibido, null));
							// HARDCODING
							createFileButton(nombrerecibido, ".java");

						}

					}

				});

				d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				d.setVisible(true);

			}

		});

		addScript.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});

	}

	// Método que añade un botón al FileExplorerPanel si no existe de antemano ,
	// este método además se encarga
	// de darle su comportamiento al bóton antes de añadirlo
	public void createFileButton(String name, String extension) {

		if (!fileExplorerPanel.containsButton(name)) {
			CustomFileButton newbutton = new CustomFileButton(name);
			newbutton.setCustomName(name, extension);
			newbutton.setJButtonName();
			newbutton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					String contents = developerMainFrame.getEditorPanelContents();
					String fullName = newbutton.fullName;
					uiController.run(() -> developerComponent.openFile(name, contents));

				}

			});

			fileExplorerPanel.addButton(newbutton);
			this.updateUI();
		}
	}

	// Método para habilitar los botones de este panel una vez se abra una carpeta
	// para trabajar
	// Llamado por Menutoolbar al abrir una carpeta cambiado a observeer
	public void enableToolbarButtons() {

		addClass.setEnabled(true);
		addScript.setEnabled(true);

	}

	// Metodo que recibe todos los archivos existentes y llama al metodo de crear
	// botón para cada uno de ellos
	public void updateAllButtons(File[] ficheros) {
		for (int i = 0; i < ficheros.length; i++) {

			String nombreboton = ficheros[i].getName().replace(".java", "");
			// HARDCODING
			this.createFileButton(nombreboton, ".java");

		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		case UPDATE_FILE_EXPLORER_PANEL_BUTTONS:
			DEBUG.debugmessage("PERO POR QUE PONE 4 BOTONES ");
			@SuppressWarnings("unchecked")
			ArrayList<Object> eventList = (ArrayList<Object>) evt.getNewValue();
			File[] files = (File[]) eventList.get(0);
			for (File f : files) {
				// HARDCODING
				createFileButton(f.getName(), ".java");
			}

			break;
			
		case ADD_PROJECT_TREE:
			DEBUG.debugmessage("ADDING TREE");
			ArrayList<Object> eventList2 = (ArrayList<Object>)evt.getNewValue();
			File newProject = (File)eventList2.get(0);
			fileExplorerPanel.add(new ProjectTree(newProject));
			fileExplorerPanel.updateUI();
			
			
			
			
			break;
		default:
			break;
		}
	}

	// Método para habilitar el área de edición de texto

}
