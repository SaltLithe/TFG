
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.JoinSessionPanel;

public class ChooseModePanel extends JPanel {

	private JButton create;
	private JButton join;
	private CreateSessionPanel createSessionPanel;
	private JoinSessionPanel joinSessionPanel;

	public ChooseModePanel(JFrame frame) {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		create = new JButton("Create Session");
		join = new JButton("Join Session");

		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				createSessionPanel = new CreateSessionPanel(frame);
				this.deleteAll();
				add(createSessionPanel, BorderLayout.CENTER);
				updateUI();
			}

			private void deleteAll() {
				remove(join);
				remove(create);
			}

		});

		join.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				joinSessionPanel = new JoinSessionPanel();
				this.deleteAll();
				add(joinSessionPanel, BorderLayout.CENTER);
				updateUI();
			}

			private void deleteAll() {
				remove(join);
				remove(create);
			}

		});

		add(create);
		add(join);

	}

}
