
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class FileExplorerToolbar extends JPanel {
//
	private FileExplorerPanel fileExplorerPanel;

	private JButton addClass;
	private JButton addScript;
	private DeveloperComponent developerComponent;
	private TextEditorToolbar textEditorToolbar;

	public FileExplorerToolbar(DeveloperComponent dp, TextEditorToolbar tet) {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEEXPLORERTOOLBAR");
		fileExplorerPanel = new FileExplorerPanel();
		textEditorToolbar = tet;
		developerComponent = dp;

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
							developerComponent.createNewClassFile(nombrerecibido, null);
							createFileButton(nombrerecibido);

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
	public void createFileButton(String name) {

		if (!fileExplorerPanel.containsButton(name)) {
			JButton newbutton = new JButton(name);

			newbutton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					String newcontents = developerComponent.openFile(name, textEditorToolbar.getContents());
					textEditorToolbar.setContents(newcontents, name);
				}

			});

			fileExplorerPanel.addButton(newbutton);
			this.updateUI();
		}
	}

	// Método para habilitar los botones de este panel una vez se abra una carpeta
	// para trabajar
	public void enableToolbarButtons() {

		addClass.setEnabled(true);
		addScript.setEnabled(true);

	}

	// Metodo que recibe todos los archivos existentes y llama al metodo de crear
	// botón para cada uno de ellos
	public void updateAllButtons(File[] ficheros) {
		for (int i = 0; i < ficheros.length; i++) {

			String nombreboton = ficheros[i].getName().replace(".java", "");
			this.createFileButton(nombreboton);

		}

	}

	// Método para habilitar el área de edición de texto
	public void enableTextEditor() {
		textEditorToolbar.enableEditor();
		textEditorToolbar.enableButtons();

	}

}
