package core;

public class Command {
	Object myObject;
	Memento memento;
	public Runnable commandDo;

	public Command(Runnable commandDo, Object myObject) {
		this.commandDo = commandDo;
		this.myObject = myObject;
	}

	public Runnable execute() {
		memento = new Memento();
		memento.setState(myObject);
		return commandDo;

	};

	public Object unExecute() {

		return memento.getState();

	};
}