package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class TextEditorPanel extends JPanel {

	private RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;

	private String focus = null;

	public void enableTextEditorArea() {

		textEditorArea.setEnabled(true);
	}

	public String getCodeFromEditor() {

		return textEditorArea.getText();

	}

	public void setFocus(String name) {

		this.focus = name;
	}

	public String getFocus() {

		return focus;
	}

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

	public String getContents() {

		return textEditorArea.getText();
	}

	public void setContents(String newcontents) {
		textEditorArea.setText("");
		textEditorArea.setText(newcontents);
		// TODO Auto-generated method stub

	}

	public void enableEditor() {
		textEditorArea.setEnabled(true);
		// TODO Auto-generated method stub

	}

}