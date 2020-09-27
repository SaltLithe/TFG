package TFG;

public class Memento {
	Object myObject;

	public Object getState() {
		return myObject;
	}

	public void setState(Object myObject) {
		this.myObject = myObject;
	}
}
