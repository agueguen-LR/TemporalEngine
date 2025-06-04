package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends Entity{


    public Enemy(Vector4f color, Vector2f player_death_position, Vector2f[] coords, Scene scene) {
        super("dracula", new Vector2f(coords[0].x, coords[0].y), new Vector2f(1,1),
                new float[]{ 0.1f, 10, 0.1f, 0}, color , "bat");
        if(coords.length == 1){
            throw new IllegalArgumentException("Not enough coordinate points");
        }
        Collider2D collider=new Collider2D((new AABB(transform)));
        collider.setOnIntersects((context, other) -> {
                if (other instanceof GameObject objet) {
                    if (objet.getName() .equals("player")){
                    objet.getComponent(Transform.class).setPosition(new Vector2f(player_death_position));
                    }
                }
        });
        p.addComponent(collider);
        getRender().setTileName("fly");


        for (int i=0;i<coords.length;i++) {
            int ii = i;
            int nextIndex;
            if(i+1 >= coords.length){nextIndex=0;} else {
                nextIndex = i + 1;
            }
            GameObject gameCollider=new GameObject("collider");
            Transform transform1=new Transform(coords[i]);
            Render render1=new ColorRender(new Vector4f(0.25f,0.5f,0.25f, 0.5f));
            Collider2D collider2=new Collider2D((new AABB(transform1)));
            collider2.setOnIntersects((context, other) -> {
                {
                    if (other instanceof GameObject objet ) {
                        if (objet.getName() =="dracula") {
                            float XDistance = coords[nextIndex].x - coords[ii].x;
                            float YDistance = coords[nextIndex].y - coords[ii].y;
                            Vector2f vitesse = this.physicsBody.getVelocity();
                            this.physicsBody.applyForce(-vitesse.x, -vitesse.y);
                            this.physicsBody.applyForce(XDistance * 2, YDistance * 2);
                        }
                    }/*
                    float XDistance = coords[nextIndex].x - coords[ii].x;
                    float YDistance = coords[nextIndex].y - coords[ii].y;
                    Vector2f vitesse= this.physicsBody.getVelocity();
                    this.physicsBody.applyForce(-vitesse.x,-vitesse.y);
                    this.physicsBody.applyForce(XDistance*1, YDistance*1);*/
                }
            });
            gameCollider.addComponent(collider2);
            gameCollider.addComponent(transform1);
            gameCollider.addComponent(render1);
            scene.addGameObject(gameCollider);

            float XDistance = coords[1].x - coords[0].x;
            float YDistance = coords[1].y - coords[0].y;
            //this.physicsBody.applyForce(XDistance,YDistance);

            }
    }



    /*public Vector2f[] getLoopPoints() {return this.loopPoints;}
    public void setLoopPoints(Vector2f[] loopPoints) {this.loopPoints = loopPoints;}
*/
    /*public void finalMovement(){
        if ((int)getTransform().getPosition().x == (int)this.loopPoints[direction].x
                && (int)getTransform().getPosition().y == (int)this.loopPoints[direction].y) {
            direction++;
            if (loopPoints.length <= direction) direction = 0;
            }

        if (haveToGoUp()) this.moveUp();
        else this.moveDown();
        if (haveToGoRight()) this.moveRight();
        else this.moveLeft();

    }

    private boolean haveToGoUp() {
        return getTransform().getPosition().y < this.loopPoints[direction].y;
    }
    private boolean haveToGoRight() {
        return getTransform().getPosition().x < this.loopPoints[direction].x;
    }
*/
}

