package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.rendering.component.MapRender;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;

import java.util.Set;

public class Zone1_lvl2 implements TimeLevel{

	private Scene pastScene;
	private Set<TimeObject> pastTimeObjects;

	public Zone1_lvl2(Game game, GameObject pastCamera,
		Player pastPlayer, Medusa pastMedusa,
		CapsuleReceiver zone1_pastCapsuleReceiver
	){
		this.pastScene = new Scene("zone1_lvl2_past");

		// Game Objects
		GameObject map = new GameObject("map_zone1_lvl2_past");
		map.addComponent(new MapRender(
			"past", "zone1_lvl2"
		));

		this.pastTimeObjects = Set.of(
			new Wall(new Vector2f(0, 3), new Vector2f(32, 3)), // Bottom wall
			new Wall(new Vector2f(9.3f, 0), new Vector2f(9.3f, 32)), // Left wall
			new Wall(new Vector2f(20.7f, 0), new Vector2f(20.7f, 32)), // Right wall
			new Wall(new Vector2f(0, 23), new Vector2f(32, 23)), // Top wall

			new Exit(
				"exit_zone1_lvl1", 4.0f, 1.0f,
				pastPlayer, pastMedusa,
				"zone1_lvl1_past",
				game::changeLeftScene,
				new Vector2f(7.0f, 28.0f)
			),

			new Medusa("pastMedusa",
				new Vector2f(0.5f, 0.5f),
				pastPlayer
			),
			pastPlayer,
			zone1_pastCapsuleReceiver
		);

		// Adds game objects
		this.pastScene.addGameObject(pastCamera);
		this.pastScene.addGameObject(map);
		this.pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));
	}

	@Override
	public Scene getPastScene() {
		return pastScene;
	}

	@Override
	public Scene getFuturScene(){ return null; }
}