package games.temporalstudio.temporalengine.rendering.component;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.texture.Texture;
import games.temporalstudio.temporalengine.rendering.texture.Texture.Tile;

public final class TextureRender implements Render{

	private String textureName;
	private String tileName;

	public TextureRender(String textureName, String tileName){
		this.textureName = textureName;
		this.tileName = tileName;
	}

	// GETTERS
	public Texture getTexture(){ return Texture.get(textureName); }
	public Tile getTile(){ return getTexture().getTile(tileName); }

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
