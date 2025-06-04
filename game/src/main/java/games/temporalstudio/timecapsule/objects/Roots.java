package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Roots {
    private GameObject gameObject;
    private ArrayList<Root> roots;
//boolean north 0 , boolean south 1 , boolean west 2, boolean east 3
    public Roots(String name, float oX, float oY, List<CardinalDirections> directions, Scene futurScene) {
        //Les racines
        roots = new ArrayList<>();

        float iX = oX;
        float iY =  oY;
        for (CardinalDirections direction : directions) {
            switch (direction) {
                case NORTH -> iY += 1;
                case SOUTH -> iY -= 1;
                case EAST -> iX += 1;
                case WEST -> iX -= 1;
            }
            roots.add(new Root(name, iX, iY, futurScene));

        }

    }



}