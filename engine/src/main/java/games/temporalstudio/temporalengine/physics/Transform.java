package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

/**
 * Represents the position and scale of a game object in 2D space.
 * This component is used to track and manipulate the spatial properties
 * of game objects, such as their location and size.
 *
 * @author agueguen-LR, Xibitol
 */
public class Transform implements Component {

	/** The default scale for all transforms (1, 1). */
	private static final Vector2f DEFAULT_SCALE = new Vector2f(1, 1);

	/** The position of the object in 2D space. */
	private Vector2f position;

	/** The scale of the object in 2D space. */
	private Vector2f scale;

	/**
	 * Constructs a Transform with the specified position and scale.
	 *
	 * @param position The position vector.
	 * @param scale    The scale vector.
	 */
	public Transform(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
	}

	/**
	 * Constructs a Transform with the specified position and default scale.
	 *
	 * @param position The position vector.
	 */
	public Transform(Vector2f position) {
		this(position, new Vector2f(DEFAULT_SCALE));
	}

	/**
	 * Constructs a Transform with default position (0, 0) and default scale (1, 1).
	 */
	public Transform() {
		this(new Vector2f(), new Vector2f(DEFAULT_SCALE));
	}

	/**
	 * Returns the position of the object.
	 *
	 * @return The position vector.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * Returns the scale of the object.
	 *
	 * @return The scale vector.
	 */
	public Vector2f getScale() {
		return scale;
	}

	/**
	 * Sets the position of the object.
	 *
	 * @param position The new position vector.
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * Sets the scale of the object.
	 *
	 * @param scale The new scale vector.
	 */
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	/**
	 * Moves the object by the specified translation vector.
	 *
	 * @param translation The translation vector to add to the current position.
	 */
	public void move(Vector2f translation) {
		this.position.add(translation);
	}

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){}
	@Override
	public void destroy(LifeCycleContext context){}
}
