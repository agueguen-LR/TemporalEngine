package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.rendering.shader.Shader;

public class Renderer implements RenderLifeCycle, LifeCycleContext{

	private static final int MAX_BATCH_SIZE = 1000;

	private List<RenderBatch> mainMenuBatches = new ArrayList<>();
	private List<RenderBatch> leftSceneBatches = new ArrayList<>();
	private List<RenderBatch> rightSceneBatches = new ArrayList<>();
	private Collection<List<RenderBatch>> sceneBatches;
	private static Shader shader;

	public Renderer(){
		sceneBatches = Collections.unmodifiableCollection(List.of(
			mainMenuBatches, leftSceneBatches, rightSceneBatches
		));

		sceneBatches.forEach(batches ->
			batches.addAll(
				Arrays.stream(Layer.values())
					.map(l -> new RenderBatch(this, MAX_BATCH_SIZE, l))
					.toList()
			)
		);
	}

	// GETTERS
	public Shader getShader(){ return shader; }

	public Scene getAssociatedScene(Game game, List<RenderBatch> batches){
		if(batches == leftSceneBatches) return game.getLeftScene();
		else if(batches == rightSceneBatches) return game.getRightScene();
		else return game.getMainMenu();
	}

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
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		sceneBatches.forEach(batches -> {
			Scene s = getAssociatedScene(game, batches);
			batches.forEach(rb -> rb.init(s));
		});
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

		sceneBatches.forEach(batches -> {
			Scene s = getAssociatedScene(game, batches);
			batches.forEach(rb -> rb.start(s));
		});
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

		glEnable(GL_BLEND);

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

		glDisable(GL_BLEND);

		sceneBatches.forEach(batches -> {
			Scene s = getAssociatedScene(game, batches);
			batches.forEach(rb -> rb.destroy(s));
		});
	}
}
