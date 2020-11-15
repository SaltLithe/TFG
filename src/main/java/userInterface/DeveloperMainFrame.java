package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import userInterface.fileNavigation.FileExplorerToolbar;
import userInterface.textEditing.TextEditorPanel;
import userInterface.textEditing.TextEditorToolbar;

/*Clase que contiene todos los elementos necesarios para la interfaz principal de la aplicacion
 * Consiste en un frame que contiene todos los componentes necesarios y sus correspondientes separaciones
 * por motivos de flexibilidad y orden los componentes se declaran en su mayoría en sus propias clases
 * En esta clase es donde se crea un objeto de la clase developerComponent , que es la que lleva la gran mayoría
 * de las funcionalidades de la aplicación
 */

@SuppressWarnings("serial")
public class DeveloperMainFrame extends JFrame {

	TextEditorToolbar textEditorToolbar;
	private Dimension screenSize;
	FileExplorerToolbar fileExplorerToolbar;
	UsersPanel usersPanel;
	MenuToolbar menuToolbar;
	TextEditorPanel textEditorPanel;
	ConsolePanel consolePanel;
	JSplitPane explorerDivision;
	JSplitPane usersDivision;

	private UIController controller;

	// Metodo para activar la barra de exploración de archivos , se le llamará
	// cuando se haya abierto una carpeta
	public void enableFileExplorerToolbar() {

		//fileExplorerToolbar.enableToolbarButtons();

	}

	DeveloperMainFrame() {

		super("Pair Leap");

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		this.controller = UIController.getInstance();
		controller.setDeveloperMainFrame(this);

		// Inicializo lo que lleva toda la funcionalidad
		// Tomo el tamaño de la pantalla y pongo el layout
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLayout(new BorderLayout());

		// Creo y configuro todos los elementos de mi interfaz

		// Cambiar esto por dios
		textEditorToolbar = new TextEditorToolbar(this);

		fileExplorerToolbar = new FileExplorerToolbar(this);
		textEditorPanel = textEditorToolbar.textEditorPanel;
		fileExplorerToolbar.setPreferredSize(new Dimension(ImageObserver.HEIGHT, 500));
		menuToolbar = new MenuToolbar(this);

		usersPanel = new UsersPanel();

		explorerDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileExplorerToolbar, textEditorToolbar);
		explorerDivision.setDividerLocation(200);

		usersDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, explorerDivision, usersPanel);
		usersDivision.setDividerLocation(1200);

		// Añado estos elementos
		add(usersDivision);
		add(menuToolbar, BorderLayout.NORTH);
		usersPanel.updateUI();

		// Pongo el tamaño de ventana, configuro el comportamiento de la ventana al
		// cerrar y la hago visible
		setSize(screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// Metodo que actualiza las divisiones que contienen algun elemento de interfaz
	// que pueda cambiar
	// en el futuro para que los cambios sean visibles
	// en este caso son divisiones pues estas son el contenedor final de los
	// elementos
	public void updateAllSplits() {

		usersDivision.updateUI();
		explorerDivision.updateUI();

	}

	// Metodo que sirve para poner en el panel de usuarios al dueño del servidor de
	// esta sesión
	public void setSessionOwner(String name) {
		usersPanel.setSessionOwner(name);

	}

	// Método que sirve para poner en el panel de usuarios la ip del dueño del
	// servidor de esta sesión
	// Esto es útil para que el dueño y los clientes puedan saber siempre la ip y
	// conectarse de nuevo o dársela
	// a otra persona para que se conecte
	public void setIpIndicator(String remoteaddr) {
		usersPanel.setIpIndicator(remoteaddr);
	}

	public String getEditorPanelContents() {
		return textEditorToolbar.getContents();

	}

}
