package userInterface.networkManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import userInterface.ObserverActions;

/*Esta clase contiene un panel en el que de momento se añaden labels con el dueño de sesion y su ip
 * en el futuro tiene que ser capaz de sacar notificaciones cuando ocurran eventos como que se conecte un 
 * usuario , se comience una ejecucion global , etc 
 */

public class UsersPanel extends JPanel implements PropertyChangeListener {

	JLabel sessionowner;
	JLabel ipIndicator;
	JButton sessionButton;
	JButton disconnectButton;
	JPanel userIconsPanel;
	testIconComponent self; 
	testIconComponent server; 
	LinkedList<testIconComponent> icons;

	public UsersPanel() {
		
		icons = new LinkedList<testIconComponent>(); 
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JToolBar toolBar = new JToolBar();
		panel.add(toolBar);

		sessionButton = new JButton("Join/Create");
		sessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				ConnectionDialog d = new ConnectionDialog();

			}

		});

		toolBar.add(sessionButton);

		disconnectButton = new JButton("Disconnect");
		toolBar.add(disconnectButton);

		userIconsPanel = new JPanel();
		add(userIconsPanel, BorderLayout.WEST);
		userIconsPanel.setLayout(new BoxLayout(userIconsPanel, BoxLayout.Y_AXIS));

	}

	public void setSessionOwner(String name) {
		sessionowner = new JLabel("Owner : " + name);
		add(sessionowner);
		updateUI();
		// TODO Auto-generated method stub

	}

	public void setIpIndicator(String remoteaddr) {
		ipIndicator = new JLabel(remoteaddr);
		add(ipIndicator);
		updateUI();
		// TODO Auto-generated method stub

	}
	

	private void removeClient() {
		
		
	}
	

	
	private void setSelf(Color color , String image , String name) {
		 self =  new testIconComponent(image,color,name,false);
		 icons.addFirst(self);
	}
	
	
	private void setServer(Color color , String image , String name) {
		 server = new testIconComponent(image,color,name,false);
		 icons.addFirst(server);

	}
	
	private void setClient(String string , int color , String name) {
		testIconComponent client = new testIconComponent(color,string,name);
		icons.addLast(client);
		
	}
	
	
	private void Rearange() {
		
		this.userIconsPanel.removeAll();
		for (testIconComponent c : icons) {
			
			c.setMaximumSize( c.getPreferredSize() );
			c.setMaximumSize( c.getPreferredSize() );
			c.setMaximumSize( c.getPreferredSize() );
			userIconsPanel.add(c);
			
			
			
		}
		userIconsPanel.updateUI();
		
		
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		ArrayList<Object> result = (ArrayList<Object>) evt.getNewValue();
		
		switch(action) {
		case  SET_SELF_ICON :
			
			setSelf((Color)result.get(0),(String)result.get(1),(String)result.get(2));
			Rearange();
			break;
		case SET_SERVER_ICON:
			setServer((Color)result.get(0),(String)result.get(1),(String)result.get(2));
			Rearange();
			
			break;
		case SET_CLIENT_ICON:
		
			setClient((String)result.get(0),(int)result.get(1),(String)result.get(2));
			Rearange(); 
			break;
		case REMOVE_CLIENT_ICON:
			removeClient();
			Rearange(); 
			break;
		
		case DISABLE_JOIN_BUTTON:
			sessionButton.setEnabled(false);
			break;
		case ENABLE_JOIN_BUTTON:
			sessionButton.setEnabled(true);
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

}
