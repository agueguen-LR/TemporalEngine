package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Consumer;

public class Exit implements TimeObject {
	private final GameObject gameObject;

	public Exit(String name, float x, float y, Player player, Medusa medusa, String targetSceneName, Consumer<String> changeSceneFunction, Vector2f newPosition) {
		this.gameObject = new GameObject(name);
		Render render = new ColorRender(new Vector4f(1, 0, 0, 1));
		Transform transform = new Transform(new Vector2f(x, y));
		Collider2D collider = new Collider2D(new AABB(transform));
		collider.setOnIntersects((thisExit, other) -> {
			if (other == player.getGameObject()) {
				changeSceneFunction.accept(targetSceneName);
				player.getTransform().setPosition(new Vector2f(newPosition));
				medusa.getTransform().setPosition(newPosition.sub(1,1, new Vector2f()));
			}
		});

		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(collider);
		this.gameObject.addComponent(render);
	}

	public GameObject getGameObject() {
		return gameObject;
	}
}
