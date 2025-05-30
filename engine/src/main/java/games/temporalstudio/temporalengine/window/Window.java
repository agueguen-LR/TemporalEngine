package games.temporalstudio.temporalengine.window;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.listeners.MouseListener;
import games.temporalstudio.temporalengine.utils.NIOUtils;

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

	public Window(Consumer<Float> handler,
		@Nullable String title, @Nullable String iconPath
	){
		if(Window.hasInstance())
			new RuntimeException("Multiple windows isn't supported;");

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

	public Vector2i getSize(){
		int[] width = new int[1];
		int[] height = new int[1];

		glfwGetWindowSize(id, width, height);

		return new Vector2i(width[0], height[0]);
	}

	// SETTERS
	public void setTitle(String title){
		this.title = title;
	}

	public void setIcon(String iconPath) {
		this.iconPath = iconPath;
	}

	// FUNCTIONS
	private ByteBuffer loadIconBuffer() throws IOException{
		try{
			return NIOUtils.getResourceAsByteBuffer(iconPath);
		}catch (Exception e){
			Game.LOGGER.warning(
				"Failed to load icon at %s; Using default icon."
					.formatted(iconPath)
			);
			return NIOUtils.getResourceAsByteBuffer(DEFAULT_ICON_PATH);
		}
	}
	private void loadIcon(){
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		try(GLFWImage.Buffer icons = GLFWImage.malloc(1)){
			ByteBuffer icon = loadIconBuffer();
            ByteBuffer pixels = stbi_load_from_memory(icon,
				w, h, comp, 4
			);

			if(pixels == null)
				throw new IOException(
					"Failed to load icon image from memory;"
				);

			icons.position(0)
				.width(w.get(0))
				.height(h.get(0))
				.pixels(pixels);

			glfwSetWindowIcon(id, icons);

			stbi_image_free(pixels);
		}catch(IOException e){
            throw new RuntimeException(
				"Failed to load any icon: %s;".formatted(iconPath), e
			);
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
			throw new IllegalStateException("Unable to initialize GLFW;");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

		id = glfwCreateWindow(width, height, title,
			MemoryUtil.NULL, MemoryUtil.NULL
		);

		if(id == MemoryUtil.NULL)
			throw new IllegalStateException(
				"Unable to create the GLFW window;"
			);

		if(!glfwPlatformSupported(glfwGetPlatform())){
			loadIcon();
		}else
			game.getLogger().warning(
				"Current platform %s isn't supported by GLFW; Some features may not be available."
					.formatted(Platform.getByID(glfwGetPlatform()))
			);
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
