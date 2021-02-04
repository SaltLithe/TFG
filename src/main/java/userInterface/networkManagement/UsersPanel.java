package userInterface.networkManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.UIController;

import javax.swing.ImageIcon;

/**
 * Ui class that contains functionality to join and disconnect from sessions as
 * well as a list rendering the users connected a session represented with
 * profile icons
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class UsersPanel extends JPanel implements PropertyChangeListener {

	JLabel sessionowner;
	JLabel ipIndicator;
	JButton joinSessionButton;
	JButton disconnectButton;
	JPanel userIconsPanel;
	ProfileIIconComponent self;
	ProfileIIconComponent server;
	LinkedList<ProfileIIconComponent> icons;
	private JButton createSessionButton;

	public UsersPanel() {

		icons = new LinkedList<ProfileIIconComponent>();
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JToolBar toolBar = new JToolBar();
		panel.add(toolBar);

		joinSessionButton = new JButton("Join");
		joinSessionButton.setIcon(new ImageIcon(UsersPanel.class.getResource("/resources/images/joinSession_Icon.png")));
		joinSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				new JoinSessionDialog();
			}

		});

		toolBar.add(joinSessionButton);
		
		createSessionButton = new JButton("Create");
		createSessionButton.setIcon(new ImageIcon(UsersPanel.class.getResource("/resources/images/createSession_Icon.png")));
		toolBar.add(createSessionButton);
		createSessionButton.addActionListener(new ActionListener() {
			
			

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(UIController.developerComponent.workSpace == null) {
					JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
						    "You are trying to create a session without a workspace loaded , restart this software and choose a workspace to create"
						    + "a session.",
						    "No Workspace error",
						    JOptionPane.ERROR_MESSAGE);
				}else {
					new CreateSessionDialog(); 

				}
			}

		});

		disconnectButton = new JButton("Disconnect");
		disconnectButton.setIcon(new ImageIcon(UsersPanel.class.getResource("/resources/images/disconnect_Icon.png")));
	
		toolBar.add(disconnectButton);
		disconnectButton.setEnabled(false);
		disconnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				UIController.developerComponent.disconnect();
			}

		});
		
		
		

		userIconsPanel = new JPanel();
		add(userIconsPanel, BorderLayout.WEST);
		userIconsPanel.setLayout(new BoxLayout(userIconsPanel, BoxLayout.Y_AXIS));

	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener for this class
	 * to listen to ui notifications
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		@SuppressWarnings("unchecked")
		ArrayList<Object> result = (ArrayList<Object>) evt.getNewValue();

		switch (action) {
		case SET_SELF_ICON:

			setSelf((Color) result.get(0), (String) result.get(1), (String) result.get(2));
			Rearange();
			break;
		case SET_SERVER_ICON:
			setServer((String) result.get(0), (int) result.get(1), (String) result.get(2));
			Rearange();

			break;
		case SET_CLIENT_ICON:

			setClient((String) result.get(0), (int) result.get(1), (String) result.get(2), (int) result.get(3));
			Rearange();
			break;
		case REMOVE_CLIENT_ICON:
			removeClient((int) result.get(0));
			Rearange();
			break;

		case DISABLE_JOIN_BUTTON:
			joinSessionButton.setEnabled(false);
			createSessionButton.setEnabled(false);
			break;
		case ENABLE_JOIN_BUTTON:
			joinSessionButton.setEnabled(true);
			createSessionButton.setEnabled(true);
			break;
		case ENABLE_DISCONNECT_BUTTON:
			disconnectButton.setEnabled(true);
			break;
		case DISABLE_DISCONNECT_BUTTON:
			disconnectButton.setEnabled(false);
			break;
		default:

			break;

		}

	}

	private void removeClient(int clientID) {
		Iterator<ProfileIIconComponent> i = icons.iterator();
		int index = -1;
		int count = 0;
		boolean found = false;
		while (i.hasNext() && !found) {
			ProfileIIconComponent next = i.next();
			if (next.clientID == clientID) {
				index = count;
				found = true;
			} else {
				count++;
			}
		}
		if (index != -1) {
			icons.remove(index);

			Rearange();
		}

	}

	private void setSelf(Color color, String image, String name) {
		self = new ProfileIIconComponent(image, color, name, false);
		icons.addFirst(self);
	}

	private void setServer(String string, int color, String name) {
		server = new ProfileIIconComponent(color, string, name, -1);
		icons.addFirst(server);

	}

	private void setClient(String string, int color, String name, int clientID) {
		ProfileIIconComponent client = new ProfileIIconComponent(color, string, name, clientID);
		icons.addLast(client);

	}

	private void Rearange() {

		this.userIconsPanel.removeAll();
		for (ProfileIIconComponent c : icons) {

			c.setMaximumSize(c.getPreferredSize());
			c.setMaximumSize(c.getPreferredSize());
			c.setMaximumSize(c.getPreferredSize());
			userIconsPanel.add(c);

		}
		userIconsPanel.updateUI();

	}

}
