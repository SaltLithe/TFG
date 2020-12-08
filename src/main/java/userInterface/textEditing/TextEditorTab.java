package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import core.DEBUG;
import core.DeveloperComponent;
import network.WriteMessage;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

public class TextEditorTab extends JPanel {
	
	
	//PUBLIC
	
	//userInterface
	public RSyntaxTextArea textEditorArea;
	public TabMiniPanel miniPanel;
	
	//flags
	public boolean notConsideredChanges;
	public boolean messageWrite;
	public boolean unsavedChanges;
	
	//PRIVATE
	private static final long serialVersionUID = 1L;
	
	//userInterface
	private RTextScrollPane textEditorScrollPane;


	//behaviour
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private PropertyChangeMessenger support;
	private ArrayBlockingQueue<WriteMessage> sendBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	private Semaphore sendSemaphore = new Semaphore(1);
	private long sendDelay = 50; 
	
	//data
	private String path;
	private String project;
	private String keypath;
	
	
	public TextEditorTab(String path, TabMiniPanel miniPanel, String project) {
		
		
		
		DEBUG.debugmessage("Se ha creado un tab para el fichero en la direccion : " + path);
		
		Thread t = new Thread (()-> sendMessages());
		t.start();
		
		this.project = project;
		String workSpacePath = project.substring(0, project.lastIndexOf("\\"));
		int indexFrom = workSpacePath.lastIndexOf("\\");

		keypath = path.substring(indexFrom, path.length());

		support = PropertyChangeMessenger.getInstance();
		notConsideredChanges = true;
		this.miniPanel = miniPanel;
		unsavedChanges = false;
		this.path = path;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();

	
		messageWrite = false;

		setLayout(new BorderLayout());

		textEditorArea = new RSyntaxTextArea();

		textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textEditorArea.setCodeFoldingEnabled(false);

		try {
			Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
			theme.apply(textEditorArea);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		textEditorScrollPane = new RTextScrollPane(textEditorArea);
		add(textEditorScrollPane, BorderLayout.CENTER);

		
		//LISTENERS
		
		textEditorArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
			
				
				try {
					sendSemaphore.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
			

				if (notConsideredChanges) {

					notConsideredChanges = false;
				} else {

					unsavedChanges = true;
					miniPanel.setAsUnsaved();
					support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null, null);

				}

				
				if (!messageWrite) {

			
				WriteMessage message = new WriteMessage();
				message.path = keypath; 
				message.adding = true;
				message.offset = e.getOffset();
				message.changes = textEditorArea.getText().substring(e.getOffset() , e.getOffset()+e.getLength());
				
				
				try {
					sendBuffer.put(message);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				
				}
				sendSemaphore.release();

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					sendSemaphore.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				

				if (notConsideredChanges) {

					notConsideredChanges = false;
				} else {

					unsavedChanges = true;
					miniPanel.setAsUnsaved();
					support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null, null);

				}

				
				if (!messageWrite) {

				WriteMessage message = new WriteMessage();
				message.path = keypath; 
				message.adding = false;
				message.lenght = e.getLength();
				message.offset = e.getOffset(); 
				
				
				try {
					sendBuffer.put(message);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				}
				
				sendSemaphore.release();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				
			
			}

		});

		setVisible(true);
	}
	
	
	
	//PUBLIC 
	
	
	public void sendMessages() {
		
		while(true) {
		 WriteMessage message = null;
		try {
			message = sendBuffer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final WriteMessage finalMessage = message; 
		uiController.run(()-> developerComponent.sendMessageToEveryone(finalMessage));
		try {
			Thread.sleep(sendDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}

		
	}
		
	
	public void setTextEditorCode(String code) {

	
		notConsideredChanges = true;
		messageWrite = true;

		textEditorArea.setText(code);
		messageWrite = false; 

		

	}

	public void updateContents(ArrayList<Object> results) {


		messageWrite = true; 
		
		
		WriteMessage incoming = (WriteMessage) results.get(1);
		if(incoming.adding) {
			
			textEditorArea.insert(incoming.changes, incoming.offset);
			
		}else {
			
			textEditorArea.replaceRange("", incoming.offset, incoming.offset+incoming.lenght);
		}
			
			
		
		
		uiController.run(() -> developerComponent.writeOnClosed(path, textEditorArea.getText()));
		
		messageWrite = false; 


	}



	public String getPath() {

		return path;
	}


	public String getContents() {
		return this.textEditorArea.getText();
	}

	public void setAsSaved() {
		DEBUG.debugmessage("SET AS SAVED");
		this.unsavedChanges = false;
		this.miniPanel.setAsSaved();
		support.notify(ObserverActions.SET_SAVE_FLAG_FALSE, null, null);

	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProject() {
		return project;
	}

}
