package games.temporalstudio.temporalengine.rendering.texture;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.utils.AssetPool;
import games.temporalstudio.temporalengine.utils.AssetPoolObject;
import games.temporalstudio.temporalengine.utils.NIOUtils;

public class Texture implements AssetPoolObject{

	private static final int TILE_SIZE = 64;

	private static final String TEXTURES_FOLDER = "assets/textures";
	private static final String TEXTURE_FILE_FORMAT = "%s.png";
	private static final Pattern TEXTURE_FILE_PATTERN = Pattern.compile(
		"^.*/([a-zA-Z0-9]+).png$"
	);
	private static final String DEFAULT_TEXTURE_NAME = "default";

	private String name;
	private Vector2i size;

	private int id;

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
        return Path.of(TEXTURES_FOLDER, TEXTURE_FILE_FORMAT.formatted(name));
    }

	public Vector2i getSize(){ return new Vector2i(size); }
	public List<Vector2f> getCoords(Vector2i position, Vector2i scale){
		Vector2f offset = new Vector2f(
			1f/(size.x()/TILE_SIZE), 1f/(size.y()/TILE_SIZE)
		);

		Vector2f positionf = new Vector2f(position);
		Vector2f scalef = new Vector2f(scale);

		return List.of(
			new Vector2f(0, 0).add(positionf).mul(scalef).mul(offset),
			new Vector2f(1, 0).add(positionf).mul(scalef).mul(offset),
			new Vector2f(1, 1).add(positionf).mul(scalef).mul(offset),
			new Vector2f(0, 1).add(positionf).mul(scalef).mul(offset)
		);
	}

	// SETTERS
	private static ByteBuffer loadImageBuffer(Path path) throws IOException{
		try{
			return NIOUtils.getResourceAsByteBuffer(path.toString());
		}catch (Exception e){
			Matcher m = TEXTURE_FILE_PATTERN.matcher(path.toString());
			m.matches();

			Game.LOGGER.warning(
				"Failed to load %s texture; Using default texture."
					.formatted(m.group(0))
			);

			return NIOUtils.getResourceAsByteBuffer(
				Texture.getImagePath(DEFAULT_TEXTURE_NAME).toString()
			);
		}
	}
	private void load(Path path){
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		ByteBuffer texture;
		try{
			texture = Texture.loadImageBuffer(path);
		}catch(IOException e){
			Matcher m = TEXTURE_FILE_PATTERN.matcher(path.toString());
			m.matches();

            throw new UncheckedIOException(
				"Failed to load any texture: %s;".formatted(path), e
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
    public void load(){
		load(Texture.getImagePath(name));
    }

	// FUNCTIONS
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
}
