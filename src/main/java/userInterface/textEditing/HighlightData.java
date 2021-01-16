package userInterface.textEditing;

import javax.swing.text.Highlighter.Highlight;

public class HighlightData {

	public int linestart;
	public Highlight highlight;
	public int lineend;
	
	public HighlightData (int linestart , String name , int lineend, Highlight highlight) {
		
		this.linestart = linestart;
		this.highlight = highlight; 
		this.lineend = lineend;
		
		
	}
	
	
	
}
