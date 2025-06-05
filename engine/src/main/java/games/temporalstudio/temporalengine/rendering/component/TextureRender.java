package games.temporalstudio.temporalengine.rendering.component;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.texture.Texture;

public sealed abstract class TextureRender extends Render
	permits TileRender, MapRender
{

	private String textureName;

	public TextureRender(String textureName, Layer layer){
		super(layer);

		this.textureName = textureName;
	}

	// GETTERS
	public Texture getTexture(){ return Texture.get(textureName); }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!getTexture().wasLoaded())
			getTexture().load();
	}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
