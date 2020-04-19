import java.io.Serializable;

/*Esta es una clase serializable utilizada para enviar mensajes entre cliente y server , la clase puede
 * transformarse de objeto a string y de string a objeto  , asi pueden mandarse mensajes por el socket que una vez
 * deserializados se podra acceder a sus propiedades fácilmente sin necesidad de tener que porgamar un parser
 * y con flexibilidad para añadirle mas campos si se desea
 */
@SuppressWarnings("serial")
public class PersonalMessage implements Serializable {

	private final String text;
	private final String type;
	private final int id;

	public PersonalMessage(String text, String type, int id) {
		this.text = text;
		this.type = type;
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public String getType() {

		return type;
	}

	public int getId() {

		return id;
	}

}
