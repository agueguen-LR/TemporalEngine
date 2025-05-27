package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player implements Component{

private enum Controls {
    UP(GLFW_KEY_W),
    DOWN(GLFW_KEY_S),
    LEFT(GLFW_KEY_A),
    RIGHT(GLFW_KEY_D);

    private final int key;

    Controls(int key) {
        this.key = key;
    }
}

	@Override
	public void update(LifeCycleContext context, float delta) {
		if (!(context instanceof GameObject object)) {
			System.err.println("Player component can only be used with GameObject context.");
			return;
		}
		PhysicsBody body = object.getComponent(PhysicsBody.class);
		if (body == null) {
			Game.LOGGER.severe("Player component requires a PhysicsBody component to function.");
			return;
		}
		if (KeyListener.isKeyPressed(Controls.UP.key)) {
			body.applyForce(new Vector2f(0.0f, 1.0f));
		}
		if (KeyListener.isKeyPressed(Controls.DOWN.key)) {
			body.applyForce(new Vector2f(0.0f, -1.0f));
		}
		if (KeyListener.isKeyPressed(Controls.LEFT.key)) {
			body.applyForce(new Vector2f(-1.0f, 0.0f));
		}
		if (KeyListener.isKeyPressed(Controls.RIGHT.key)) {
			body.applyForce(new Vector2f(1.0f, 0.0f));
		}
	}

	@Override
	public void init(LifeCycleContext context) {
	}

	@Override
	public void start(LifeCycleContext context) {

	}

	@Override
	public void destroy(LifeCycleContext context) {

	}
}
