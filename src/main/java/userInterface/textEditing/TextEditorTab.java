package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.autocomplete.*;
import core.DEBUG;
import core.DeveloperComponent;
import network.WriteMessage;
import userInterface.UIController;

public class TextEditorTab extends JPanel {
	public RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;
	private int lastLenght;
	private int newLenght;
	public boolean messageWrite;
	private int linenum;
	private int columnnum;
	private int lastCaretPos;
	private int newCaretPos;
	private boolean isFocus = false;
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private String path;
	public boolean unsavedChanges;
	public TabMiniPanel miniPanel;
	public boolean notConsideredChanges;
	private String project;
	public String name;


	public void setTextEditorCode(String code) {

		linenum = 1;
		columnnum = 1;
		lastCaretPos = 1;
		newCaretPos = 1;
		notConsideredChanges = true;

		textEditorArea.setText(code);

		isFocus = true;

	}

	public void updateContents(ArrayList<Object> results) {

		boolean adding = (boolean) results.get(2);

		messageWrite = true;
		if (adding) {
			int caret = (int) results.get(0);
			String added = (String) results.get(1);
			textEditorArea.insert(added, caret);

		} else if (!adding) {
			int caret = (int) results.get(0);
			int lenght = (int) results.get(1);
			textEditorArea.replaceRange(null, caret, caret + lenght);

		}

	}

	protected void updateStatus(int linenum, int columnnum, int caretpos) {
		this.linenum = linenum;
		this.columnnum = columnnum;
		lastCaretPos = newCaretPos;
		newCaretPos = caretpos;

	}
	
	public String getPath() {
		
		return path; 
	}
	

	public TextEditorTab(String path, TabMiniPanel miniPanel, String project) {
		DEBUG.debugmessage("Se ha creado un tab para el fichero en la direccion : " + path);
		this.project = project;

		notConsideredChanges = false;
		this.miniPanel = miniPanel;
		unsavedChanges = false;
		this.path = path;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();

		lastLenght = 0;
		newLenght = 0;
		messageWrite = false;

		setLayout(new BorderLayout());

		textEditorArea = new RSyntaxTextArea();

		textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textEditorArea.setCodeFoldingEnabled(true);

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

				int linenum = 0;
				int columnnum = 0;
				int caretpos = 0;
				lastLenght = textEditorArea.getText().length();
				try {

					caretpos = textEditorArea.getCaretPosition();
					linenum = textEditorArea.getLineOfOffset(caretpos);
					columnnum = caretpos - textEditorArea.getLineStartOffset(linenum);
					linenum += 1;

				}

				catch (Exception ex) {
				}
				updateStatus(linenum, columnnum, caretpos);

			}

		});
		
		

		textEditorArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {

				DEBUG.debugmessage("Pero saltas o no saltas");
				newLenght = textEditorArea.getText().length();

				if (notConsideredChanges) {

					notConsideredChanges = false;
				} else {

					unsavedChanges = true;
					miniPanel.setAsUnsaved();

				}

				if (!messageWrite) {

					if (newLenght > lastLenght) {

						// Mas o menos detecta bastante bien lo que se añade al documento

						WriteMessage message = new WriteMessage();

						int caret = textEditorArea.getCaretPosition();
						message.caret = caret;
						message.lenght = e.getLength();
						message.path = path;

						int lenght = caret + e.getLength();
						String changes = textEditorArea.getText().substring(caret, lenght);
						System.out.println("What changed " + changes);
						message.adding = true;
						message.added = changes;
						message.path = path;

						uiController.run(() -> developerComponent.sendMessageToEveryone(message));
					}
				}
				messageWrite = false;

			}

			@Override
			public void removeUpdate(DocumentEvent e) {

				newLenght = textEditorArea.getText().length();

				if (notConsideredChanges) {

					notConsideredChanges = false;
				} else {

					unsavedChanges = true;
					miniPanel.setAsUnsaved();
				}

				if (!messageWrite) {

					if (newLenght < lastLenght) {

						System.out.println("Last caret " + lastCaretPos + "New caret " + newCaretPos);
						int changelenght = lastCaretPos - newCaretPos;
						if (changelenght < 0) {
							DEBUG.debugmessage("ESTO NO TENDRA QUE PASAR");
						}
						DEBUG.debugmessage("DIFFERENCE IS : " + changelenght);

						/*
						 * DEBUG.debugmessage("DELETED THE FOLLOWING : " +
						 * textEditorArea.getText().substring(newCaretPos, newCaretPos + changelenght));
						 */

						WriteMessage message = new WriteMessage();
						message.adding = false;
						// message.caret = newCaretPos - 1;
						message.caret = newCaretPos;
						message.lenght = e.getLength();
						uiController.run(() -> developerComponent.sendMessageToEveryone(message));
					}
				}
				messageWrite = false;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

		});

		setVisible(true);
	}

	public String getContents() {
		// TODO Auto-generated method stub
		return this.textEditorArea.getText();
	}

	public void setAsSaved() {
		this.unsavedChanges = false;
		this.miniPanel.setAsSaved();

	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProject() {
		// TODO Auto-generated method stub
		return project;
	}
	
	


}
