package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class Entity extends GameObject {

    protected Transform transform;
    protected Collider2D collider;
    protected PhysicsBody physicsBody;
    protected Render render;
    private static final float MALUSSLOWDOWN=0.5f;
    private static final float LIMITEGAUGE=0.4f;

    public Entity(String name, Vector2f scale, Vector2f position, Vector4f color) {
        super(name);
        transform = new Transform(scale, position);
        collider=new Collider2D(transform);
        collider.setShape(new AABB(transform));
        physicsBody = new PhysicsBody(1.0f, 1.0f, 0.1f, 1.0f);
        render=new ColorRender(color);
        this.addComponent(transform);
        this.addComponent(collider);
        this.addComponent(physicsBody);
        this.addComponent(render);
    }

    /*public Entity(String name, Vector2f scale, Vector2f position,float mass, float maxVelocity, float minVelocity, float drag) {
        super(name);
        transform = new Transform(scale, position);
        collider=new Collider2D(transform);
        physicsBody = new PhysicsBody(mass, maxVelocity, minVelocity, drag);
        this.addComponent(transform);
        this.addComponent(collider);
        this.addComponent(physicsBody);
    }*/

    public Transform getTransform() {return transform;}
    public PhysicsBody getPhysicsBody() {return physicsBody;}
    public void setTransform(Transform transform) {this.transform = transform;}
    public void setPhysicsBody(PhysicsBody physicsBody) {this.physicsBody = physicsBody;}

    public void moveUp (float gauge) {
        if (gauge <= LIMITEGAUGE) {physicsBody.applyForce(new Vector2f(0.0f, 100.0f * MALUSSLOWDOWN));}
        else this.moveUp();
    }
    public void moveUp(){
        System.out.println("move UP !");
        physicsBody.applyForce(new Vector2f(0.0f, 100.0f));}

    public void moveDown(float gauge){
            if (gauge<=LIMITEGAUGE){physicsBody.applyForce(new Vector2f(0.0f, -100.0f*MALUSSLOWDOWN));}
            else this.moveDown();
    }
    public void moveDown(){physicsBody.applyForce(new Vector2f(0.0f, -100.0f));}

    public void moveLeft (float gauge){
        if (gauge<=LIMITEGAUGE){physicsBody.applyForce(new Vector2f(-100.0f*MALUSSLOWDOWN, 0.0f));}
        else this.moveLeft();
    }
    public void moveLeft(){physicsBody.applyForce(new Vector2f(-100.0f, 0.0f));}

    public void moveRight (float gauge){
        if (gauge<=LIMITEGAUGE){physicsBody.applyForce(new Vector2f(100.0f*MALUSSLOWDOWN, 0.0f));}
        else this.moveRight();
    }
    public void moveRight (){physicsBody.applyForce(new Vector2f(100.0f, 0.0f));}

    public void jump (){
        physicsBody.applyForce(new Vector2f(0.0f, 100.0f));
    }


}

