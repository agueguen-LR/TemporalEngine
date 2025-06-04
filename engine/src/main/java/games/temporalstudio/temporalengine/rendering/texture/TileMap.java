package games.temporalstudio.temporalengine.rendering.texture;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SequencedCollection;

import org.tiledreader.TiledMap;
import org.tiledreader.TiledReader;
import org.tiledreader.TiledResource;
import org.tiledreader.TiledTileLayer;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.utils.AssetPool;
import games.temporalstudio.temporalengine.utils.AssetPoolObject;

public class TileMap implements AssetPoolObject{

	private static final String MAPS_FOLDER = "assets/maps";
	private static final String MAP_FILE_FORMAT = "%s.tmx";
	private static final ResourceTiledReader READER = new ResourceTiledReader();

	private String name;
	private int width;
	private int height;
	private SequencedCollection<TiledTileLayer> layers
		= Collections.emptyList();

	private boolean loaded = false;

	private TileMap(String name){
		this.name = name;
	}

	// GETTERS
	public static TileMap get(String name){
		try{
			return AssetPool.getObject(name, TileMap.class);
		}catch(NoSuchElementException e){
			TileMap map = new TileMap(name);
			AssetPool.addObject(name, map);
			return map;
		}
	}

	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public int getTileCount(){ return width*height; }
	public TiledTileLayer getTileLayer(Layer layer){
		return switch(layer){
			case BACKGROUND -> layers.getFirst();
			case FOREGROUND -> layers.getLast();
			default -> throw new NoSuchElementException();
		};
	}

	public boolean wasLoaded(){ return loaded; }

	// SETTERS
	private void loadMap(){
        TiledMap tm = READER.getMap(MAP_FILE_FORMAT.formatted(name));

		width = tm.getWidth();
		height = tm.getHeight();
		layers = tm.getTopLevelLayers().stream()
			.filter(TiledTileLayer.class::isInstance)
			.map(TiledTileLayer.class::cast)
			.toList();
	}
	public void load(){
		loadMap();

		loaded = true;
    }

	// INNER CLASSES
	public static class ResourceTiledReader extends TiledReader{

		static{
			Arrays.stream(TiledReader.LOGGER.getHandlers())
				.forEach(TiledReader.LOGGER::removeHandler);

			TiledReader.LOGGER.setParent(Game.LOGGER);
		}

		// GETTERS
		@Override
		public String getCanonicalPath(String path){
			Path p = Path.of(path);

			return p.startsWith(MAPS_FOLDER) ? path
				: Path.of(MAPS_FOLDER, path).toString();
		}
		@Override
		public InputStream getInputStream(String path){
			Optional<InputStream> opIS = Optional.ofNullable(
				ClassLoader.getSystemResourceAsStream(path)
			);
			if(opIS.isEmpty())
				throw new UncheckedIOException(new FileNotFoundException(
					"No such %s tilemap component (%s);".formatted(
						path.split(FileSystems.getDefault().getSeparator())[0],
						path
					)
				));

			return opIS.get();
		}
		@Override
		public TiledResource getCachedResource(String path){ return null; }

		// SETTERS
		@Override
		protected void setCachedResource(String path, TiledResource resource){}
		@Override
		protected void removeCachedResource(String path){
			throw new UnsupportedOperationException();
		}
		@Override
		protected void clearCachedResources(){
			throw new UnsupportedOperationException();
		}

		// FUNCTIONS
		@Override
		public String joinPaths(String basePath, String relativePath){
			return getCanonicalPath(relativePath);
		}
	}
}