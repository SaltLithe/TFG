package core;

import java.util.function.Consumer;

public class DoUndoPair {

	public Consumer<Object[]> actionDo;
	public Consumer<Object[]> actionUndo;
	public Object[] args;

}
