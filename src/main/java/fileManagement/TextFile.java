package fileManagement;

import java.util.concurrent.Semaphore;

/**
 * Class used to keep files in memory , save useful data and edit them in
 * realtime. Saving class files uses these objects in order to keep track of the
 * changes in memory and preventing constant read and write operations in files
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class TextFile {
	private String content;
	private FileType type;
	private String name;
	private String path;
	private Semaphore editingSem;
	private int counter = 0 ;
	private int lastcounter = -1;

	/***
	 * 
	 * @param name    : The name of this file
	 * @param path    : The path of this file
	 * @param content : The contents of this file
	 * @param type    : The type of file
	 */
	public TextFile(String name, String path, String content, FileType type) {

		editingSem = new Semaphore(1);
		this.content = null;
		this.type = type;
		this.name = name;
		this.path = path;
		this.content = content;

		if (type == FileType.CLASS && content == null) {
			content = "public class " + name + " {" + "" + "" + "}";

		}
	}

	/**
	 * 
	 * @return The type of the file
	 */
	public FileType getType() {
		return type;
	}

	/**
	 * Set the type
	 * 
	 * @param type
	 */
	public void setType(FileType type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The name of the file
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the contents
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		try {
			editingSem.acquire();
		
		this.content = content;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}finally {
		editingSem.release();
		}
	}

	/**
	 * 
	 * @return The file's contents
	 */
	public String getContent() {

		return content;
	}

	/**
	 * 
	 * @return The file's path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the path
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Insert into the contents
	 * 
	 * @param changes : The changes to insert
	 * @param offset  : Where to insert them
	 */
	public void insert(String changes, int offset) {
		try {
			editingSem.acquire();
			
			lastcounter = counter; 
			counter++;
			
			String firsthalf = content.substring(0, offset-changes.length());
			String secondhalf = content.substring(offset-changes.length(), content.length());
			content = firsthalf + changes + secondhalf;
		} catch (Exception e) {
			System.out.println("CHANGES : " + changes);
			System.out.println("OFFSET : " + offset);

			e.printStackTrace();
		} finally {

			editingSem.release();
		}

	}

	/**
	 * Remove from contents
	 * 
	 * @param offset : Where to remove from
	 * @param length : How much to remove
	 */
	public void replace(int offset, int length) {

		try {
			editingSem.acquire();
			
			try {
				String firsthalf = content.substring(0, offset);
			String secondhalf = content.substring(length, content.length());
			content = firsthalf + secondhalf;

			}
			catch(Exception e) {
				//content = firsthalf;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			editingSem.release();

		}

	}

}
