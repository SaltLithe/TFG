
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
/*
 * Este es el primer componente de interfaz que se invoca a partir de app lo
 * unico que hace es crear una ventana y ponerle un ChooseModePanel si cada paso
 * de la interfaz esta en su propio panel es mas facil ir cambiandolos sin tocar
 * la ventana que los contiene
 */
public class InitFrame extends JFrame {

	private ChooseModePanel chooseModePanel;

	public InitFrame() {

		super("Pair Leap");
		setLayout(new BorderLayout());

		chooseModePanel = new ChooseModePanel(this);
		setSize(new Dimension(600, 600));

		add(chooseModePanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
		DEBUG.debugmessage("AÑADIDO EL PANEL DE SELECCIÓN DE MODO");

	}

}
