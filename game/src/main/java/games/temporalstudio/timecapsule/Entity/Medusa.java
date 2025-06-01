package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Medusa extends Entity{

    private Player player;
    private static double MAX_DISTANCE=2;

    public Medusa(String name, Vector2f scale, Vector2f position, Vector4f color, Player player) {
        super(name, scale, position, color);
        this.player = player;
    }

    @Override
    public void update(LifeCycleContext context, float delta){
        if (distance(player) >= MAX_DISTANCE){
            XmoveCloser(player, MAX_DISTANCE);
            YmoveCloser(player, MAX_DISTANCE);
        }
    }

}
