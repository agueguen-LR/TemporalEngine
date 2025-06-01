package games.temporalstudio.timecapsule.objects;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;
public class CompleteKey extends Key{
    private GameObject completeKey;

    public CompleteKey(String name, float x, float y, GameObject collectableBy){
        this.completeKey = new GameObject(name); // Create a new GameObject for the completeKey
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the completeKey
        AABB shape = new AABB(transform); // Create a component shape for the completeKey
        Collider2D collider = new Collider2D(shape); // Create a collider for the completeKey

// Set the action to be performed when the completeKey collides with another object
        collider.setOnCollide((thisCompleteKey, collidingObject) -> {
            if (!(collidingObject instanceof GameObject collector)){
                return; // Ensure the colliding object is a GameObject
            }
            if (collector != collectableBy) { // Check if the colliding object is the collectableBy object
// Logic for when the completeKey collides with the player
                System.out.println("Complete key collected by player!");
// You can add more logic here, like increasing score or removing the completeKey
            }
        });

        this.completeKey.addComponent(transform); // Add the Transform component to the completeKey
        this.completeKey.addComponent(collider); // Add the Collider2D component to the completeKey
    }

    public GameObject getCompleteKey() {
        return completeKey; // Return the GameObject representing the completeKey
    }
}