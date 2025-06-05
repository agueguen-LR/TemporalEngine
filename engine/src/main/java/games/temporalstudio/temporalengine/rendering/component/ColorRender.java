package games.temporalstudio.temporalengine.rendering.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joml.Vector4f;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.Layer;

public final class ColorRender extends Render{

	private List<Vector4f> colors = new ArrayList<>();

	public ColorRender(Vector4f color, Layer layer){
		super(layer);

		for(int i = 0; i < 4; i++)
			this.colors.add(color);
	}
	public ColorRender(Vector4f color){
		this(color, Render.DEFAULT_LAYER);
	}
	public ColorRender(Collection<Vector4f> colors, Layer layer){
		super(layer);

		if(colors.size() != 4)
			throw new IllegalArgumentException("Invalid color count;");

		this.colors.addAll(colors);
	}
	public ColorRender(Collection<Vector4f> colors){
		this(colors, Render.DEFAULT_LAYER);
	}

	// GETTERS
	public List<Vector4f> getColors(){
		return Collections.unmodifiableList(colors);
	}

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
