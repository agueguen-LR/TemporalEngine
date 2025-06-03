package games.temporalstudio.timecapsule.objects;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;
public class CompleteKey implements InventoryObject{
    private GameObject gameObject;
    private Triggerable triggerable;

    public CompleteKey(String name,Chest chest , GameObject collectableBy){
        this.gameObject = new GameObject(name);// Create a new GameObject for the completeKey
        chest.openChest();
    }

    public GameObject getGameObject() {
        return gameObject; // Return the GameObject representing the completeKey
    }
    public Triggerable getTriggerable(){
        return triggerable;
    }
}
