package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.Entity.Player;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class CompleteKey implements InventoryObject{

    private static final float MAX_ACTIVATION_DISTANCE = 0.4f;

    private GameObject completeKey;
    private Triggerable triggerable;

    public CompleteKey(String name, float x, float y, GameObject collectableBy, Player player, Chest chest){
        this.completeKey = new GameObject(name); // Create a new GameObject for the completeKey
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the completeKey
        AABB shape = new AABB(transform); // Create a component shape for the completeKey
        Collider2D collider = new Collider2D(shape); // Create a collider for the completeKey
        Render render = new ColorRender(new Vector4f(0, 1, 1, 1)); // Create a render component for the completeKey
        this.triggerable = new Triggerable((thisKeyFragment) -> {
            Transform chestTrans = chest.getGameObject().getComponent(Transform.class);
            Transform playerTrans = player.getGameObject().getComponent(Transform.class);

			Vector2f chestRadius = chestTrans.getScale().div(2, new Vector2f());
			Vector2f playerRadius = playerTrans.getScale().div(2, new Vector2f());

			Vector2f chestCenter = chestTrans.getPosition().add(chestRadius, new Vector2f());
			Vector2f playerCenter = playerTrans.getPosition().add(playerRadius, new Vector2f());

            if(chestCenter.distance(playerCenter) <= MAX_ACTIVATION_DISTANCE + chestRadius.length() + playerRadius.length()) {
                chest.open();
                player.removeFromInventory(this);
            }
        });
        this.completeKey.addComponent(triggerable);

// Set the action to be performed when the completeKey collide with another object
        collider.setOnIntersects((thisKey, collidingObject) -> {
            if (!(collidingObject instanceof GameObject collector)) {
                return; // Ensure the colliding object is a GameObject
            }
            if (collector == collectableBy) { // Check if the colliding object is the collectableBy object
// Logic for when the completeKey collides with the player
                Game.LOGGER.info("Fragment key collected by player!");
                this.completeKey.removeComponent(collider);
                this.completeKey.removeComponent(render);
                this.completeKey.removeComponent(transform);
                player.addToInventory(this);
            }
        });
        this.completeKey.addComponent(transform); // Add the Transform component to the completeKey
        this.completeKey.addComponent(collider); // Add the Collider2D component to the completeKey
        this.completeKey.addComponent(render); // Add the Render component to the completeKey
    }

    public GameObject getGameObject() {
        return completeKey; // Return the GameObject representing the completeKey
    }
    public Triggerable getTriggerable(){
        return triggerable;
    }
}
