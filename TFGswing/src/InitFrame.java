

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

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

	}

}
