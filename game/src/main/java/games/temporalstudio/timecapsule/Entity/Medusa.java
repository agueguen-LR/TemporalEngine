package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Medusa extends Entity{

    private Player player;
    private static double MAX_DISTANCE=1.5f;
    private Following following;

    public Medusa(String name, int x, int y, Vector2f scale, Vector4f color, Player player) {
        super(name,new Vector2f(x,y), new Vector2f(1,1), new float[]{1, 10, 0.1f, 20f} ,color);
        Collider2D collider=new Collider2D((new AABB(transform)));
        this.player=player;
        p.addComponent(collider);

        following=new Following(this,player);
        p.addComponent(following);
    }

    public PhysicsBody getPhysicsBody() {return p.getComponent(PhysicsBody.class);}
    public GameObject getGameObject(){return p;}

    public static double getMaxDistance() {
        return MAX_DISTANCE;
    }
}
