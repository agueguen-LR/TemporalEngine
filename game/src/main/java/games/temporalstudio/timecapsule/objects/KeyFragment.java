package games.temporalstudio.timecapsule.objects;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;

import java.util.List;

public class KeyFragment extends Key {
    private final GameObject keyfragment;
    private static List<KeyFragment> fragments;
    public KeyFragment(String name, float x, float y, GameObject collectableBy){
        this.keyfragment = new GameObject(name); // Create a new GameObject for the key
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the key
        AABB shape = new AABB(transform); // Create a component shape for the key
        Collider2D collider = new Collider2D(shape); // Create a collider for the key

// Set the action to be performed when the key collide with another object
        collider.setOnCollide((thisKey, collidingObject) -> {
            if (!(collidingObject instanceof GameObject collector)){
                return; // Ensure the colliding object is a GameObject
            }
            if (collector != collectableBy) { // Check if the colliding object is the collectableBy object
// Logic for when the key collides with the player
                System.out.println("Fragment key collected by player!");
                fragments.add(this);

// You can add more logic here, like increasing score or removing the key
            }
        });
        if (fragments.size() == 2){
            for (KeyFragment fragment : fragments){
                this.keyfragment.removeComponent();
            }
        }
        this.keyfragment.addComponent(transform); // Add the Transform component to the key
        this.keyfragment.addComponent(collider); // Add the Collider2D component to the key
    }

    public GameObject getKey() {
        return keyfragment; // Return the GameObject representing the key
    }
}