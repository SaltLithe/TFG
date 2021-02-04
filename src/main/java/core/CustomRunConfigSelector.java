package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

import userInterface.RunConfigDialog;

@SuppressWarnings("serial")
/**
 * Class used to display a dialog option used to configure a project's
 * entrypoint class This class contains an element representing a class from the
 * project for the user to choose
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class CustomRunConfigSelector extends JPanel {

	public String name;
	public RunConfigDialog componentParent;
	JLabel nameLabel;

	/**
	 * 
	 * @param name   : The name representing of the class this element represents
	 * @param parent : The dialog that is using this class in a list of selectable
	 *               class files
	 */
	public CustomRunConfigSelector(String name, RunConfigDialog parent) {

		this.componentParent = parent;
		// We use a flowlayout to ensure this element is displayed properly in the
		// dialog that creates it
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		// We create a label to display the name of the class this element represents
		this.name = name;
		nameLabel = new JLabel(name);
		nameLabel.setForeground(Color.white);
		add(nameLabel);

		setMinimumSize(new Dimension(parent.getWidth(), 25));
		setPreferredSize(new Dimension(parent.getWidth(), 25));
		setMaximumSize(new Dimension(parent.getWidth(), 25));

		setOpaque(false);

		// We add a mouse listener to detect when the user has clicked on this element
		// We only need one of the method the mouselistener has
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent evt) {

				CustomRunConfigSelector component = (CustomRunConfigSelector) evt.getComponent();
				component.componentParent.clickedOption(name);

			}

			@Override
			public void mouseEntered(MouseEvent evt) {
				// We will not be implementing methods that we don't need but we still need to
				// override them

			}

			@Override
			public void mouseExited(MouseEvent evt) {
				// We will not be implementing methods that we don't need but we still need to
				// override them

			}

			@Override
			public void mousePressed(MouseEvent evt) {
				// We will not be implementing methods that we don't need but we still need to
				// override them

			}

			@Override
			public void mouseReleased(MouseEvent evt) {
				// We will not be implementing methods that we don't need but we still need to
				// override them

			}

		});

	}

	/**
	 * Method used to indicate that the user has clicked on another element , we use
	 * this to set the look of this component to default
	 */
	public void setUnselected() {

		setOpaque(false);
		nameLabel.setForeground(Color.white);

		updateUI();

	}

	/**
	 * Method used to indicate that the user has clicked on this element, we use
	 * this to set the look of this component to selected
	 */
	public void setSelected() {
		setOpaque(true);
		setBackground(Color.lightGray);
		nameLabel.setForeground(Color.black);

		updateUI();

	}

}
