package games.temporalstudio.temporalengine;

import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.listeners.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.util.function.Consumer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

class Window {
	private static float timeStarted = System.nanoTime() * 1E-9f;

	private int width;
	private int height;
	private String title;

	private long window = MemoryUtil.NULL;

	private Consumer<Float> loop;

	public Window(Consumer<Float> loop, String title) {
		this.width = 1920;
		this.height = 1080;
		this.title = title;
		this.loop = loop;
	}

	// GETTERS
	public void setTitle(String title){
		if(window != MemoryUtil.NULL)
			throw new RuntimeException();

		this.title = title;
	}

	public void run(){
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();

		float beginTime = timeStarted;
		float endTime = timeStarted;
		float dt = -1.0f;
		while (!glfwWindowShouldClose(window)){
			// Poll events
			glfwPollEvents();

			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			this.loop.accept(dt);

			glfwSwapBuffers(window);

			endTime = (System.nanoTime() - timeStarted) * 1E-9f;
			dt = endTime - beginTime;
			beginTime = endTime;
		}

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void init() {
		// Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW
		if (!glfwInit()){
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (window == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window");
		}

		// Set callbacks
		glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(window, KeyListener::keyCallback);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}
}
