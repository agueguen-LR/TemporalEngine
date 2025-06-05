package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import org.joml.Vector2f;

class Root implements TimeObject {
    private GameObject gameObject;


    //Racine nord
    Root(String name, float oX, float oY, Scene futurScene) {

        gameObject = new GameObject(name);
        Transform transform = new Transform(new Vector2f(oX, oY),new Vector2f(1,3));


        Render render = new TileRender("past","small_root_bridge");
        //Render render = new ColorRender(new Vector4f(0.8f, 0.2f, 0.2f, 1));
        Collider2D newColider2d = new Collider2D(new AABB(transform));
        newColider2d.setOnIntersects((objRoot, other) -> {
            if (other instanceof GameObject otherGameObject) {
                if (otherGameObject.getName().startsWith("Wall")) {

                    //otherGameObject.removeComponent(otherGameObject.getComponent(Collider2D.class));
                    futurScene.removeGameObject(otherGameObject);
                }

            }
        });
        this.gameObject.addComponent(transform);
        this.gameObject.addComponent(newColider2d);
        this.gameObject.addComponent(render);

        futurScene.addGameObject(gameObject);
    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }
}