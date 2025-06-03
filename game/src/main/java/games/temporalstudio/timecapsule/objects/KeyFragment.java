package games.temporalstudio.timecapsule.objects;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class KeyFragment extends Key implements InventoryObject{
    private final GameObject keyfragment;
    private Triggerable triggerable;

    public KeyFragment(String name, float x, float y, GameObject collectableBy, Player player, Chest chest){
        this.keyfragment = new GameObject(name); // Create a new GameObject for the key
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the key
        AABB shape = new AABB(transform); // Create a component shape for the key
        Collider2D collider = new Collider2D(shape); // Create a collider for the key
        Render render = new ColorRender(new Vector4f(0, 1, 0, 1)); // Create a render component for the key
        this.triggerable = new Triggerable((thisKeyFragment) -> {
            player.removeFromInventory(this);
        });
        this.keyfragment.addComponent(triggerable);

// Set the action to be performed when the key collide with another object
        collider.setOnIntersects((thisKey, collidingObject) -> {
            if (!(collidingObject instanceof GameObject collector)){
                return;// Ensure the colliding object is a GameObject
            }
            if (collector == collectableBy) { // Check if the colliding object is the collectableBy object
// Logic for when the key collides with the player
                System.out.println("Fragment key collected by player!");
                this.keyfragment.removeComponent(collider);
                this.keyfragment.removeComponent(render);
                this.keyfragment.removeComponent(transform);
                player.fragments.add(this);
                player.addToInventory(this);
                if (player.fragments.size() == 2){
                    player.buildCompleteKey(chest);
                }
            }
        });
        this.keyfragment.addComponent(transform); // Add the Transform component to the key
        this.keyfragment.addComponent(collider); // Add the Collider2D component to the key
        this.keyfragment.addComponent(render); // Add the Render component to the key
    }

    public GameObject getGameObject() {
        return keyfragment; // Return the GameObject representing the key
    }

    public Triggerable getTriggerable(){
        return triggerable;
    }
}