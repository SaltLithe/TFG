package userInterface.textEditing;

import javax.swing.text.Highlighter.Highlight;

public class HighlightData {

	public int linestart;
	public Object highlight;
	public int lineend;
	
	/**
	 * 
	 * @param linestart
	 * @param lineend
	 * @param name
	 * @param highlight
	 */
	public HighlightData (int linestart , int lineend, String name,  Object highlight) {
		
		this.linestart = linestart;
		this.highlight = highlight; 
		this.lineend = lineend;
		
		
	}
	
	
	
}
