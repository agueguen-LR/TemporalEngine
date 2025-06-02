package games.temporalstudio.temporalengine.rendering.component;

import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.rendering.Layer;

public sealed abstract class Render implements Component
	permits ColorRender, TextureRender
{

	public static final Layer DEFAULT_LAYER = Layer.OBJECT;
	
	private Layer layer;

	protected Render(Layer layer){
		this.layer = layer;
	}

	// GETTERS
	public Layer getLayer(){ return layer; }
}
