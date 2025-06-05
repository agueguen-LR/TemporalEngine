package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import games.temporalstudio.timecapsule.Entity.Player;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

public class SeedEmplacement implements TimeObject{
    private GameObject gameObject;

    public SeedEmplacement(String name, float x, float y, Player player, Scene pastScene, Scene futurScene) {
        this.gameObject = new GameObject(name);
        Transform transform = new Transform(new Vector2f(x, y));
        Render render = new TileRender("past", "big_seed_emplacement");

        Collider2D collider2D = new Collider2D(new AABB(transform));
        collider2D.setOnIntersects((context, other) -> {
            if (other instanceof GameObject && other == player.getGameObject()) {
                //Checking if the player have a seed
                if (player.inventoryContains(Seed.class)){
                    if (player.getGameObject().getComponent(Input.class).isControlPressed(GLFW_KEY_E)){
                        gameObject.removeComponent(render);
                        gameObject.removeComponent(collider2D);
                        gameObject.removeComponent(transform);


                        player.removeFromInventory(player.getInventoryObject(Seed.class));
                        Tree pastTree = new Tree(pastScene.getName() + "_tree", x, y);
                        //pastScene.addGameObject(pastTree.getGameObject());
                        Tree futurTree = new Tree(futurScene.getName() + "_tree", x, y);
                        futurScene.addGameObject(futurTree.getGameObject());
                        ArrayList<CardinalDirections> directionsTest = new ArrayList<CardinalDirections>();
                        directionsTest.add(CardinalDirections.NORTH);


                        new Roots("_roots", x, y, directionsTest, futurScene);

                        pastScene.removeGameObject(this.gameObject);
                    }
                }
            }
        });
        this.gameObject.addComponent(transform);
        this.gameObject.addComponent(render);
        this.gameObject.addComponent(collider2D);



    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }


}
