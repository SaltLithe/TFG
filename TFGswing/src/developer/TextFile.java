package developer;
public class TextFile {
	private String content;
	private FileType type;
	private String name;

	public TextFile(String name, FileType type) {

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
