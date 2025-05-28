package games.temporalstudio.temporalengine.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import games.temporalstudio.temporalengine.utils.IOUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.listeners.MouseListener;

public class Window implements WindowLifeCycle{
	private static final String DEFAULT_TITLE = "Temporal Engine Game";
	private static final String DEFAULT_ICON_PATH = "default_icon.png";

	private static Window instance = null;

	private final int width = 640;
	private final int height = 360;
	private String title;
	private String iconPath;
	private final Consumer<Float> handler;

	private long id = MemoryUtil.NULL;

	public Window(Consumer<Float> handler, @Nullable String title, @Nullable String iconPath){
		if(instance != null)
			new RuntimeException("Multiple windows isn't supported.");

		this.title = title != null ? title : DEFAULT_TITLE;
		this.iconPath = iconPath != null ? iconPath : DEFAULT_ICON_PATH;
		this.handler = handler;

		Window.instance = this;

		// TODO: Give it to logger (Move to init?).
		GLFWErrorCallback.createPrint(System.err).set();
	}
	public Window(Consumer<Float> handler){
		this(handler, null, null);
	}

	// GETTERS
	public static boolean hasInstance(){ return instance != null; }

	public void setTitle(String title){
		if(!hasInstance())
			throw new RuntimeException();

		this.title = title;
	}

	public void setIcon(String iconPath) {
		if(!hasInstance()) { throw new RuntimeException(); }
		this.iconPath = iconPath;
	}

	private ByteBuffer loadIconBuffer() throws IOException {
		try { return IOUtil.ioResourceToByteBuffer(iconPath, 512 * 512 * 4); }
		catch (Exception e) {
			Game.LOGGER.warning("Failed to load icon from path: %s. Using default icon.".formatted(iconPath));
			return IOUtil.ioResourceToByteBuffer(DEFAULT_ICON_PATH, 512 * 512 * 4);
		}
	}

	private void loadIcon() {
		IntBuffer w = memAllocInt(1);
		IntBuffer h = memAllocInt(1);
		IntBuffer comp = memAllocInt(1);

		try (GLFWImage.Buffer icons = GLFWImage.malloc(1)) {
			ByteBuffer icon = loadIconBuffer();
            ByteBuffer pixels = stbi_load_from_memory(icon, w, h, comp, 4);
			if (pixels == null) { throw new IOException("Failed to load icon image from memory."); }
			icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels);
			icons.position(0);
			glfwSetWindowIcon(id, icons);
			stbi_image_free(pixels);
		} catch (IOException e) {
            throw new RuntimeException("Failed to load icon or default icon: " + iconPath, e);
        }
    }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)) return;

		game.getLogger().info("GLFW version: %s.".formatted(
			glfwGetVersionString()
		));

		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

		id = glfwCreateWindow(width, height, title,
			MemoryUtil.NULL, MemoryUtil.NULL
		);

		if(id == MemoryUtil.NULL)
			throw new IllegalStateException(
				"Unable to create the GLFW window."
			);

		loadIcon();
	}
	@Override
	public void start(LifeCycleContext context){
		if(!(context instanceof Game game)) return;

		// TODO: Move to related classes.
		glfwSetCursorPosCallback(id, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(id, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(id, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(id, KeyListener::keyCallback);

		glfwMakeContextCurrent(id);
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	
		game.getLogger().info("OpenGL version: %s.".formatted(
			glGetString(GL_VERSION)
		));

		glfwShowWindow(id);
	}
	@Override
	public void run(LifeCycleContext context){
		glfwSwapInterval(1);
		glClearColor(0, 0, 0, 0);

		glfwSetTime(0);
		float beginTime = (float) glfwGetTime();
		float endTime;
		float dt = -1;

		while(!glfwWindowShouldClose(id)){
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			this.handler.accept(dt);

			glfwSwapBuffers(id);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = (float) glfwGetTime();
		}
	}
	@Override
	public void destroy(LifeCycleContext context){
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);

		glfwTerminate();
		glfwSetErrorCallback(null).free();

		id = 0;
	}
}
