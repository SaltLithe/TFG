package observerController;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Class that suscribes classes to listen to notifications and sends them using
 * the method from the class PropertyChangeMessenger This class implements the
 * singleton pattern
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class PropertyChangeMessenger {

	private static PropertyChangeMessenger instance;
	private PropertyChangeSupport support;

	public static PropertyChangeMessenger getInstance() {
		if (instance == null) {
			instance = new PropertyChangeMessenger();
		}
		return instance;
	}

	/**
	 * Notify the listeners
	 * 
	 * @param action   : The enum that will signify what kind of notification this
	 *                 is
	 * @param newvalue : The objects to send alongside the notification
	 */
	public void notify(ObserverActions action, Object[] newvalue) {
		ArrayList<Object> message = new ArrayList<>();
		if(newvalue != null) {
		
		for (int i = 0; i < newvalue.length; i++) {
			message.add(newvalue[i]);
		}
		}
		support.firePropertyChange(action.toString(), null, message);

	}

	/**
	 * Protect regular constructor
	 */
	private PropertyChangeMessenger() {
		support = new PropertyChangeSupport(this);
	}

	/**
	 * Subscribe ui component to the listeners
	 * 
	 * @param pcl : The ui component to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	/**
	 * Unsuscribe ui component from the listeners
	 * 
	 * @param pcl : The ui component to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}
}
