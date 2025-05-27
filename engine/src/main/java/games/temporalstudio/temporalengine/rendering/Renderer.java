package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;

import java.util.ArrayList;
import java.util.SequencedCollection;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.shader.Shader;

public class Renderer implements RenderLifeCycle, LifeCycleContext{
	
	private static final int MAX_BATCH_SIZE = 1000;
	private SequencedCollection<RenderBatch> batches = new ArrayList<>();
	private static Shader shader;

	public Renderer(){
		batches.add(new RenderBatch(MAX_BATCH_SIZE));
	}

	// GETTERS
	public Shader getShader(){ return shader; }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)) return;

		game.getLogger().info("Rendering hardware: %s.".formatted(
			glGetString(GL_RENDERER)
		));

		shader = new Shader("default");
		shader.load();
		shader.compile();

		batches.forEach(b -> b.init(this));
	}
	@Override
	public void start(LifeCycleContext context){
		shader.link();

		batches.forEach(b -> b.start(this));
	}
	@Override
	public void render(LifeCycleContext context){
		shader.use();

		batches.forEach(b -> b.render(this));

		shader.detach();
	}
	@Override
	public void destroy(LifeCycleContext context){
		batches.forEach(b -> b.destroy(this));
	}
}
