package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import core.DEBUG;
import userInterface.fileNavigation.FileExplorerToolbar;
import userInterface.networkManagement.UsersPanel;
import userInterface.textEditing.TextEditorContainer;
import userInterface.textEditing.TextEditorPanel;

/*Clase que contiene todos los elementos necesarios para la interfaz principal de la aplicacion
 * Consiste en un frame que contiene todos los componentes necesarios y sus correspondientes separaciones
 * por motivos de flexibilidad y orden los componentes se declaran en su mayoría en sus propias clases
 * En esta clase es donde se crea un objeto de la clase developerComponent , que es la que lleva la gran mayoría
 * de las funcionalidades de la aplicación
 */

@SuppressWarnings("serial")
public class DeveloperMainFrame extends JFrame implements PropertyChangeListener {

	TextEditorContainer textEditorContainer;
	private Dimension screenSize;
	FileExplorerToolbar fileExplorerToolbar;
	UsersPanel usersPanel;
	MenuToolbar menuToolbar;
	TextEditorPanel textEditorPanel;
	ConsolePanel consolePanel;
	JSplitPane explorerDivision;
	JSplitPane usersDivision;
	private PropertyChangeMessenger support; 
	private PrintStream stdout;
	private InputStream stdin;
	private PrintStream stderr; 
	private JFrame instance; 
	private boolean somethingHasChanged;
	private UIController controller;
	// Metodo para activar la barra de exploración de archivos , se le llamará
	// cuando se haya abierto una carpeta


	DeveloperMainFrame() {

		super("Pair Leap");
		support= PropertyChangeMessenger.getInstance();
		instance = this; 
		somethingHasChanged = false; 
		
		stdin = System.in; 
		stdout = System.out;
		stderr = System.err; 

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
		textEditorContainer = new TextEditorContainer(this);

		fileExplorerToolbar = new FileExplorerToolbar(this);
		textEditorPanel = textEditorContainer.textEditorPanel;
		fileExplorerToolbar.setPreferredSize(new Dimension(ImageObserver.HEIGHT, 500));
		menuToolbar = new MenuToolbar(this,textEditorContainer);

		usersPanel = new UsersPanel();

		explorerDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileExplorerToolbar, textEditorContainer);
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
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        // call terminate
		    	/*
		    	DEBUG.unsetExecuting();
		    	System.setOut(stdout);
				System.setErr(stderr);
				System.setIn(stdin);
				*/
		    	support.notify(ObserverActions.SAFETY_STOP,null,null);
		    	support.notify(ObserverActions.SAFETY_DELETE,null , null);
		    	
		    	if (somethingHasChanged) {
		    	 int result = JOptionPane.showConfirmDialog(instance,"You left some unsaved changes. Would you like to do a full save of all of your progress before closing?",
		                 "Closing PairLeap", JOptionPane.YES_NO_CANCEL_OPTION,
		                 JOptionPane.QUESTION_MESSAGE);
		    	if(result == JOptionPane.YES_OPTION) {
		    	support.notify(ObserverActions.SAFETY_SAVE,null,null);
		    	}
		    	}
		    	 dispose();
		    	 System.exit(0);
		    }
		});
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


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		
		case SET_SAVE_FLAG_TRUE:
			somethingHasChanged = true; 
			DEBUG.debugmessage("YES CHANGES");

			break;
		
		case SET_SAVE_FLAG_FALSE:
			DEBUG.debugmessage("NO CHANGES");
			somethingHasChanged = false; 
			break;
		
		default:
			break;
		
		}
		
		
	}

	

}
