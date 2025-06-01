package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.objects.*;

import java.util.Set;

public class Zone1_lvl1 implements TimeLevel{
	private Scene pastScene;
	private Scene futurScene;
	private Set<TimeObject> pastTimeObjects;
	private Set<TimeObject> futurTimeObjects;

	public Zone1_lvl1(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer) {
		this.pastScene = new Scene("Zone1_lvl1_Past");
		this.futurScene = new Scene("Zone1_lvl1_Futur");
		this.futurScene.addGameObject(futurCamera);
		this.pastScene.addGameObject(pastCamera);

		ThrowableBottle throwableBottle = new ThrowableBottle("ThrowableBottle", pastPlayer);

		pastTimeObjects = Set.of(
				new Wall("Zone1_lvl1_Wall1", 1f, 6.0f),
				new Wall("Zone1_lvl1_Wall2", 2f, 7.0f),
				new Wall("Zone1_lvl1_Wall3", 3f, 8.0f),
				new Wall("Zone1_lvl1_Wall4", 4f, 9.0f),
				new Wall("Zone1_lvl1_Wall5", 5f, 10.0f),
				new Exit(
						"Zone1_lvl1_CapsuleExit", 4.0f, 1.0f, pastPlayer.getGameObject(),
						"Zone1_pastCapsule", game::changeLeftScene
				),
				new Exit(
						"Zone1_lvl1_Exit", 3.0f, 3.0f, pastPlayer.getGameObject(),
						"Zone1_lvl2_Past", game::changeLeftScene
				),
				new Pickupable("Bottle", 5.0f, 5.0f, pastPlayer, throwableBottle),
				throwableBottle,
				pastPlayer
		);

		pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));

		futurTimeObjects = Set.of(
				new Wall("Zone1_lvl1_Wall1", 1f, 6.0f),
				new Wall("Zone1_lvl1_Wall2", 2f, 7.0f),
				new Wall("Zone1_lvl1_Wall3", 2f, 8.0f),
				new Wall("Zone1_lvl1_Wall4", 4f, 9.0f),
				new Wall("Zone1_lvl1_Wall5", 4f, 10.0f),
				new Exit(
						"Zone1_lvl1_Exit", 3.0f, 4.0f, futurPlayer.getGameObject(),
						"Zone1_lvl2_Futur", game::changeRightScene
				),
				futurPlayer
		);


		futurTimeObjects.forEach((timeObject) -> this.futurScene.addGameObject(timeObject.getGameObject()));
	}

	@Override
	public Scene getPastScene() {
		return pastScene;
	}

	@Override
	public Scene getFuturScene() {
		return futurScene;
	}
}
