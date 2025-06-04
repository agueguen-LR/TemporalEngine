package games.temporalstudio.temporalengine.rendering.component;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.texture.TileMap;

public final class MapRender extends TextureRender{

	private String mapName;

	public MapRender(String textureName, String mapName){
		super(textureName, Layer.BACKGROUND);

		this.mapName = mapName;
	}

	// GETTERS
	public TileMap getMap(){ return TileMap.get(mapName); }

	// FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		super.init(context);

		if(!getMap().wasLoaded())
			getMap().load();
	}
}
