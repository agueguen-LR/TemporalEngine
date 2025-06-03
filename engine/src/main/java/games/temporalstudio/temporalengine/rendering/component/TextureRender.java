package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.texture.Texture;
import games.temporalstudio.temporalengine.rendering.texture.Texture.Tile;

public final class TextureRender extends Render{

	private String textureName;
	private String tileName;
	private Vector2i viewportScale;

	public TextureRender(String textureName, String tileName, Layer layer,
		Vector2i viewportScale
	){
		super(layer);

		this.textureName = textureName;
		this.tileName = tileName;
		this.viewportScale = viewportScale;
	}
	public TextureRender(String textureName, String tileName, Layer layer){
		this(textureName, tileName, layer, null);
	}
	public TextureRender(String textureName, String tileName){
		this(textureName, tileName, Render.DEFAULT_LAYER);
	}

	// GETTERS
	public Texture getTexture(){ return Texture.get(textureName); }
	public Tile getTile(){
		Tile t = getTexture().getTile(tileName);
		return viewportScale != null ?
			new Tile(t.position(), viewportScale) : t;
	}

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
