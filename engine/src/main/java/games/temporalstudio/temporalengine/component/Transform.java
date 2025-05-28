package games.temporalstudio.temporalengine.component;

import org.joml.Vector2f;

import games.temporalstudio.temporalengine.LifeCycleContext;

public class Transform implements Component{

	private Vector2f position;

	public Transform(Vector2f position){
		this.position = position;
	}

	// GETTERS
	public Vector2f getPosition(){ return position; }

	// LIFECYLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
