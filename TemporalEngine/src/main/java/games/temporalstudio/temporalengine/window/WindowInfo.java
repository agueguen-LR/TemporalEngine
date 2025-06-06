package games.temporalstudio.temporalengine.window;

import org.joml.Vector2i;

public final class WindowInfo{

	private final Window window;

	public WindowInfo(Window window){
		this.window = window;
	}

	// GETTERS
	public Vector2i getSize(){ return window.getSize(); }
}
