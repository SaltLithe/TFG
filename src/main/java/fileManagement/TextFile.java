package fileManagement;

import java.util.concurrent.Semaphore;

import core.DEBUG;

/*Esta clase define a los archivos que usa la aplicacion para leer y ejecutar codigo, guarda los contenidos
 * escritos por el usuario , el tipo de archivo que es y su nombre , mediante el uso de esta clase sencilla
 * se evita leer y escribir constantemente en archivos en el sistema y permite trabajar sin guardar los cambios
 * por si el usuario se arrepiente de lo que ha escrito y quiere volver atras
 */
public class TextFile {
	private String content;
	private FileType type;
	private String name;
	private String path; 
	private String project; 
	private Semaphore editingLock;
	
	
	public TextFile(String name ,String path, String content,  FileType type ) {
		DEBUG.debugmessage("SE HA INVOCADO EL CONSTRUCTOR DE TEXTFILE");
		editingLock = new Semaphore(1);
		this.content = null;
		this.type = type;
		this.name = name;
		this.path = path;
		this.content = content; 
				
		if (type == FileType.CLASS && content == null) {
			content = "public class " + name + " {" + "" + "" + "}";

		}
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {

		return content;
	}

	public void saveFile(String content) {

		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void insert(String changes, int offset) {
		try {
		editingLock.acquire();

		String firsthalf = content.substring(0,offset);
		String secondhalf = content.substring(offset ,content.length());
		content = firsthalf+changes+secondhalf;
		}catch(Exception e) {
			e.printStackTrace();
		}
		editingLock.release(); 
		
	}	

	public void replace( int offset, int length) {
		try {

		editingLock.acquire();
		String firsthalf = content.substring(0,offset);
		String secondhalf = content.substring(length,content.length());
		content = firsthalf+secondhalf;
		}catch(Exception e) {
			e.printStackTrace();
		}
		editingLock.release(); 
	}

}
