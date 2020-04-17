
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import ui.UsersPanel;

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

	public void enableFileExplorerToolbar() {

		fileExplorerToolbar.enableToolbarButtons();

	}

	public DeveloperMainFrame(Socket client, NioSocketServer server) {

		super("Pair Leap");

		try {
			developerComponent = new DeveloperComponent(this, client, server);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Inicializo lo que lleva toda la funcionalidad

		// Tomo el tamaño de la pantalla y pongo el layout
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

		// Añado estos elementos
		add(usersDivision);
		add(menuToolbar, BorderLayout.NORTH);

		// Pongo el tamaño de ventana, configuro el comportamiento de la ventana al
		// cerrar y la hago visible
		setSize(screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// Metodo que actualiza las divisiones que contienen los elementos
	// que pueden cambiar para que se vean los cambios
	public void updateAllSplits() {

		usersDivision.updateUI();
		explorerDivision.updateUI();

	}

	public TextEditorToolbar getTextEditorToolbar() {
		// TODO Auto-generated method stub

		return textEditorToolbar;
	}

	public void setSessionOwner(String name) {
		usersPanel.setSessionOwner(name);
		// TODO Auto-generated method stub

	}

	public void setIpIndicator(String remoteaddr) {
		usersPanel.setIpIndicator(remoteaddr);
		// TODO Auto-generated method stub

	}

}
