package Entity;

import org.joml.Vector2f;

public class Enemy extends PNJ {

    private Vector2f coordRight;
    private Vector2f coordLeft;
    private Vector2f direction;

    public Enemy(String name, Vector2f scale, Vector2f coord1, Vector2f coord2){
        super(name, scale, coord2);
        if (coord2.x > coord1.x){
            this.coordRight = coord2;
            this.coordLeft = coord1;
        }
        else {
            this.coordRight = coord1;
            this.coordLeft = coord2;
        }
        this.direction=coord1;
    }

    public Enemy(String name, Vector2f scale, Vector2f position, Vector2f coord1, Vector2f coord2) {
        super(name, scale, position);
        if (coord2.x > coord1.x){
            this.coordRight = coord2;
            this.coordLeft = coord1;
        }
        else {
            this.coordRight = coord1;
            this.coordLeft = coord2;
        }
        this.direction=coordLeft;
    }

    public Vector2f getCoordLeft() {return coordLeft;}
    public Vector2f getCoordRight() {return coordRight;}
    public void setCoordLeft(Vector2f coordLeft) {this.coordLeft = coordLeft;}
    public void setCoordRight(Vector2f coordRight) {this.coordRight = coordRight;}

    public void finalMovement(){
        if(this.coordLeft==this.direction) {
            if (this.transform.getPosition().x <= this.direction.x && this.transform.getPosition().y <= this.direction.y) {
                direction=this.coordRight;
                moveRight();
            }
            else this.moveLeft();
        }
        else {
            if (this.transform.getPosition().x >= this.direction.x && this.transform.getPosition().y >= this.direction.y) {
                direction=this.coordLeft;
                moveLeft();
            }
            else this.moveRight();
        }
    }


}
