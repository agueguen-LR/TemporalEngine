package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;

import java.util.ArrayList;
import java.util.SequencedCollection;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.shader.Shader;

public class Renderer implements RenderLifeCycle, LifeCycleContext{
	
	private static final int MAX_BATCH_SIZE = 1000;
	private SequencedCollection<RenderBatch> mainMenuBatches
		= new ArrayList<>();
	private SequencedCollection<RenderBatch> leftSceneBatches
		= new ArrayList<>();
	private SequencedCollection<RenderBatch> rightSceneBatches
		= new ArrayList<>();
	private static Shader shader;

	public Renderer(){
		mainMenuBatches.add(new RenderBatch(this, MAX_BATCH_SIZE));
		leftSceneBatches.add(new RenderBatch(this, MAX_BATCH_SIZE));
		rightSceneBatches.add(new RenderBatch(this, MAX_BATCH_SIZE));
	}

	// GETTERS
	public Shader getShader(){ return shader; }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)){
			Game.LOGGER.severe(
				"Renderer init method requires a Game context."
			);
			return;
		}

		game.getLogger().info("Rendering hardware: %s.".formatted(
			glGetString(GL_RENDERER)
		));

		shader = new Shader("default");
		shader.load();
		shader.compile();

		mainMenuBatches.forEach(b -> b.init(game.getMainMenu()));
		leftSceneBatches.forEach(b -> b.init(game.getLeftScene()));
		rightSceneBatches.forEach(b -> b.init(game.getRightScene()));
	}
	@Override
	public void start(LifeCycleContext context){
		if(!(context instanceof Game game)){
			Game.LOGGER.severe(
				"Renderer init method requires a Game context."
			);
			return;
		}

		shader.link();

		mainMenuBatches.forEach(b -> b.start(game.getMainMenu()));
		leftSceneBatches.forEach(b -> b.start(game.getLeftScene()));
		rightSceneBatches.forEach(b -> b.start(game.getRightScene()));
	}
	@Override
	public void render(LifeCycleContext context){
		if(!(context instanceof Game game)){
			Game.LOGGER.severe(
				"Renderer init method requires a Game context."
			);
			return;
		}
		Vector2i winSize = game.getWindowInfo().getSize();

		shader.use();

		if(game.isPaused()){
			glViewport(0, 0, winSize.x(), winSize.y());
			mainMenuBatches.forEach(b -> b.render(game.getMainMenu()));
		}else{
			glViewport(0, 0, winSize.x()/2, winSize.y());
			leftSceneBatches.forEach(b -> b.render(game.getLeftScene()));

			glViewport(winSize.x()/2, 0, winSize.x()/2, winSize.y());
			rightSceneBatches.forEach(b -> b.render(game.getRightScene()));
		}

		shader.detach();
	}
	@Override
	public void destroy(LifeCycleContext context){
		if(!(context instanceof Game game)){
			Game.LOGGER.severe(
				"Renderer init method requires a Game context."
			);
			return;
		}

		mainMenuBatches.forEach(b -> b.destroy(game.getMainMenu()));
		leftSceneBatches.forEach(b -> b.destroy(game.getLeftScene()));
		rightSceneBatches.forEach(b -> b.destroy(game.getRightScene()));
	}
}
