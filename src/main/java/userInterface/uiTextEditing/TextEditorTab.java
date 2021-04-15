package userInterface.uiTextEditing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import commandController.CommandController;
import core.DEBUG;
import core.DeveloperComponent;
import networkMessages.HighLightMessage;
import networkMessages.WriteMessage;
import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;

@SuppressWarnings("unused")
public class TextEditorTab extends JPanel {

	public RSyntaxTextArea textEditorArea;
	public TabMiniPanel miniPanel;

	public boolean notConsideredChanges;
	public boolean messageWrite;
	public boolean unsavedChanges;
	private static final long serialVersionUID = 1L;
	private RTextScrollPane textEditorScrollPane;
	private HashMap<Integer, HighlightPainter> painters;
	private HashMap<String, HighlightData> highlights;
	private Semaphore editingLock;
	private CommandController uiController;
	private DeveloperComponent developerComponent;
	private PropertyChangeMessenger support;

	private Semaphore sendSemaphore = new Semaphore(1);
	private String path;
	private String project;
	private String keypath;
	private String chosenName;
	private int alpha = 65;
	private Highlight[] lasthighlight;
	private int LastLine = -2;
	private TextEditorPanel parent;
	private String name; 

	/**
	 * 
	 * @param path       : The path of this file this tab opens
	 * @param miniPanel  : The panel for the tab above the editor window
	 * @param project    : The project this file belongs to
	 * @param chosenName : The name of this user for the session
	 */
	public TextEditorTab(String path, TabMiniPanel miniPanel, String project, String chosenName,
			TextEditorPanel parent ,  String name) {

		this.parent = parent;
		lasthighlight = new Highlight[1];
		LastLine = -1;
		this.name = name;
		painters = new HashMap<>();
		highlights = new HashMap<>();
		this.chosenName = chosenName;
		editingLock = new Semaphore(1);

		this.project = project;
		String workSpacePath = project.substring(0, project.lastIndexOf("\\"));
		int indexFrom = workSpacePath.lastIndexOf("\\");

		keypath = path.substring(indexFrom, path.length());

		support = PropertyChangeMessenger.getInstance();
		notConsideredChanges = true;
		this.miniPanel = miniPanel;
		unsavedChanges = false;
		this.path = path;
		uiController = CommandController.getInstance();
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
		
		

		textEditorArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				
				if (developerComponent.isConnected) {

					int caretline = textEditorArea.getCaretLineNumber();
					int linestart = -1;
					int lineend = -1;

					try {
						linestart = textEditorArea.getLineStartOffset(caretline);
						lineend = textEditorArea.getLineEndOffset(caretline);
					} catch (Exception ex) {
					}

					if (caretline != LastLine && linestart != lineend) {
						LastLine = caretline;
						HighLightMessage highlightmessage = new HighLightMessage(linestart, lineend, chosenName,
								keypath);
						try {
							parent.highlightBuffer.put(highlightmessage);
						} catch (InterruptedException e1) {
		
							e1.printStackTrace();
						}

					}

				}


			}

		});

		textEditorArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {

				try {

					sendSemaphore.acquire();

					if (notConsideredChanges) {

						notConsideredChanges = false;
					} else {

						unsavedChanges = true;
						miniPanel.setAsUnsaved();
						support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null);

					}

					if (!messageWrite) {

						WriteMessage message = new WriteMessage(chosenName);
						message.path = keypath;
						message.adding = true;
						
						message.offset = e.getOffset();
						message.changes = textEditorArea.getText().substring(e.getOffset(),
								e.getOffset() + e.getLength());
						

						try {
							parent.sendBuffer.put(message);

						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

					}
				} catch (Exception ex) {

				} finally {

					sendSemaphore.release();
				}

			}

			@Override
			public void removeUpdate(DocumentEvent e) {

				try {

					sendSemaphore.acquire();
					if (notConsideredChanges) {

						notConsideredChanges = false;
					} else {

						unsavedChanges = true;
						miniPanel.setAsUnsaved();
						support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null);

					}

					if (!messageWrite) {

						WriteMessage message = new WriteMessage(chosenName);
						message.path = keypath;
						message.adding = false;
						message.lenght = e.getLength();
						message.offset = e.getOffset();

						try {
							parent.sendBuffer.put(message);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				} catch (Exception exc) {

				} finally {

					sendSemaphore.release();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}

		});

		setVisible(true);

	}

	/**
	 * Method used to paint highlights from other users
	 * 
	 * @param linestart : Where the highlight starts
	 * @param lineend   : Where the highlight ends
	 * @param color     : A number representing the color of this highlight
	 * @param username  : The username of the client that created this highlight
	 */
	public void paintHighLight(int linestart, int lineend, int color, String username) {

		if (!painters.containsKey(color)) {

			Color newcolor = new Color(color);
			Color transparency = new Color(newcolor.getRed(), newcolor.getGreen(), newcolor.getBlue(), alpha);
			painters.put(color, new DefaultHighlightPainter(transparency));

		}

		if (!highlights.containsKey(username)) {

			highlights.put(username, new HighlightData(linestart, lineend, username, null));

		}
		HighlightData removing = highlights.get(username);
		if (removing.highlight != null) {
			textEditorArea.getHighlighter().removeHighlight(removing.highlight);
			;
		}

		HighlightPainter p = painters.get(color);
		try {

			Object tag = textEditorArea.getHighlighter().addHighlight(linestart, lineend, p);
			highlights.get(username).highlight = tag;

		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method used whenever a user opens a file and a big string of code has to be
	 * written into the tab
	 * 
	 * @param code : The string containing the saved code inside of the file
	 */
	public void setTextEditorCode(String code) {

		notConsideredChanges = true;
		messageWrite = true;

		textEditorArea.setText(code);
		messageWrite = false;

	}

	/**
	 * Method used to insert updates to the code made by other users
	 * 
	 * @param results : The results from the message sent by other users containing
	 *                the necessary data to update the code
	 */
	public void updateContents(ArrayList<Object> results) {

		try {
			editingLock.acquire();
			messageWrite = true;

			WriteMessage incoming = (WriteMessage) results.get(1);
			if (incoming.adding) {

				textEditorArea.insert(incoming.changes, incoming.offset);

			} else {

				textEditorArea.replaceRange("", incoming.offset, incoming.offset + incoming.lenght);
			}

			CommandController.developerComponent.writeOnClosed(path, textEditorArea.getText());

			messageWrite = false;
		} catch (Exception e) {

		} finally {
			editingLock.release();
		}
	}

	/**
	 * 
	 * @return the path of the file this tab opens
	 */
	public String getPath() {

		return path;
	}

	/**
	 * 
	 * @return the contents of this tab
	 */
	public String getContents() {
		return this.textEditorArea.getText();
	}

	/**
	 * Method that calls the panel for this tab so it changes the name , it also
	 * updates this tab unsaved flag and notifies the mainframe
	 */
	public void setAsSaved() {
		this.unsavedChanges = false;
		this.miniPanel.setAsSaved();
		support.notify(ObserverActions.SET_SAVE_FLAG_FALSE, null);

	}

	/**
	 * 
	 * @return the project of the file that this tab opens
	 */
	public String getProject() {
		return project;
	}
	
	/**
	 * 
	 * @return the name of the file this tab represents
	 */
	public String getName() {
		return name;
	}

}
