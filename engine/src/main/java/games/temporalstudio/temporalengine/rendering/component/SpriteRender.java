package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.Layer;

public class SpriteRender extends TileRender{

	public static final float ANIMATION_DURATION = 1.25f;

	private float animatingTime = 0;

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

	// LIFECYCLE FUNCTIONS
	@Override
	public void update(LifeCycleContext context, float delta){
		super.update(context, delta);

		if(animatingTime < getStateDuration())
			animatingTime += delta;
		else{
			animatingTime -= getStateDuration();

			setState(getState() + 1 < getStateCount() ?
				getState() + 1 : TileRender.DEFAULT_STATE
			);
		}
	}
}
