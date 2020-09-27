package TFG;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PropertyChangeMessenger {

	private static PropertyChangeMessenger instance;
	private PropertyChangeSupport support;

	public static PropertyChangeMessenger getInstance() {
		if (instance == null) {
			instance = new PropertyChangeMessenger();
		}
		return instance;
	}

	public void notify(ObserverActions action, Object oldvalue, Object newvalue) {
		support.firePropertyChange(action.toString(), oldvalue, newvalue);

	}

	private PropertyChangeMessenger() {
		support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}
}
