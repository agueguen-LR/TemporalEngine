package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CompleteKey implements InventoryObject{

    private static final float MAX_ACTIVATION_DISTANCE = 0.4f;

    private GameObject completeKey;
    private Triggerable triggerable;

    public CompleteKey(String name, float x, float y, GameObject collectableBy, Player player, Chest chest){
        this.completeKey = new GameObject(name); // Create a new GameObject for the key
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the key
        AABB shape = new AABB(transform); // Create a component shape for the key
        Collider2D collider = new Collider2D(shape); // Create a collider for the key
        Render render = new ColorRender(new Vector4f(0, 1, 1, 1)); // Create a render component for the key
        this.triggerable = new Triggerable((thisKeyFragment) -> {
            Transform chestTrans = chest.getGameObject().getComponent(Transform.class);
            Transform playerTrans = player.getGameObject().getComponent(Transform.class);
            System.out.println(playerTrans.getPosition().distance(chestTrans.getPosition()));
            if(playerTrans.getPosition().distance(chestTrans.getPosition()) <= MAX_ACTIVATION_DISTANCE) {
                chest.open();
                player.removeFromInventory(this);
            }
        });
        this.completeKey.addComponent(triggerable);

// Set the action to be performed when the key collide with another object
        collider.setOnIntersects((thisKey, collidingObject) -> {
            if (!(collidingObject instanceof GameObject collector)) {
                return; // Ensure the colliding object is a GameObject
            }
            if (collector == collectableBy) { // Check if the colliding object is the collectableBy object
// Logic for when the key collides with the player
                System.out.println("Fragment key collected by player!");
                this.completeKey.removeComponent(collider);
                this.completeKey.removeComponent(render);
                this.completeKey.removeComponent(transform);
                player.addToInventory(this);
            }
        });
        this.completeKey.addComponent(transform); // Add the Transform component to the key
        this.completeKey.addComponent(collider); // Add the Collider2D component to the key
        this.completeKey.addComponent(render); // Add the Render component to the key
    }

    public GameObject getGameObject() {
        return completeKey; // Return the GameObject representing the completeKey
    }
    public Triggerable getTriggerable(){
        return triggerable;
    }
}
