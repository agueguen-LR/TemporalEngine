package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Tree {
    private GameObject gameObject;

    public Tree(String name, float x, float y) {
        this.gameObject = new GameObject(name);
        Transform transform = new Transform(new Vector2f(x, y));
        Render render = new ColorRender(new Vector4f(0.8f, 0.2f, 0.2f, 1));

        this.gameObject.addComponent(transform);
        this.gameObject.addComponent(render);




    }
}
