package userInterface;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import core.DEBUG;
import core.DeveloperComponent;
import network.WriteMessage;



public class TextEditorTab  {
	public JPanel panel;
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
	boolean unsavedChanges; 
	public TabMiniPanel miniPanel; 
	
	
	public void setTextEditorCode(String code) {

		linenum = 1;
		columnnum = 1;
		lastCaretPos = 1;
		newCaretPos = 1;
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
	
	public TextEditorTab(String path , TabMiniPanel miniPanel) {
		this.miniPanel = miniPanel; 
		unsavedChanges = false; 
		this.path = path; 
		uiController = UIController.getInstance(); 
		developerComponent = uiController.getDeveloperComponent();
		
		lastLenght = 0;
		newLenght = 0;
		messageWrite = false;

		panel = new JPanel(); 
	panel.setLayout(new BorderLayout());
	
	textEditorArea = new RSyntaxTextArea();

	textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	textEditorArea.setCodeFoldingEnabled(true);
	//textEditorArea.setEnabled(false);

	
	try {
		Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
		theme.apply(textEditorArea);
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	textEditorScrollPane = new RTextScrollPane(textEditorArea);
	panel.add(textEditorScrollPane, BorderLayout.CENTER);
	
	textEditorArea = new RSyntaxTextArea();

	textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	textEditorArea.setCodeFoldingEnabled(true);
	textEditorArea.setEnabled(false);
	try {
		Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
		theme.apply(textEditorArea);
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	
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
			
			unsavedChanges = true;
			miniPanel.setAsUnsaved(); 
			newLenght = textEditorArea.getText().length();

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

			unsavedChanges = true;
			miniPanel.setAsUnsaved(); 

			newLenght = textEditorArea.getText().length();

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
	
	
	panel.setVisible(true);
	}

	public String getContents() {
		// TODO Auto-generated method stub
		return this.textEditorArea.getText();
	}

}
