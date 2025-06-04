package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Medusa extends Entity{

    private Player player;
    private static double MAX_DISTANCE=1.5f;
    private static double LIMITE_ECART=4;
    private Following following;

    public Medusa(String name, Vector2f scale, Vector4f color, Player player) {
        super(name,new Vector2f(player.getTransform().getPosition().x-1, player.getTransform().getPosition().y),
                scale, new float[]{1, 10, 0.1f, 100f} ,color, "meduse");
        Collider2D collider=new Collider2D((new AABB(transform)));
        this.player=player;
        p.addComponent(collider);

        getRender().setAnimChooser(context -> {
            Vector2f vel = physicsBody.getVelocity();
            float angle = vel.angle(new Vector2f(1, 0));
            String tileName;

            if(angle > Math.PI/4*3 || angle < -Math.PI/4*3)
                tileName = "left_walk";
            else if(angle < Math.PI/4*3 && angle > Math.PI/4)
                tileName = "face_walk";
            else if(angle > -Math.PI/4*3 && angle < -Math.PI/4)
                tileName = "back_walk";
            else
                tileName = "right_walk";

            return tileName;
        });

        following=new Following(this,player);
        p.addComponent(following);
    }

    public PhysicsBody getPhysicsBody() {return p.getComponent(PhysicsBody.class);}
    public GameObject getGameObject(){return p;}

    public static double getMaxDistance() {
        return MAX_DISTANCE;
    }
    public static double getLimiteEcart() {return LIMITE_ECART;}
}
