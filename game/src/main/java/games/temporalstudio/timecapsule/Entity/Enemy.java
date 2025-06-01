package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends PNJ {

    private Vector2f[] loopPoints;
    private int direction;

    public Enemy(String name, Vector2f scale, Vector2f position, Vector4f color, Vector2f[] coords) {
        super(name, scale, position, color);
        if(coords.length <= 1){
            System.out.println("Not enough coordinates !");
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

