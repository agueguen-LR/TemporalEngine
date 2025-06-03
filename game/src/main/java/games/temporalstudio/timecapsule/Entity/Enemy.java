package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends Entity{


    private Collider2D[] loopPoints;
    private int direction;

    public Enemy(String name, Vector4f color, Vector2f[] coords, Scene scene) {
        super(name, coords[0], new Vector2f(1,1),new float[]{ 0, 10, 0.1f, 20f}, color );
        if(coords.length <= 1){
            throw new IllegalArgumentException("Not enough coordinate points");
        }
        Collider2D collider=new Collider2D((new AABB(transform)));
        /*collider.setOnIntersects((context, other) -> {
                if (other instanceof Player player
                ) {
                    System.out.println("ludfhemruih");
                    Game.LOGGER.info("Player killed by dracula!");
                    player.transform.setPosition(new Vector2f());
                }
        });*/
        p.addComponent(collider);


        direction=0;
        for (int i=0;i<coords.length;i++) {
            int ii = i;
            int nextIndex;
            if(i+1 >= coords.length){nextIndex=0;} else {
                nextIndex = i + 1;
            }
            GameObject gameCollider=new GameObject("collider");
            Transform transform1=new Transform(coords[i]);
            Collider2D collider2=new Collider2D((new AABB(transform1)));
            collider2.setOnIntersects((context, other) -> {
                {
                    Game.LOGGER.info("Enemy change direction");
                    float XDistance=coords[nextIndex].x- coords[ii].x;
                    float YDistance=coords[nextIndex].y- coords[ii].y;
                    getPhysicsBody().applyForce(XDistance, YDistance);
                }
            });
            gameCollider.addComponent(collider2);
            scene.addGameObject(gameCollider);

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

