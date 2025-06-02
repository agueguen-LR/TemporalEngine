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
import games.temporalstudio.temporalengine.utils.AssetPool;
import games.temporalstudio.temporalengine.utils.AssetPoolObject;
import games.temporalstudio.temporalengine.utils.NIOUtils;
import kotlin.text.Charsets;

public class Texture implements AssetPoolObject{

	private static final int TILE_SIZE = 64;

	private static final String TEXTURES_FOLDER = "assets/textures";
	private static final String IMAGE_FILE_FORMAT = "%s.png";
	private static final String TILESET_FILE_FORMAT = "%s.json";
	private static final String DEFAULT_TEXTURE_NAME = "default";

	private String name;
	private Vector2i size;
	private Map<String, Tile> tiles = new HashMap<>();

	private int id;
	private boolean loaded = false;

	private Texture(String name){
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
    private static Path getTilesetPath(String name){
        return Path.of(TEXTURES_FOLDER, TILESET_FILE_FORMAT.formatted(name));
    }

	public Vector2i getSize(){ return new Vector2i(size); }
	public Tile getTile(String tileName){
		Entry<String, Tile> entry = tiles.entrySet().stream()
			.filter(e -> e.getKey().equals(tileName))
			.findFirst().orElse(null);

		return entry != null ? entry.getValue() : null;
	}
	public List<Vector2f> getCoords(Tile tile){
		Vector2f offset = new Vector2f(
			1f/(size.x()/TILE_SIZE), 1f/(size.y()/TILE_SIZE)
		);

		Vector2f positionf = new Vector2f(tile.position());
		Vector2f scalef = new Vector2f(tile.scale());

		return List.of(
			new Vector2f(0, 0).mul(scalef).add(positionf).mul(offset),
			new Vector2f(1, 0).mul(scalef).add(positionf).mul(offset),
			new Vector2f(1, 1).mul(scalef).add(positionf).mul(offset),
			new Vector2f(0, 1).mul(scalef).add(positionf).mul(offset)
		);
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
				"Failed to load %s texture; Using default texture."
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
	private void loadTileset(){
		Optional<InputStream> opIs = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(
				Texture.getTilesetPath(name).toString()
			)
		);
        if(opIs.isEmpty()) return;

		try(InputStream is = opIs.get()){
			Config c = new JsonParser().parse(is, Charsets.UTF_8);

			c.entrySet().forEach(entry -> {
				if(!(entry.getValue() instanceof Config tc))
					throw new IllegalArgumentException(
						"Tileset entry %s isn't an object;"
							.formatted(entry.getKey())
					);

				tiles.put(entry.getKey(), Tile.fromConfig(tc));
			});
		}catch(IOException e){
			throw new UncheckedIOException(
				"Failed to load and parse tileset;", e
			);
		}
	}
	public void load(){
		loadImage();
		loadTileset();

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
	public static record Tile(Vector2i position, Vector2i scale){

		private static final String POSITION_CONFIG_FIELD = "position";
		private static final String SCALE_CONFIG_FIELD = "scale";

		// FUNCTIONS
		public static Tile fromConfig(Config config){
			List<Integer> rawPos, rawScale;

			if(!config.contains(POSITION_CONFIG_FIELD)
				|| !config.contains(SCALE_CONFIG_FIELD)
			)
				throw new IllegalArgumentException(
					"Missing tile properties in config;"
				);

			rawPos = config.get("position");
			rawScale = config.get("scale");

			return new Tile(
				new Vector2i(rawPos.get(0), rawPos.get(1)),
				new Vector2i(rawScale.get(0), rawScale.get(1))
			);
		}
	}
}
