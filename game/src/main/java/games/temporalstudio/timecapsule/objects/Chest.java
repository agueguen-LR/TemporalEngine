package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
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

public class Chest implements TimeObject{
    private GameObject chest;
    private ArrayList<InventoryObject> inventory;
    private boolean open;
    private Player player;

    public Chest(String name, float x, float y, Player player){
        open = false;
        this.player = player;
        this.chest = new GameObject(name); // Create a new GameObject for the chest
        Transform transform = new Transform(new Vector2f(x, y)); // Set the position of the chest
        AABB shape = new AABB(transform); // Create a component shape for the chest
        Collider2D collider = new Collider2D(shape); // Create a collider for the chest
        Render render = new TileRender("past", "chest"); // Create a render component for the chest
        collider.setRigid(true);

        this.chest.addComponent(transform); // Add the Transform component to the chest
        this.chest.addComponent(collider); // Add the Collider2D component to the chest
        this.chest.addComponent(render); // Add the Render component to the chest
    }

    public void open(){
        Game.LOGGER.info("Le coffre est enfin ouvert !! ");
    }

    public GameObject getGameObject(){
        return chest;
    }

}
