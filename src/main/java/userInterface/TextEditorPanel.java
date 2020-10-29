package userInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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

/*Clase que contiene el editor de texto usando un elemento del tipo RSyntaxTextArea 
 * Tiene tambien una referencia al focus actual que indica el archivo sobre el que se está
 * trabajando actualmente en la aplicación
 * 
 */
@SuppressWarnings("serial")
public class TextEditorPanel extends JPanel implements PropertyChangeListener {

	
	
	
	private RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;
	private JTabbedPane tabPane;

	private String focus = null;
	private int linenum;
	private int columnnum;
	private int lastCaretPos;
	private int newCaretPos;
	private TextEditorToolbar toolbar;
	private UIController uicontroller;
	private DeveloperComponent developerComponent;
	private int lastLenght;
	private int newLenght;
	private boolean messageWrite;
	private Color dark = new Color(70, 70, 70);

	// Activa el editor de texto
	public void enableTextEditorArea() {

		textEditorArea.setEnabled(true);
	}

	// Metodo para poner el focus del editor
	public void setFocus(String name) {

		this.focus = name;
	}

	// Metodo para recuperar el focus actual
	public String getFocus() {

		return focus;
	}

	// Metodo para establecer los contenidos del editor de forma correcta cambiando
	// el focus actual
	public void setTextEditorCode(String code, String filename) {

		linenum = 1;
		columnnum = 1;
		lastCaretPos = 1;
		newCaretPos = 1;
		textEditorArea.setText(code);
		if (focus == null) {

			enableTextEditorArea();
		}
		focus = filename;

	}

	public TextEditorPanel(TextEditorToolbar toolbar) {

		lastLenght = 0;
		newLenght = 0;
		uicontroller = UIController.getInstance();
		developerComponent = uicontroller.getDeveloperComponent();
		messageWrite = false;

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		this.toolbar = toolbar;
		tabPane = new JTabbedPane();
		
		
		/*
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
		textEditorScrollPane = new RTextScrollPane(textEditorArea);
		add(textEditorScrollPane, BorderLayout.CENTER);
*/
		
		TextEditorTab tab = new TextEditorTab();
		tabPane.addTab("Tab de prueba", tab.panel);
		
		TextEditorTab tab2 = new TextEditorTab();
		tabPane.addTab("Tab de prueba", tab2.panel);

		this.add(tabPane,BorderLayout.CENTER);
		
		tabPane.setVisible(true);
		
		/*
		// Trackear el caret
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
		
		*/
		

		RSyntaxTextArea dummy = new RSyntaxTextArea();
		// trackear los cambios en el documento
		dummy.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {

				newLenght = textEditorArea.getText().length();

				if (!messageWrite) {
					if (newLenght > lastLenght) {
						// Mas o menos detecta bastante bien lo que se añade al documento

						WriteMessage message = new WriteMessage();

						int caret = textEditorArea.getCaretPosition();
						message.caret = caret;
						message.lenght = e.getLength();

						int lenght = caret + e.getLength();
						String changes = textEditorArea.getText().substring(caret, lenght);
						System.out.println("What changed " + changes);
						message.adding = true;
						message.added = changes;

						uicontroller.run(() -> developerComponent.sendMessageToEveryone(message));
					}
				}
				messageWrite = false;

			}
			
		
			@Override
			public void removeUpdate(DocumentEvent e) {

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
						uicontroller.run(() -> developerComponent.sendMessageToEveryone(message));
					}
				}
				messageWrite = false;
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

		});
		
		
		this.setVisible(true);
	}

	// Metodo que recupera los contenidos del editor de texto

	private void updateStatus(int linenum, int columnnum, int caretpos) {
		this.linenum = linenum;
		this.columnnum = columnnum;
		lastCaretPos = newCaretPos;
		newCaretPos = caretpos;

	}

	public String getContents() {

		return textEditorArea.getText();
	}

	// Metodo para activar el editor de texto
	public void enableEditor() {
		textEditorArea.setEnabled(true);

	}

	public void updateContents(int caret, String added) {

		textEditorArea.insert(added, caret);

	}

	public void updateContents(int caret, int lenght) {

		DEBUG.debugmessage("DELETED THE FOLLOWING : " + textEditorArea.getText().substring(caret, caret + lenght));
		textEditorArea.replaceRange(null, caret, caret + lenght);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = Enum.valueOf(ObserverActions.class, evt.getPropertyName());
		ArrayList<Object> results;
		switch (action) {

		case ENABLE_TEXT_EDITOR:
			enableEditor();
			break;
		case UPDATE_PANEL_CONTENTS:
			enableEditor();
			results = (ArrayList<Object>) evt.getNewValue();
			boolean adding = (boolean) results.get(2);
			messageWrite = true;
			if (adding) {
				int caret = (int) results.get(0);
				String added = (String) results.get(1);
				updateContents(caret, added);
			} else if (!adding) {
				int caret = (int) results.get(0);
				int lenght = (int) results.get(1);
				updateContents(caret, lenght);
			}
			break;
		case SET_TEXT_CONTENT:
			results = (ArrayList<Object>) evt.getNewValue();
			String newcontents = (String) results.get(0);
			String filename = (String) results.get(1);

			setTextEditorCode(newcontents, filename);
			DEBUG.debugmessage("Jamas cambieis vuestra arquitectura lo ultimo");
			break;
		default:
			break;
		}
	}

}