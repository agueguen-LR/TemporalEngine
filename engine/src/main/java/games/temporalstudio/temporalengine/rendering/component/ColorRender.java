package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector4f;

import games.temporalstudio.temporalengine.LifeCycleContext;

public final class ColorRender implements Render, Colored{

	private Vector4f color;
	
	public ColorRender(Vector4f color){
		this.color = color;
	}

	// GETTERS
	@Override
	public Vector4f getColor(){ return color; }

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context) {}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
