package games.temporalstudio.temporalengine.window;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

public enum Platform{

	WIN32(GLFW_PLATFORM_WIN32),
	X11(GLFW_PLATFORM_X11),
	WAYLAND(GLFW_PLATFORM_WAYLAND),
	COCOA(GLFW_PLATFORM_COCOA),
	UNAVAILABLE(GLFW_PLATFORM_UNAVAILABLE);

	public final int platform;

	private Platform(int platform){
		this.platform = platform;
	}

	// GETTERS
	public static Platform getByID(int platform){
		return Arrays.stream(values())
			.filter(p -> p.platform == platform)
			.findFirst().orElse(null);
	}
}
