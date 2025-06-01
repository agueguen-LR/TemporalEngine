package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends Entity {

    private Vector2f[] loopPoints;
    private int direction;

    public Enemy(String name, Vector2f scale, Vector2f position, Vector4f color, Vector2f[] coords) {
        super(name, scale, position, color);

        collider.setOnIntersects((context, other) -> {
            if (context != other) System.out.println("rlrihgzmfih");
                /*if (context instanceof Enemy enemy
                        && other instanceof Player player
                ) {
                    Game.LOGGER.info("Player killed by dracula!");
                    player.transform.setPosition(new Vector2f());
                }*/
        });
        this.addComponent(collider);
        this.removeComponent(physicsBody);


        if(coords.length <= 1){
            throw new IllegalArgumentException("Not enough coordinate points");
        }
        else {
            direction=0;
            loopPoints=coords;

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

