package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

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

		textEditorArea.setText(code);
		if (focus == null) {

			enableTextEditorArea();
		}
		focus = filename;

	}

	public TextEditorPanel() {

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		textEditorArea = new RSyntaxTextArea();
		textEditorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textEditorArea.setCodeFoldingEnabled(true);
		textEditorArea.setEnabled(false);
		textEditorScrollPane = new RTextScrollPane(textEditorArea);
		add(textEditorScrollPane, BorderLayout.CENTER);

	}

	// Metodo que recupera los contenidos del editor de texto

	public String getContents() {

		return textEditorArea.getText();
	}

	// Metodo para activar el editor de texto
	public void enableEditor() {
		textEditorArea.setEnabled(true);

	}

}