package fileManagement;

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

	public TextFile(String name, FileType type) {
		DEBUG.debugmessage("SE HA INVOCADO EL CONSTRUCTOR DE TEXTFILE");
		this.content = null;
		this.type = type;
		this.name = name;

		if (type == FileType.CLASS) {
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

}
