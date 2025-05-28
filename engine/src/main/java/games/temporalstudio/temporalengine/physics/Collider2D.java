package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public class Collider2D implements Component {
	private AABB aabb;
	private BiConsumer<LifeCycleContext, LifeCycleContext> physicsOnCollide;
	private BiConsumer<LifeCycleContext, LifeCycleContext> physicsOnSeparate;
	private BiConsumer<LifeCycleContext, LifeCycleContext> gameOnCollide;
	private BiConsumer<LifeCycleContext, LifeCycleContext> gameOnSeparate;
	private Map<LifeCycleContext, Boolean> colliding;
	private boolean enabled;

	public Collider2D(Transform transform) {
		if (transform == null) {
			Game.LOGGER.severe("Collider2D cannot be created with null Transform.");
			return;
		}
		this.aabb = new AABB(transform);
		this.colliding = new HashMap<>();
		this.physicsOnCollide = (object, other) -> {};
		this.physicsOnSeparate = (object, other) -> {};
		this.gameOnCollide = (object, other) -> {};
		this.gameOnSeparate = (object, other) -> {};
		this.enabled = true;
	}

	public void setPhysicsOnCollide(BiConsumer<LifeCycleContext, LifeCycleContext> physicsOnCollide) {
		this.physicsOnCollide = physicsOnCollide;
	}

	public void setPhysicsOnSeparate(BiConsumer<LifeCycleContext, LifeCycleContext>physicsOnSeparate) {
		this.physicsOnSeparate = physicsOnSeparate;
	}

	public void setGameOnCollide(BiConsumer<LifeCycleContext, LifeCycleContext>gameOnCollide) {
		this.gameOnCollide = gameOnCollide;
	}

	public void setGameOnSeparate(BiConsumer<LifeCycleContext, LifeCycleContext>gameOnSeparate) {
		this.gameOnSeparate = gameOnSeparate;
	}

	BiConsumer<LifeCycleContext, LifeCycleContext>getPhysicsOnCollide() {
		return physicsOnCollide;
	}

	BiConsumer<LifeCycleContext, LifeCycleContext>getPhysicsOnSeparate() {
		return physicsOnSeparate;
	}

	public void setOffset(float x, float y) {
		this.aabb.setOffset(x, y);
	}

	public void setMagnitude(float x, float y) {
		this.aabb.setMagnitude(x, y);
	}

	public Vector2f getMin() {
		return this.aabb.getMin();
	}

	public Vector2f getMax() {
		return this.aabb.getMax();
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

	void updateAABB() {
		if (this.aabb == null) {
			Game.LOGGER.severe("Collider2D updateAABB called on null AABB.");
			return;
		}
		this.aabb.updateMin();
		this.aabb.updateMax();
	}

	/**
	 * Checks if this collision box collides with another collision box.
	 * @param other The other Collision2D object to check for collision against.
	 * @return true if the two collision boxes overlap, false otherwise.
	 */
	public boolean collidesWith(Collider2D other) {
		if (other == null) {
			Game.LOGGER.severe("Collision2D collidesWith called with null argument.");
			return false;
		}
		// if a separating axis exists, then the two AABBs do not collide
		if (this.aabb.getMax().x < other.getMin().x || this.aabb.getMin().x > other.getMax().x)return false;
		if (this.aabb.getMax().y < other.getMin().y || this.aabb.getMin().y > other.getMax().y)return false;
		return true;
	}

	public void setColliding(LifeCycleContext context, boolean isColliding) {
		if (context == null) {
			Game.LOGGER.severe("Collider2D setColliding called with null context.");
			return;
		}
		colliding.put(context, isColliding);
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
		colliding.forEach((otherContext, isColliding) -> {
			if (isColliding) {
				gameOnCollide.accept(context, otherContext);
			} else {
				gameOnSeparate.accept(context, otherContext);
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
