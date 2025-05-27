package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

public class Transform implements Component {
	private Vector2f position;
	private Vector2f scale;

	public Transform(Vector2f scale, Vector2f position) {
		this.scale = scale;
		this.position = position;
	}

	public void setPosition(Vector2f position) {
		Game.LOGGER.info("New position: " + position);
		this.position = position;
	}

	public Vector2f getPosition() {
		return position;
	}

	@Override
	public void update(LifeCycleContext context, float delta) {

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
