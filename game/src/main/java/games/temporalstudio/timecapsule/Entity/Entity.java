package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.SpriteRender;
import games.temporalstudio.timecapsule.objects.TimeObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class Entity implements TimeObject {

    protected GameObject p;
    protected Transform transform;
    protected PhysicsBody physicsBody;


    public Entity(String name, Vector2f position, Vector2f scale,float[] physBody, Vector4f color, String texture) {
        p=new GameObject(name);
        if (physBody.length != 4){
            throw new IllegalArgumentException("physBody.length must be 4");
        }
        transform = new Transform(position, scale);
        physicsBody = new PhysicsBody(physBody[0], physBody[1], physBody[2], physBody[3]);
        SpriteRender render = new SpriteRender(
                texture, "face_walk"
        );

        render.setAnimChooser(context -> {
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

        p.addComponent(transform);
        p.addComponent(physicsBody);
        p.addComponent(render);
    }

    public Transform getTransform() {return this.transform;}
    public PhysicsBody getPhysicsBody() {return this.physicsBody;}
    public Render getRender() {return p.getComponent(Render.class);}
    public GameObject getGameObject() {return p;}

    public void moveUp(float force) {
        getPhysicsBody().applyForce(new Vector2f(0, force));}

    public void moveDown(float force){
        getPhysicsBody().applyForce(new Vector2f(0, -force));}

    public void moveLeft(float force){
        getPhysicsBody().applyForce(new Vector2f(-force, 0));}

    public void moveRight (float force){
        getPhysicsBody().applyForce(new Vector2f(force, 0));}

    public void jump (float force){
        getPhysicsBody().applyForce(new Vector2f(0, force));
    }


}

