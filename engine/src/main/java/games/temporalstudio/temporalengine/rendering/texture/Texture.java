package games.temporalstudio.temporalengine.rendering.texture;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Map.Entry;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.json.JsonParser;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import games.temporalstudio.temporalengine.utils.AssetPool;
import games.temporalstudio.temporalengine.utils.AssetPoolObject;
import games.temporalstudio.temporalengine.utils.NIOUtils;
import kotlin.text.Charsets;

public class Texture implements AssetPoolObject{

	private static final int TILE_SIZE = 64;

	private static final String TEXTURES_FOLDER = "assets/textures";
	private static final String IMAGE_FILE_FORMAT = "%s.png";
	private static final String METADATA_FILE_FORMAT = "%s.json";
	private static final String DEFAULT_TEXTURE_NAME = "default";

	private String name;
	private Vector2i size;
	private Map<String, Tile> tiles = new HashMap<>();

	private int id;
	private boolean loaded = false;

	protected Texture(String name){
		this.name = name;
		this.id = glGenTextures();

		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		unbind();
	}

	// GETTERS
	public static Texture get(String name){
		try{
			return AssetPool.getObject(name, Texture.class);
		}catch(NoSuchElementException e){
			Texture tex = new Texture(name);
			AssetPool.addObject(name, tex);
			return tex;
		}
	}
    private static Path getImagePath(String name){
        return Path.of(TEXTURES_FOLDER, IMAGE_FILE_FORMAT.formatted(name));
    }
    private static Path getMetadataPath(String name){
        return Path.of(TEXTURES_FOLDER, METADATA_FILE_FORMAT.formatted(name));
    }

	public Vector2i getSize(){ return new Vector2i(size); }
	public Tile getTile(String tileName){
		return tiles.entrySet().stream()
			.filter(e -> e.getKey().equals(tileName))
			.map(Entry::getValue)
			.findFirst().orElse(null);
	}
	public Tile getTile(int id){
		return tiles.entrySet().stream()
			.filter(e -> e.getValue().tiledID() == id)
			.map(Entry::getValue)
			.findFirst().orElse(null);
	}
	public List<Vector2f> getCoords(Tile tile, int state){
		Vector2f offset = new Vector2f(
			1f/(size.x()/TILE_SIZE), 1f/(size.y()/TILE_SIZE)
		);

		Vector2f positionf = new Vector2f(tile.position(state));
		Vector2f scalef = new Vector2f(tile.scale());

		return List.of(
			new Vector2f(0, 0).mul(scalef).add(positionf).mul(offset),
			new Vector2f(1, 0).mul(scalef).add(positionf).mul(offset),
			new Vector2f(1, 1).mul(scalef).add(positionf).mul(offset),
			new Vector2f(0, 1).mul(scalef).add(positionf).mul(offset)
		);
	}
	public List<Vector2f> getCoords(Tile tile){
		return getCoords(tile, TileRender.DEFAULT_STATE);
	}
	public boolean wasLoaded(){ return loaded; }

	// SETTERS
	private ByteBuffer loadImageBuffer() throws IOException{
		try{
			return NIOUtils.getResourceAsByteBuffer(
				Texture.getImagePath(name).toString()
			);
		}catch (Exception e){
			Game.LOGGER.warning(
				"No such %s texture; Using default texture."
					.formatted(name)
			);

			return NIOUtils.getResourceAsByteBuffer(
				Texture.getImagePath(DEFAULT_TEXTURE_NAME).toString()
			);
		}
	}
	private void loadImage(){
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		ByteBuffer texture;
		try{
			texture = loadImageBuffer();
		}catch(IOException e){
			throw new UncheckedIOException(
				"Failed to load any texture: %s;".formatted(name), e
			);
		}

		ByteBuffer pixels = stbi_load_from_memory(texture,
			w, h, channels, 4
		);

		if(pixels != null){
			bind();
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
				w.get(0), h.get(0), 0,
				GL_RGBA, GL_UNSIGNED_BYTE, pixels
			);
			unbind();

			size = new Vector2i(w.get(0), h.get(0));
		}else
			throw new UncheckedIOException(new IOException(
				"Failed to load texture from memory;"
			));

		stbi_image_free(pixels);
	}
	protected void loadMetadata(){
		Optional<InputStream> opIs = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(
				Texture.getMetadataPath(name).toString()
			)
		);
        if(opIs.isEmpty()) return;

		try(InputStream is = opIs.get()){
			Config c = new JsonParser().parse(is, Charsets.UTF_8);

			c.entrySet().stream()
				.map(e -> {
					if(!(e.getValue() instanceof Config tc))
						throw new IllegalArgumentException(
							"Tileset entry %s isn't an object;"
								.formatted(e.getKey())
						);

					return Map.entry(e.getKey(), Tile.fromConfig(tc));
				})
				.forEach(e -> tiles.put(e.getKey(), e.getValue()));
		}catch(IOException e){
			throw new UncheckedIOException(
				"Failed to load and parse tileset;", e
			);
		}
	}
	public void load(){
		loadImage();
		loadMetadata();

		loaded = true;
    }

	// FUNCTIONS
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}

	// INNER CLASSES
	public static record Tile(
		List<Vector2i> positions,
		Vector2i scale,
		int tiledID
	){

		private static final String POSITION_CONFIG_FIELD = "position";
		private static final String POSITIONS_CONFIG_FIELD = "positions";
		private static final String SCALE_CONFIG_FIELD = "scale";
		private static final String TILEDID_CONFIG_FIELD = "tiledID";

		// GETTERS
		public Vector2i position(int state){
			return positions.get(state);
		}

		// FUNCTIONS
		public static Tile fromConfig(Config config){
			List<Integer> rawPosition;
			List<List<Integer>> rawPositions;
			List<Integer> rawScale;
			int rawTiledID = -1;

			if(
				(
					!config.contains(POSITION_CONFIG_FIELD)
					&& !config.contains(POSITIONS_CONFIG_FIELD)
				)
				|| !config.contains(SCALE_CONFIG_FIELD)
			)
				throw new IllegalArgumentException(
					"Missing tile properties in config;"
				);

			if(config.contains(POSITION_CONFIG_FIELD)){
				rawPosition = config.get(POSITION_CONFIG_FIELD);
				rawPositions = List.of(rawPosition);
			}else
				rawPositions = config.get(POSITIONS_CONFIG_FIELD);

			rawScale = config.get(SCALE_CONFIG_FIELD);

			if(config.contains(TILEDID_CONFIG_FIELD))
				rawTiledID = config.get(TILEDID_CONFIG_FIELD);

			return new Tile(
				rawPositions.stream()
					.map(pos -> new Vector2i(
						pos.get(0), pos.get(1)
					))
					.toList(),
				new Vector2i(rawScale.get(0), rawScale.get(1)),
				rawTiledID
			);
		}
	}
}
