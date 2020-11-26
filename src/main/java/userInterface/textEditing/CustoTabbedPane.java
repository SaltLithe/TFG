package userInterface.textEditing;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.DEBUG;

public class CustoTabbedPane extends JTabbedPane implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {

		DEBUG.debugmessage("SALTA EL CHANGELISTENER");
		try {
			
			 JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
		        int selectedIndex = tabbedPane.getSelectedIndex();
		        DEBUG.debugmessage("SELECCIONADO EL PANEL " + selectedIndex);
		}
		catch(Exception excp) {}
	}
	
	

}
