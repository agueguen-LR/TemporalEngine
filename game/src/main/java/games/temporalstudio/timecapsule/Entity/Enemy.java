package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.Trigger;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.concurrent.atomic.AtomicBoolean;

public class Enemy extends PNJ {

    private Vector2f[] loopPoints;
    private int direction;

    public Enemy(String name, Vector2f scale, Vector2f position, Vector4f color, Vector2f[] coords, Scene scene) {
        super(name, scale, position, color);
        if(coords.length <= 1){
            System.out.println("Not enough coordinates !");
        }
        else {
            direction=0;
            loopPoints=coords;
            /*AtomicBoolean triggerActivated = new AtomicBoolean(false);
            Trigger trigger = new Trigger(1 , triggerActivated::get);*/
            this.removeComponent(collider);
            collider.setOnIntersects((context, other) -> {
                System.out.println("rlrihgzmfih");
                if (context instanceof Enemy enemy
                        && other instanceof Player player
                ) {
                    System.out.println("icciiiii");
                    Game.LOGGER.info("Player killed by dracula!");
                    scene.removeGameObject(player);
                    player.destroy(scene);
                }
            });
            this.addComponent(collider);
        }
    }

    public Vector2f[] getLoopPoints() {return this.loopPoints;}
    public void setLoopPoints(Vector2f[] loopPoints) {this.loopPoints = loopPoints;}

    public void finalMovement(){
        if ((int)this.transform.getPosition().x == (int)this.loopPoints[direction].x
                && (int)this.transform.getPosition().y == (int)this.loopPoints[direction].y) {
            direction++;
            if (loopPoints.length <= direction) direction = 0;
            }

        if (haveToGoUp()) this.moveUp();
        else this.moveDown();
        if (haveToGoRight()) this.moveRight();
        else this.moveLeft();

    }

    private boolean haveToGoUp() {
        return this.transform.getPosition().y < this.loopPoints[direction].y;
    }
    private boolean haveToGoRight() {
        return this.transform.getPosition().x < this.loopPoints[direction].x;
    }


    @Override
    public void update(LifeCycleContext context, float delta){
        finalMovement();
    }
}

