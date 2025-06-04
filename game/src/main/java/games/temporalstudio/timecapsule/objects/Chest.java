package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.Entity.Player;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Chest implements TimeObject{
    private GameObject chest;
    private InventoryObject inventory;
    private boolean open;

    public Chest(String name, float x, float y, Player player){
        open = false;
        this.chest = new GameObject(name); // Create a new GameObject for the key
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the key
        AABB shape = new AABB(transform); // Create a component shape for the key
        Collider2D collider = new Collider2D(shape); // Create a collider for the key
        Render render = new ColorRender(new Vector4f(1, 1, 1, 1)); // Create a render component for the key
        collider.setRigid(true);

        this.chest.addComponent(transform); // Add the Transform component to the key
        this.chest.addComponent(collider); // Add the Collider2D component to the key
        this.chest.addComponent(render); // Add the Render component to the key
    }

    public void open(){
        System.out.println("Le coffre est enfin ouvert");
    }

    public GameObject getGameObject(){
        return chest;
    }

}
