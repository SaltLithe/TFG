
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import ui.UsersPanel;

/*Clase que contiene todos los elementos necesarios para la interfaz principal de la aplicacion
 * Consiste en un frame que contiene todos los componentes necesarios y sus correspondientes separaciones
 * por motivos de flexibilidad y orden los componentes se declaran en su mayor�a en sus propias clases
 * En esta clase es donde se crea un objeto de la clase developerComponent , que es la que lleva la gran mayor�a
 * de las funcionalidades de la aplicaci�n
 */

@SuppressWarnings("serial")
public class DeveloperMainFrame extends JFrame {

	private TextEditorToolbar textEditorToolbar;
	private Dimension screenSize;
	private FileExplorerToolbar fileExplorerToolbar;
	private UsersPanel usersPanel;
	private MenuToolbar menuToolbar;

	private JSplitPane explorerDivision;
	private JSplitPane usersDivision;

	private DeveloperComponent developerComponent;

	// Metodo para activar la barra de exploraci�n de archivos , se le llamar�
	// cuando se haya abierto una carpeta
	public void enableFileExplorerToolbar() {

		fileExplorerToolbar.enableToolbarButtons();

	}

	public DeveloperMainFrame(Socket client, ServerComponent server) {

		super("Pair Leap");

		DEBUG.debugmessage("SE HA INVOCADO EL CONSTRUCTOR DE DEVELOPERMAINFRAME");
		try {
			developerComponent = new DeveloperComponent(this, client, server);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Inicializo lo que lleva toda la funcionalidad
		// Tomo el tama�o de la pantalla y pongo el layout
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLayout(new BorderLayout());

		// Creo y configuro todos los elementos de mi interfaz

		textEditorToolbar = new TextEditorToolbar(developerComponent);
		developerComponent.setTextEditorToolbar(textEditorToolbar);
		fileExplorerToolbar = new FileExplorerToolbar(developerComponent, textEditorToolbar);
		fileExplorerToolbar.setPreferredSize(new Dimension(ImageObserver.HEIGHT, 500));

		menuToolbar = new MenuToolbar(developerComponent, fileExplorerToolbar, textEditorToolbar);

		usersPanel = new UsersPanel();

		explorerDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileExplorerToolbar, textEditorToolbar);
		explorerDivision.setDividerLocation(200);

		usersDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, explorerDivision, usersPanel);
		usersDivision.setDividerLocation(1200);

		// A�ado estos elementos
		add(usersDivision);
		add(menuToolbar, BorderLayout.NORTH);

		// Pongo el tama�o de ventana, configuro el comportamiento de la ventana al
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

	// Metodo que sirve para poner en el panel de usuarios al due�o del servidor de
	// esta sesi�n
	public void setSessionOwner(String name) {
		usersPanel.setSessionOwner(name);

	}

	// M�todo que sirve para poner en el panel de usuarios la ip del due�o del
	// servidor de esta sesi�n
	// Esto es �til para que el due�o y los clientes puedan saber siempre la ip y
	// conectarse de nuevo o d�rsela
	// a otra persona para que se conecte
	public void setIpIndicator(String remoteaddr) {
		usersPanel.setIpIndicator(remoteaddr);
	}

}
