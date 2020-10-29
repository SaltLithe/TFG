package userInterface;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;



public class TextEditorTab  {
	public JPanel panel;
	public RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;
	
	public TextEditorTab() {
		panel = new JPanel(); 
	panel.setLayout(new BorderLayout());
	
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
	panel.add(textEditorScrollPane, BorderLayout.CENTER);

	
	panel.setVisible(true);
	}

}
