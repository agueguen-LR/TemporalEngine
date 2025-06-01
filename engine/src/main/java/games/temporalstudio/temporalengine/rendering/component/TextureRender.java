package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.texture.Texture;

public final class TextureRender implements Render{

	private Texture texture;
	private Vector2i position;
	private Vector2i scale;

	public TextureRender(Texture texture, Vector2i position){
		this.texture = texture;
		this.position = position;
		this.scale = new Vector2i(1, 1);
	}

	// GETTERS
	public Texture getTexture(){ return texture; }
	public Vector2i getPosition(){ return position; }
	public Vector2i getScale(){ return scale; }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		getTexture().load();
	}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
