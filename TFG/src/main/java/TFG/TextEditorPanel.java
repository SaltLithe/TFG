package TFG;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/*Clase que contiene el editor de texto usando un elemento del tipo RSyntaxTextArea 
 * Tiene tambien una referencia al focus actual que indica el archivo sobre el que se está
 * trabajando actualmente en la aplicación
 * 
 */
@SuppressWarnings("serial")
public class TextEditorPanel extends JPanel {

	private RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;

	private String focus = null;
	private int linenum;
	private int columnnum;
	private int lastCaretPos;
	private int newCaretPos;
	private TextEditorToolbar toolbar;

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

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		this.toolbar = toolbar;
		textEditorArea = new RSyntaxTextArea();
		textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textEditorArea.setCodeFoldingEnabled(true);
		textEditorArea.setEnabled(false);
		textEditorScrollPane = new RTextScrollPane(textEditorArea);
		add(textEditorScrollPane, BorderLayout.CENTER);

		// Trackear el caret
		textEditorArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				int linenum = 1;
				int columnnum = 1;
				int caretpos = 1;
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

		// trackear los cambios en el documento
		textEditorArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
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
				toolbar.sendTextUpdate(message);

			}

			@Override
			public void removeUpdate(DocumentEvent e) {

				System.out.println("Last caret " + lastCaretPos + "New caret " + newCaretPos);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

		});

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

}