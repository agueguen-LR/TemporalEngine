package games.temporalstudio.temporalengine.rendering.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

public class Shader {

    private static final String SHADERS_FOLDER = "shaders";
    private static final String SHADER_FILE_FORMAT = "%s.glsl";

    private String name;
    private Map<ShaderType, String> shadersSource = new HashMap<>();
    private Map<ShaderType, Integer> shadersId = new HashMap<>();

    private int id;

    public Shader(String name){
        this.name = name;
        this.id = glCreateProgram();

		for(ShaderType type : ShaderType.values()){
			if(type.getGlID() != 0){
				int shId = glCreateShader(type.getGlID());
				shadersId.put(type, shId);
				glAttachShader(id, shId);
			}
		}
    }

    // GETTERS
    private Path getSourcePath(){
        return Path.of(SHADERS_FOLDER, SHADER_FILE_FORMAT.formatted(name));
    }

    // SETTERS
	private void load(InputStream in){
        BufferedReader br = new BufferedReader(
			new InputStreamReader(in, StandardCharsets.UTF_8)
		);

		shadersSource.put(ShaderType.UNDEFINED, "");

		Iterator<String> lines = br.lines().iterator();
		ShaderType type = ShaderType.UNDEFINED;
		while(lines.hasNext()){
			String l = lines.next();
			Matcher m = ShaderType.TYPE_LINE_PATTERN.matcher(l);

			if(m.matches()){
				String rawType = m.group("type");

				if(!ShaderType.isSupported(rawType))
					throw new IllegalArgumentException(
						"Not supported %s shader type".formatted(rawType)
					);

				type = ShaderType.getByName(rawType);
				shadersSource.put(type, "");
			}else
				shadersSource.put(type,
					shadersSource.get(type).concat(l).concat("\n")
				);
		}
	}
    private void load(Path path){
		Optional<InputStream> opIs = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(path.toString())
		);
        if(opIs.isEmpty())
            throw new IllegalArgumentException(
				"No such shader named %s;".formatted(this.name)
			);

		load(opIs.get());
    }
    public void load(){
		load(getSourcePath());
    }

    // FUNCTIONS
    public void compile(){
		shadersId.forEach((t, shId) -> {
			glShaderSource(shId, shadersSource.get(t));
			glCompileShader(shId);

            if(glGetShaderi(shId, GL_COMPILE_STATUS) == GL_FALSE)
                throw new RuntimeException(
					"%s shader compilation failed (%s);".formatted(
						t.getName(),
						glGetShaderInfoLog(shId,
							glGetShaderi(id, GL_INFO_LOG_LENGTH)
						)
					)
				);
		});
    }
    public void link(){
        glLinkProgram(id);

        if(glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException(
				"%s program linking failed (%s);".formatted(
					name,
					glGetProgramInfoLog(id,
						glGetProgrami(id, GL_INFO_LOG_LENGTH)
					)
				)
			);
    }

    public void use(){
		glUseProgram(id);
	}
    public void detach(){
		glUseProgram(0);
	}

    public void uploadMatrix4f(String name, Matrix4f data){
        glUniformMatrix4fv(glGetUniformLocation(id, name),
			false, data.get(BufferUtils.createFloatBuffer(4*4))
        );
    }
}