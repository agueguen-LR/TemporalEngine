package games.temporalstudio.temporalengine.rendering.batch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.tiledreader.TiledTile;
import org.tiledreader.TiledTileLayer;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.Renderer;
import games.temporalstudio.temporalengine.rendering.component.MapRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.texture.Texture;
import games.temporalstudio.temporalengine.rendering.texture.TileMap;
import games.temporalstudio.temporalengine.rendering.texture.Texture.Tile;

public class MapRenderBatch extends RenderBatch{

	boolean wasUpdated = false;

	public MapRenderBatch(Renderer renderer, int size, Layer layer){
		super(renderer, size, layer);
	}

	// GETTERS
	@Override
	protected boolean isRenderableIncluded(GameObject renderable){
		return renderable.hasComponent(MapRender.class);
	}
	@Override
	protected int getObjectCount(GameObject renderable){
		return renderable.getComponent(MapRender.class)
			.getMap().getTileCount();
	}

	// SETTERS
	public void refresh(){
		wasUpdated = false;
	}
	@Override
	protected boolean updateVerticesAt(int index,
		GameObject renderable, Map<Texture, Integer> sampler
	){
		if(wasUpdated) return wasUpdated;

		Render r = getRenderFromRenderable(renderable);
		if(!(r instanceof MapRender mr)) return false;

		TileMap map = mr.getMap();
		TiledTileLayer tileLayer = map.getTileLayer(getLayer());

		Matrix4f transformMat = getTransformMat(renderable);
		List<Vector4f> colors = WHITE_COLORS;
		float texIndex = sampler.get(mr.getTexture());

		if(!tileLayer.getTileLocations().isEmpty()){
			AtomicInteger i = new AtomicInteger(index);
			tileLayer.getTileLocations().forEach(p -> {
				TiledTile tTile = tileLayer.getTile(p.x, p.y);
				Tile tile = tTile != null ?
					mr.getTexture().getTile(tTile.getID()) : null;

				if(tile != null){
					List<Vector2f> texCoords = mr.getTexture().getCoords(tile);

					updateVerticesAt(getOffset(i.getAndIncrement()),
						transformMat.translate(
							(float) p.getX(),
							(float) (map.getHeight() - p.getY()), 0,
							new Matrix4f()
						).scale(tile.scale().x(), tile.scale().y(), 0),
						colors, texIndex, texCoords
					);
				}
			});
		}

		return wasUpdated = true;
	}
}
