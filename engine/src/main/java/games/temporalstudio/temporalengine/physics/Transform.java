package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

public class Transform implements Component{

	private Vector2f position;
	private Vector2f scale;

	public Transform(Vector2f scale, Vector2f position) {
		this.scale = scale;
		this.position = position;
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
