package userInterface.textEditing;

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

import core.DEBUG;
import core.DeveloperComponent;
import network.HighLightMessage;
import network.WriteMessage;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

public class TextEditorTab extends JPanel {

	// PUBLIC

	// userInterface
	public RSyntaxTextArea textEditorArea;
	public TabMiniPanel miniPanel;

	// flags
	public boolean notConsideredChanges;
	public boolean messageWrite;
	public boolean unsavedChanges;

	// PRIVATE
	private static final long serialVersionUID = 1L;

	// userInterface
	private RTextScrollPane textEditorScrollPane;
	private HashMap<Integer, HighlightPainter> painters;
	private HashMap<String, HighlightData> highlights;
	private Semaphore editingLock;
	private Semaphore highlightLock;

	// behaviour
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private PropertyChangeMessenger support;
	private ArrayBlockingQueue<WriteMessage> sendBuffer = new ArrayBlockingQueue<WriteMessage>(200);
	private ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(200);

	private Semaphore sendSemaphore = new Semaphore(1);
	private long sendDelay = 50;

	// data
	private String path;
	private String project;
	private String keypath;
	private String chosenName;
	private int alpha = 65;

	private Highlight[] lasthighlight;
	private int LastLine = -2;

	public TextEditorTab(String path, TabMiniPanel miniPanel, String project, String chosenName) {

		lasthighlight = new Highlight[1];
		highlightLock = new Semaphore(1);
		LastLine = -1;

		painters = new HashMap<Integer, HighlightPainter>();
		highlights = new HashMap<String, HighlightData>();
		this.chosenName = chosenName;
		DEBUG.debugmessage("Se ha creado un tab para el fichero en la direccion : " + path);
		editingLock = new Semaphore(1);

		new Thread(() -> sendMessages()).start();
		;

		new Thread(() -> sendHighlights()).start();

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

		// LISTENERS

		textEditorArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				try {
					editingLock.acquire();
				} catch (InterruptedException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				
				

				if (developerComponent.isConnected) {

					int caretline = textEditorArea.getCaretLineNumber();
					int linestart = -1;
					int lineend = -1;

					try {
						linestart = textEditorArea.getLineStartOffset(caretline);
						lineend = textEditorArea.getLineEndOffset(caretline);
					} catch (Exception ex) {
					}

					System.out.println("LASTLINE : " + LastLine + " Linestart : " + linestart + " Lineeend : "  + lineend); 
					
					if (linestart != LastLine && linestart != lineend) {
						LastLine = caretline; 
						HighLightMessage highlightmessage = new HighLightMessage(linestart, lineend, chosenName,
								keypath);
						try {
							highlightBuffer.put(highlightmessage);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

				}

				editingLock.release();

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
						support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null, null);

					}

					if (!messageWrite) {

						WriteMessage message = new WriteMessage(chosenName);
						message.path = keypath;
						message.adding = true;
						message.offset = e.getOffset();
						message.changes = textEditorArea.getText().substring(e.getOffset(),
								e.getOffset() + e.getLength());

						try {
							sendBuffer.put(message);

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
						support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null, null);

					}

					if (!messageWrite) {

						WriteMessage message = new WriteMessage(chosenName);
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
				} catch (Exception exc) {

				} finally {

					sendSemaphore.release();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				DEBUG.debugmessage("TTHIS IS A CHANGE");

			}

		});

		setVisible(true);

	}

	// PUBLIC

	public void paintHighLight(int linestart, int lineend, int color, String username) {

	

		if (!painters.containsKey(color)) {

			Color newcolor = new Color(color);
			Color transparency = new Color(newcolor.getRed(), newcolor.getGreen(), newcolor.getBlue(), alpha);
			painters.put(color, new DefaultHighlightPainter(transparency));

		}
		textEditorArea.getHighlighter().removeAllHighlights();

		HighlightPainter p = painters.get(color);
		try {
			textEditorArea.getHighlighter().addHighlight(linestart, lineend, p);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		/*
		 * 
		 * if(highlights.containsKey(username)) {
		 * 
		 * textEditorArea.getHighlighter().removeHighlight(highlights.remove(username).
		 * highlight); }
		 * 
		 * boolean positionTaken = false; Object[] names =
		 * highlights.keySet().toArray();
		 * 
		 * int count = 0 ;
		 * 
		 * while (count < names.length && !positionTaken) {
		 * 
		 * if (highlights.get(names[count]).linestart == linestart) { positionTaken =
		 * true; }else { count++; }
		 * 
		 * }
		 * 
		 * 
		 * if(positionTaken) {
		 * 
		 * // data = new HighlightData(linestart , name , lineend , null);
		 * //highlights.put(name, data);
		 * 
		 * }else { try { HighlightPainter p = painters.get(color);
		 * 
		 * textEditorArea.getHighlighter().addHighlight(linestart, lineend, p);
		 * Highlight[] highlightlist = textEditorArea.getHighlighter().getHighlights();
		 * if (highlightlist.length >0 ) { highlights.put(username, new
		 * HighlightData(linestart,username , lineend
		 * ,highlightlist[highlightlist.length-1])); } } catch (BadLocationException e)
		 * { e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * 
		 * 
		 */

	}

	public void sendHighlights() {

		while (true) {
			HighLightMessage message = null;

			try {

				message = highlightBuffer.take();

			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			final HighLightMessage finalMessage = message;
			developerComponent.sendMessageToEveryone(finalMessage);
			try {
				Thread.sleep(sendDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void sendMessages() {

		while (true) {
			WriteMessage message = null;
			try {
				message = sendBuffer.take();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final WriteMessage finalMessage = message;
			developerComponent.sendMessageToEveryone(finalMessage);
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

		try {
			editingLock.acquire();
			messageWrite = true;

			WriteMessage incoming = (WriteMessage) results.get(1);
			if (incoming.adding) {

				textEditorArea.insert(incoming.changes, incoming.offset);

			} else {

				textEditorArea.replaceRange("", incoming.offset, incoming.offset + incoming.lenght);
			}

			uiController.run(() -> developerComponent.writeOnClosed(path, textEditorArea.getText()));

			messageWrite = false;
		} catch (Exception e) {

		} finally {
			System.out.println("Realising editor");
			editingLock.release();
		}
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
