import java.io.Serializable;

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
