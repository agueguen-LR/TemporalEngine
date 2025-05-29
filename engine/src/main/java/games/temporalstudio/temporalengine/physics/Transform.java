package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

public class Transform implements Component{

	private static final Vector2f DEFAULT_SCALE = new Vector2f(1, 1);

	private Vector2f position;
	private Vector2f scale;

	public Transform(Vector2f position, Vector2f scale){
		this.position = position;
		this.scale = scale;
	}
	public Transform(Vector2f position){
		this(position, new Vector2f(DEFAULT_SCALE));
	}
	public Transform(){
		this(new Vector2f(), new Vector2f(DEFAULT_SCALE));
	}

	// GETTERS
	public Vector2f getPosition(){ return position; }
	public Vector2f getScale(){ return scale; }

	// SETTERS
	public void setPosition(Vector2f position){
		this.position = position;
	}
	public void setScale(Vector2f scale){
		this.scale = scale;
	}
	public void move(Vector2f translation){
		this.position.add(translation);
	}

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
