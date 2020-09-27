package TFG;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChooseModePanel extends JPanel {

	private JButton create;
	private JButton join;
	private CreateSessionPanel createSessionPanel;
	private JoinSessionPanel joinSessionPanel;

	public ChooseModePanel(JFrame frame) {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		create = new JButton("Create Session");
		join = new JButton("Join Session");

		/*
		 * Este panel contiene referencias a ambos tipos de paneles de sesión (creación
		 * y unirse) Según el botón que le des borra los elementos previos (los botones)
		 * y añade el panel de sesión correspondiente , luego actualiza la ui
		 */

		// Metodo para añadir un panel de creacion de sesion
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DEBUG.debugmessage("SE HA ELEGIDO CREAR SESION");
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

		// Metodo para añadir un panel de unirse a sesion
		join.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DEBUG.debugmessage("SE HA ELEGIDO UNIRSE A UNA SESION");
				joinSessionPanel = new JoinSessionPanel(frame);
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
