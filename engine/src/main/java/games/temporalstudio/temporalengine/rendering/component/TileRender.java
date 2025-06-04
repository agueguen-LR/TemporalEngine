package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.texture.Texture.Tile;

public final class TileRender extends TextureRender{

	private String tileName;
	private Vector2i viewportScale = null;

	public TileRender(String textureName, String tileName, Layer layer){
		super(textureName, layer);

		this.tileName = tileName;
	}
	public TileRender(String textureName, String tileName, Layer layer,
		Vector2i viewportScale
	){
		this(textureName, tileName, layer);

		this.viewportScale = viewportScale;
	}
	public TileRender(String textureName, String tileName){
		this(textureName, tileName, Render.DEFAULT_LAYER);
	}

	// GETTERS
	public Tile getTile(){
		Tile t = getTexture().getTile(tileName);
		return viewportScale != null ?
			new Tile(t.position(), viewportScale, t.tiledID()) : t;
	}
}
