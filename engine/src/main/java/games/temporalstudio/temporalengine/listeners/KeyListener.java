package games.temporalstudio.temporalengine.listeners;

import static org.lwjgl.glfw.GLFW.*;

import games.temporalstudio.temporalengine.Game;

public class KeyListener {

	private static KeyListener instance;

	private boolean keyPressed[] = new boolean[350];

	private KeyListener(){}

	// GETTERS
	public static KeyListener get(){
		if(KeyListener.instance == null)
			KeyListener.instance = new KeyListener();

		return KeyListener.instance;
	}

	public static boolean isKeyPressed(int keyCode){
		return KeyListener.get().keyPressed[keyCode];
	}

	// LISTENERS
	public static void keyCallback(long window,
		int key, int scancode, int action, int mods
	){
		if(key == GLFW_KEY_UNKNOWN){
			Game.LOGGER.warning(
				"KeyListener got an unknown key! Why? Who do you work for!? (key=%d, scancode=%d, name=%s, action=%d, mods=%d)"
					.formatted(
						key, scancode, glfwGetKeyName(key, scancode),
						action, mods
					)
			);
			return;
		}

		if(action == GLFW_PRESS){
			KeyListener.get().keyPressed[key] = true;
		}else if (action == GLFW_RELEASE){
			KeyListener.get().keyPressed[key] = false;
		}
	}
}
