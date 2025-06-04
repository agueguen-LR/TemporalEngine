package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;

import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Zone1_pastCapsule implements SingleLevel{
	private Scene scene;
	private Set<TimeObject> timeObjects;

	public Zone1_pastCapsule(
			GameObject pastCamera, Game game, Player pastPlayer, CapsuleReceiver zone1_pastCapsuleReceiver, Scene zone1Futur
	) {
		this.scene = new Scene("Zone1_pastCapsule");
		this.scene.addGameObject(pastCamera);

		timeObjects = Set.of(
				new Wall(new Vector2f(1, 5), new Vector2f(3, 8)),
				new Exit(
						"Zone1_pastCapsule_Exit", 1.0f, 1.0f, pastPlayer,
						"Zone1_lvl1_Past", game::changeLeftScene
				),
				pastPlayer,
				zone1_pastCapsuleReceiver,


				new SeedEmplacement("seedEmplacement", 6, 0, pastPlayer, GLFW_KEY_SPACE, this.scene, zone1Futur)
		);

		timeObjects.forEach((timeObject) -> this.scene.addGameObject(timeObject.getGameObject()));
	}

	public Scene getScene(){
		return scene;
	}
}
