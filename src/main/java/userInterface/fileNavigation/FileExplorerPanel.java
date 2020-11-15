package userInterface.fileNavigation;


import javax.swing.BoxLayout;
import javax.swing.JPanel;

import core.DEBUG;

@SuppressWarnings("serial")
public

/*
 * Clase que implementa el panel que contiene todos los botones necesarios para
 * cambiar de archivos en el editor , sus únicos comportamientos son añadir los
 * botones que se le indica e indicar si un boton a añadir existe para que no
 * sea duplicado
 */
class FileExplorerPanel extends JPanel {


	public FileExplorerPanel() {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEEXPLORERPANEL");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setVisible(true);

	}

	
}
