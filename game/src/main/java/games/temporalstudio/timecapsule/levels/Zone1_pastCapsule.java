package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.objects.*;

import java.util.Set;

public class Zone1_pastCapsule implements SingleLevel{
	private Scene scene;
	private Set<TimeObject> timeObjects;

	public Zone1_pastCapsule(GameObject pastCamera, Game game, Player pastPlayer, CapsuleReceiver zone1_pastCapsuleReceiver) {
		this.scene = new Scene("Zone1_pastCapsule");
		this.scene.addGameObject(pastCamera);

		Chest coffre;
		timeObjects = Set.of(
				new Wall("Zone1_pastCapsule_Wall1", 1f, 5.0f),
				new Wall("Zone1_pastCapsule_Wall2", 2f, 5.0f),
				new Wall("Zone1_pastCapsule_Wall3", 3f, 5.0f),
				new Wall("Zone1_pastCapsule_Wall4", 4f, 5.0f),
				new Wall("Zone1_pastCapsule_Wall5", 5f, 5.0f),
				new Exit(
						"Zone1_pastCapsule_Exit", 1.0f, 1.0f, pastPlayer,
						"Zone1_lvl1_Past", game::changeLeftScene
				),
				coffre = new Chest("coffre", 1.0f, 2.0f, pastPlayer),
				new KeyFragment("key2a", 3.0f, 4.0f, pastPlayer.getGameObject(), pastPlayer, coffre),
				new KeyFragment("Fragment_cle1",6.0f,3.0f,pastPlayer.getGameObject(), pastPlayer, coffre),
				pastPlayer,
				zone1_pastCapsuleReceiver
		);

		timeObjects.forEach((timeObject) -> this.scene.addGameObject(timeObject.getGameObject()));
	}

	public Scene getScene(){
		return scene;
	}
}
