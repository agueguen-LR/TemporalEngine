package games.temporalstudio.temporalengine.rendering.shader;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL20.*;

public enum ShaderType{

    UNDEFINED("undefined", 0),
    VERTEX("vertex", GL_VERTEX_SHADER),
    FRAGMENT("fragment", GL_FRAGMENT_SHADER);

	public static final Pattern TYPE_LINE_PATTERN
		= Pattern.compile("^#type (?<type>[a-zA-Z]+)$");

    private final String name;
    private final int glID;

    private ShaderType(String name, int glID){
		this.name = name;
        this.glID = glID;
    }

    // GETTERS
    public static ShaderType getByName(String name){
        return Arrays.stream(values())
			.filter(st -> st.getName().equals(name))
			.findFirst().orElse(null);
    }
    public static boolean isSupported(String name){
        return Arrays.stream(values())
			.anyMatch(st -> st.getName().equals(name));
    }

    public String getName(){ return name; }
    public int getGlID(){ return glID; }
}