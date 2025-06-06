package games.temporalstudio.temporalengine.rendering.component;

import java.util.function.Function;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.rendering.Layer;

public class SpriteRender extends TileRender{

	public static final float ANIMATION_DURATION = 0.8f;

	private float animatingTime = 0;
	private Function<LifeCycleContext, String> chooser
		= SpriteRender::chooseAnim;

	public SpriteRender(String textureName, String tileName, Layer layer){
		super(textureName, tileName, layer);
	}
	public SpriteRender(String textureName, String tileName, Layer layer,
		Vector2i viewportScale
	){
		super(textureName, tileName, layer, viewportScale);
	}
	public SpriteRender(String textureName, String tileName){
		super(textureName, tileName);
	}

	// GETTERS
	private float getStateDuration(){
		return ANIMATION_DURATION/getStateCount();
	}

	// SETTERS
	public void setAnimChooser(Function<LifeCycleContext, String> chooser){
		this.chooser = chooser;
	}

	// FUNCTIONS
	private static String chooseAnim(LifeCycleContext context){
		if(!(context instanceof GameObject go)){
			Game.LOGGER.severe(
				"SpiterRender chooser requires a GameObject context."
			);
			return null;
		}

		return go.getComponent(SpriteRender.class).getTileName();
	}

	// LIFECYCLE FUNCTIONS
	@Override
	public void update(LifeCycleContext context, float delta){
		super.update(context, delta);

		String newTileName = chooser.apply(context);
		if(getTileName() != newTileName)
			setTileName(newTileName);

		if(animatingTime >= getStateDuration()){
			animatingTime = Math.max(animatingTime - getStateDuration(), 0);

			setState(getState() + 1 < getStateCount() ?
				getState() + 1 : TileRender.DEFAULT_STATE
			);
		}else
			animatingTime += delta;
	}
}
