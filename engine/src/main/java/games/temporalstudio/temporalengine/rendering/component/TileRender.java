package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Vector2i;

import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.texture.Texture.Tile;

public non-sealed class TileRender extends TextureRender{

	public static final int DEFAULT_STATE = 0;

	private String tileName;
	private int state;
	private Vector2i viewportScale = null;

	public TileRender(String textureName, String tileName, int state,
		Layer layer
	){
		super(textureName, layer);

		this.tileName = tileName;
		this.state = state;
	}
	public TileRender(String textureName, String tileName, Layer layer,
		int state, Vector2i viewportScale
	){
		this(textureName, tileName, state, layer);

		this.viewportScale = viewportScale;
	}
	public TileRender(String textureName, String tileName, int state){
		this(textureName, tileName, state, Render.DEFAULT_LAYER);
	}

	public TileRender(String textureName, String tileName, Layer layer){
		this(textureName, tileName, DEFAULT_STATE, layer);
	}
	public TileRender(String textureName, String tileName, Layer layer,
		Vector2i viewportScale
	){
		this(textureName, tileName, layer, DEFAULT_STATE, viewportScale);
	}

	public TileRender(String textureName, String tileName){
		this(textureName, tileName, DEFAULT_STATE, Render.DEFAULT_LAYER);
	}

	// GETTERS
	public String getTileName(){ return tileName; }
	public Tile getTile(){
		Tile t = getTexture().getTile(tileName);
		return viewportScale != null ?
			new Tile(t.positions(), viewportScale, t.tiledID()) : t;
	}
	public int getState(){ return state; }
	public int getStateCount(){ return getTile().positions().size(); }

	// SETTERS
	public void setTileName(String tileName){
		this.tileName = tileName;
		setState(DEFAULT_STATE);
	}
	public void setState(int state){
		this.state = state;
	}
}
