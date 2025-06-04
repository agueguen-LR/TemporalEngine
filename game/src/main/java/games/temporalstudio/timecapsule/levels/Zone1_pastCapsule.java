package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Set;

public class Zone1_pastCapsule implements SingleLevel{
	private Scene scene;
	private Set<TimeObject> timeObjects;

	public Zone1_pastCapsule(GameObject pastCamera, Game game, Player pastPlayer, CapsuleReceiver zone1_pastCapsuleReceiver) {
		this.scene = new Scene("Zone1_pastCapsule");
		this.scene.addGameObject(pastCamera);


		Chest coffre = new Chest("coffre", 1.0f, 2.0f, pastPlayer);
		CompleteKey key = new CompleteKey("Key", 100.0f, 100.0f, pastPlayer.getGameObject(), pastPlayer, coffre);

		pastPlayer.setKey(key);

		timeObjects = Set.of(
				new Wall(new Vector2f(1, 5), new Vector2f(3, 8)),
				new Exit(
						"Zone1_pastCapsule_Exit", 1.0f, 1.0f, pastPlayer,
						"Zone1_lvl1_Past", game::changeLeftScene
				),
				coffre,
				new KeyFragment("key2a", 3.0f, 4.0f, pastPlayer.getGameObject(), pastPlayer, coffre),
				new KeyFragment("Fragment_cle1",6.0f,3.0f,pastPlayer.getGameObject(), pastPlayer, coffre),
				pastPlayer,
				key,
				zone1_pastCapsuleReceiver
		);

		timeObjects.forEach((timeObject) -> this.scene.addGameObject(timeObject.getGameObject()));
	}

	public Scene getScene(){
		return scene;
	}
}
