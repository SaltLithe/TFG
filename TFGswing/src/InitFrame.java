
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class InitFrame extends JFrame {

	private ChooseModePanel chooseModePanel;

	public InitFrame() {

		// Creates the first interface
		super("Pair Leap");
		setLayout(new BorderLayout());

		// Adds the first panel that contains all the functional elements
		chooseModePanel = new ChooseModePanel(this);
		setSize(new Dimension(600, 600));

		add(chooseModePanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

	}

}
