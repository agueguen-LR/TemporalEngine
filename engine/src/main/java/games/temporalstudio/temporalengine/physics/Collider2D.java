package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.shapes.Shape;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Represents a 2D collider component for game objects.
 * Used for collision detection and response in the physics engine.
 * Supports setting shape, restitution, rigidness, and collision callbacks.
 *
 * @author agueguen-LR
 */
public class Collider2D implements Component {
	private Shape shape;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onCollide;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onIntersects;
	private Set<LifeCycleContext> intersecting;
	private Set<LifeCycleContext> colliding;
	private boolean isRigid;
	/**
	 * The coefficient of restitution for this collider, which determines how bouncy it is.
	 * Only relevant if isRigid is true, or if the gameObject also has a PhysicsBody component.
	 */
	private float restitution;

	/**
	 * Constructs a Collider2D with the given transform.
	 *
	 * @param transform the transform associated with this collider
	 */
	public Collider2D(Transform transform) {
		if (transform == null) {
			Game.LOGGER.severe("Collider2D cannot be created with null Transform.");
			return;
		}
		this.intersecting = new HashSet<>();
		this.colliding = new HashSet<>();
		this.onCollide = (object, other) -> {};
		this.onIntersects = (object, other) -> {};
		this.isRigid = false;
		this.restitution = 0.0f; // Default to no bounce
	}

	/**
	 * Copy constructor for Collider2D.
	 *
	 * @param toCopy the Collider2D instance to copy
	 */
	public Collider2D(Collider2D toCopy) {
		if (toCopy == null) {
			Game.LOGGER.severe("Collider2D cannot be created from null Collider2D.");
			return;
		}
		this.shape = toCopy.shape.copy();
		this.onCollide = (object, other) -> {};
		this.onIntersects = (object, other) -> {};
		this.intersecting = new HashSet<>(toCopy.intersecting);
		this.colliding = new HashSet<>(toCopy.colliding);
		this.isRigid = toCopy.isRigid;
		this.restitution = toCopy.restitution;
	}

	/**
	 * Sets the shape used for collision detection.
	 *
	 * @param shape the shape to use
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * Sets the callback to be invoked when a collision occurs.
	 *
	 * @param onCollide the collision callback (context, other)
	 */
	public void setOnCollide(BiConsumer<LifeCycleContext, LifeCycleContext> onCollide) {
		this.onCollide = onCollide;
	}

	/**
	 * Sets the callback to be invoked when an intersection occurs (without full collision response).
	 *
	 * @param onIntersects the intersection callback (context, other)
	 */
	public void setOnIntersects(BiConsumer<LifeCycleContext, LifeCycleContext> onIntersects) {
		this.onIntersects =  onIntersects;
	}

	/**
	 * Sets the offset of the collider's shape.
	 *
	 * @param offset the new offset as a Vector2f
	 */
	public void setOffset(Vector2f offset) {
		this.shape.setOffset(offset);
	}

	/**
	 * Sets the magnitude (size) of the collider's shape.
	 *
	 * @param magnitude the new magnitude as a Vector2f
	 */
	public void setMagnitude(Vector2f magnitude) {
		this.shape.setMagnitude(magnitude);
	}

	/**
	 * Sets whether this collider is rigid (immovable).
	 *
	 * @param isRigid true if rigid, false otherwise
	 */
	public void setRigid(boolean isRigid) {
		this.isRigid = isRigid;
	}

	/**
	 * Sets the restitution (bounciness) of the collider2D.
	 * Only relevant if isRigid is true, or if the gameObject also has a PhysicsBody component.
	 *
	 * @param restitution the restitution value to set
	 */
	public void setRestitution(float restitution) {
		if (restitution < 0.0f){
			Game.LOGGER.warning("Negative restitution is likely to cause buggy behavior ! Continue at your own risk.");
		}
		this.restitution = restitution;
	}

	/**
	 * Adds a context to the set of currently intersecting objects.
	 *
	 * @param context the context to add
	 */
	public void addIntersecting(LifeCycleContext context) {
		intersecting.add(context);
	}

	/**
	 * Removes a context from the set of currently intersecting objects.
	 *
	 * @param context the context to remove
	 */
	public void removeIntersecting(LifeCycleContext context) {
		intersecting.remove(context);
	}

	/**
	 * Adds a context to the set of currently colliding objects.
	 *
	 * @param context the context to add
	 */
	public void addColliding(LifeCycleContext context) {
		colliding.add(context);
	}

	/**
	 * Removes a context from the set of currently colliding objects.
	 *
	 * @param context the context to remove
	 */
	public void removeColliding(LifeCycleContext context) {
		colliding.remove(context);
	}

	/**
	 * Returns the area of the collider's shape.
	 *
	 * @return the area as a float
	 */
	public float getArea() {
		return this.shape.getArea();
	}

	/**
	 * Returns the restitution (bounciness) of this collider.
	 *
	 * @return the restitution coefficient
	 */
	public float getRestitution() {
		return restitution;
	}

	/**
	 * Returns the intersection callback.
	 *
	 * @return the intersection callback
	 */
	public BiConsumer<LifeCycleContext, LifeCycleContext> getOnIntersects() {
		return onIntersects;
	}

	/**
	 * Returns the collision callback.
	 *
	 * @return the collision callback
	 */
	public BiConsumer<LifeCycleContext, LifeCycleContext> getOnCollide() {
		return onCollide;
	}

	/**
	 * Returns the collision normal between this collider and another collider.
	 *
	 * @param other the other Collider2D to calculate the collision normal with
	 * @return the collision normal as a Vector2f
	 */
	public Vector2f getCollisionNormal(Collider2D other) {
		if (other == null || this.shape == null || other.shape == null) {
			Game.LOGGER.severe("Collider2D getCollisionNormal called with null argument.");
			return new Vector2f(0, 0);
		}
		return this.shape.collisionNormal(other.shape);
	}

	/**
	 * Returns whether this collider is rigid.
	 *
	 * @return true if rigid, false otherwise
	 */
	public boolean isRigid() {
		return isRigid;
	}

	/**
	 * Updates the collider's shape.
	 */
	void updateCollider2D() {
		if (this.shape == null) {
			Game.LOGGER.severe("updateCollider2D called on null shape.");
			return;
		}
		this.shape.updateShape();
	}

	/**
	 * Checks if this collision box intersects with another collision box.
	 *
	 * @param other The other Collision2D object to check for collision against.
	 * @return true if the two collision boxes overlap, false otherwise.
	 */
	public boolean intersectsWith(Collider2D other) {
		if (other == null) {
			Game.LOGGER.severe("Collision2D collidesWith called with null argument.");
			return false;
		}
		return this.shape.intersects(other.shape);
	}

	/**
	 * Updates the collider's state, invoking callbacks for intersections and collisions.
	 *
	 * @param context the current LifeCycleContext
	 * @param delta   the time delta since the last update
	 */
	@Override
	public void update(LifeCycleContext context, float delta) {
		if (!(context instanceof GameObject gameObject)) {
			Game.LOGGER.severe("Collider2D can only be used with GameObject context.");
			return;
		}
		Transform transform = gameObject.getComponent(Transform.class);
		if (transform == null) {
			Game.LOGGER.severe("Collider2D requires a Transform component.");
			return;
		}
		intersecting.forEach(otherContext -> onIntersects.accept(context, otherContext));
		intersecting.clear();
		colliding.forEach(otherContext -> onCollide.accept(context, otherContext));
		colliding.clear();
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
