package core;

import java.util.HashSet;
import java.util.Iterator;

public class ClassPath {

	HashSet<String> classPaths;
	String project;
	
	
	public ClassPath(String project, String[] classes) {
		this.project = project;
		classPaths = new HashSet<String>();
		edit(classes , null);
		
		// TODO Auto-generated constructor stub
	}

	public String[] getClassPath() {
		String[] files = new String[classPaths.size()];
		Iterator<String> i = classPaths.iterator();
		int count = 0 ; 
		while(i.hasNext()) {
			files[count] = i.next();
			count++;
		
		}
		return files;  
	}
	
	
	public void edit(String[] addedclasses, String[] removedclasses) {
		
		if(addedclasses != null) {
			for(String added : addedclasses) {
				if(!classPaths.contains(added)) {
					classPaths.add(added);
				}
			
			}
		}
			if(removedclasses != null) {
	
			for(String removed : removedclasses) {
				if(classPaths.contains(removed)) {
					classPaths.remove(removed);
				}
			}
			}
		
		// TODO Auto-generated method stub
		
	}

}
