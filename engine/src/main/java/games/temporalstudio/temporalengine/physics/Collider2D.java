package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.shapes.Shape;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public class Collider2D implements Component {
	private Shape shape;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onCollide;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onIntersects;
	private BiConsumer<LifeCycleContext, LifeCycleContext> onSeparates;
	private Map<LifeCycleContext, Boolean> intersecting;
	private boolean enabled;

	public Collider2D(Transform transform) {
		if (transform == null) {
			Game.LOGGER.severe("Collider2D cannot be created with null Transform.");
			return;
		}
		this.intersecting = new HashMap<>();
		this.onCollide = (object, other) -> {};
		this.onIntersects = (object, other) -> {};
		this.onSeparates = (object, other) -> {};
		this.enabled = true;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setOnCollides(BiConsumer<LifeCycleContext, LifeCycleContext> physicsOnCollide) {
		this.onCollide = physicsOnCollide;
	}

	public void setOnIntersects(BiConsumer<LifeCycleContext, LifeCycleContext>gameOnCollide) {
		this.onIntersects = gameOnCollide;
	}

	public void setOnSeparates(BiConsumer<LifeCycleContext, LifeCycleContext> onSeparates) {
		this.onSeparates = onSeparates;
	}

	public BiConsumer<LifeCycleContext, LifeCycleContext> getOnIntersects() {
		return onIntersects;
	}

	public BiConsumer<LifeCycleContext, LifeCycleContext> getOnSeparates() {
		return onSeparates;
	}

	BiConsumer<LifeCycleContext, LifeCycleContext> getOnCollide() {
		return onCollide;
	}

	public void setOffset(float x, float y) {
		this.shape.setOffset(x, y);
	}

	public void setMagnitude(float x, float y) {
		this.shape.setMagnitude(x, y);
	}

	public void enable(){
		this.enabled = true;
	}

	public void disable(){
		this.enabled = false;
	}

	public boolean isEnabled() {
		return enabled;
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

	public void setIntersecting(LifeCycleContext context, boolean isIntersecting) {
		if (context == null) {
			Game.LOGGER.severe("Collider2D setIntersecting called with null context.");
			return;
		}
		intersecting.put(context, isIntersecting);
	}

	@Override
	public void update(LifeCycleContext context, float delta) {
		if (!enabled) {
			return; // Skip update if collider is disabled
		}
		if (!(context instanceof GameObject gameObject)) {
			Game.LOGGER.severe("Collider2D can only be used with GameObject context.");
			return;
		}
		Transform transform = gameObject.getComponent(Transform.class);
		if (transform == null) {
			Game.LOGGER.severe("Collider2D requires a Transform component.");
			return;
		}
		intersecting.forEach((otherContext, isColliding) -> {
			if (isColliding) {
				onIntersects.accept(context, otherContext);
			} else {
				onSeparates.accept(context, otherContext);
			}
		});
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
