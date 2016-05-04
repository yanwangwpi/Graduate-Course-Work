package himalia.model;

public interface Command {
	public abstract void redo();
	public abstract void undo();
}
