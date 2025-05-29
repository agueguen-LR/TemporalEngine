package games.temporalstudio.timecapsule.Entity;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends PNJ {

    private Vector2f[] loopPoints;
    private int direction;

    public Enemy(String name, Vector2f scale, Vector2f position, Vector4f color, Vector2f[] coords) {
        super(name, scale, position, color);
        if(coords.length < 2){
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
        if (this.transform.getPosition().x >= this.loopPoints[direction].x) {
            direction++;
            if (loopPoints.length <= direction) direction = 0;
            }
        else {
            if (haveToGoUp()) this.moveUp();
            else this.moveDown();
            if (haveToGoRight()) this.moveRight();
            else this.moveLeft();
        }
    }

    private boolean haveToGoUp() {
        return this.transform.getPosition().y > this.loopPoints[direction].y;
    }
    private boolean haveToGoRight() {
        return this.transform.getPosition().x > this.loopPoints[direction].x;
    }

}

