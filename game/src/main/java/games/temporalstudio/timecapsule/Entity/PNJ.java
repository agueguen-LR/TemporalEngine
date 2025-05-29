package games.temporalstudio.timecapsule.Entity;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class PNJ extends Entity {



    public PNJ(String name, Vector2f scale, Vector4f color){
        super(name, scale, new Vector2f(),color);
    }

    public PNJ(String name, Vector2f scale, Vector2f position, Vector4f color){
        super(name, scale, position,color);

    }


}
