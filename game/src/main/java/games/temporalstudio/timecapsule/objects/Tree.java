package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import org.joml.Vector2f;

public class Tree implements TimeObject{
    private GameObject gameObject;

    public Tree(String name, float x, float y) {
        this.gameObject = new GameObject(name);
        Transform transform = new Transform(new Vector2f(x-1, y),new Vector2f(3,6));
        Render render = new TileRender("future", "tree");

        this.gameObject.addComponent(transform);
        this.gameObject.addComponent(render);


    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }
}
