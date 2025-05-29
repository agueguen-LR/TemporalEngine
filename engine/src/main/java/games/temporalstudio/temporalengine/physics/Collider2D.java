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

public class Collider2D implements Component {
	private Shape shape;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onCollide;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onIntersects;
	private Set<LifeCycleContext> intersecting;
	private Set<LifeCycleContext> colliding;
	private boolean isRigid;

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
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setOnCollide(BiConsumer<LifeCycleContext, LifeCycleContext> onCollide) {
		this.onCollide = onCollide;
	}

	public void setOnIntersects(BiConsumer<LifeCycleContext, LifeCycleContext> onIntersects) {
		this.onIntersects =  onIntersects;
	}

	public void setOffset(float x, float y) {
		this.shape.setOffset(x, y);
	}

	public void setMagnitude(float x, float y) {
		this.shape.setMagnitude(x, y);
	}

	public void addIntersecting(LifeCycleContext context) {
		intersecting.add(context);
	}

	public void removeIntersecting(LifeCycleContext context) {
		intersecting.remove(context);
	}

	public void addColliding(LifeCycleContext context) {
		colliding.add(context);
	}

	public void removeColliding(LifeCycleContext context) {
		colliding.remove(context);
	}

	public void setRigid(boolean isRigid) {
		this.isRigid = isRigid;
	}

	public BiConsumer<LifeCycleContext, LifeCycleContext> getOnIntersects() {
		return onIntersects;
	}

	BiConsumer<LifeCycleContext, LifeCycleContext> getOnCollide() {
		return onCollide;
	}

	public boolean isRigid() {
		return isRigid;
	}

	void updateCollider2D() {
		if (this.shape == null) {
			Game.LOGGER.severe("updateCollider2D called on null shape.");
			return;
		}
		this.shape.updateShape();
	}

	/**
	 * Checks if this collision box intersects with another collision box.
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
	 * Checks if this collision box intersects with another collision box after casting it by a translation vector.
	 * @param other The other Collision2D object to check for collision against.
	 * @param castTranslation The translation vector to cast this collider by before checking for collision.
	 * @return true if the two collision boxes overlap after casting, false otherwise.
	 */
	public boolean collidesWith(Collider2D other, Vector2f castTranslation) {
		if (other == null) {
			Game.LOGGER.severe("Collider2D collidesWith called with null argument.");
			return false;
		}
		if (this.shape == null || other.shape == null) {
			Game.LOGGER.severe("Collider2D collidesWith called with null shape.");
			return false;
		}
		return this.shape.cast(castTranslation).intersects(other.shape);
	}

	public Vector2f computeRigidCollisionNewVelocity(Collider2D other, Vector2f incomingVelocity) {
		if (other == null || this.shape == null || other.shape == null) {
			Game.LOGGER.severe("Collider2D computeRigidCollisionNewVelocity called with null argument.");
			return new Vector2f(0, 0);
		}
		return this.shape.computeRigidCollisionNewVelocity(other.shape, incomingVelocity);
	}

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
