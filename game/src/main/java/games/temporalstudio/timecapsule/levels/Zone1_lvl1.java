package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;

import java.util.Set;

public class Zone1_lvl1 implements TimeLevel{
	private Scene pastScene;
	private Scene futurScene;
	private Set<TimeObject> pastTimeObjects;
	private Set<TimeObject> futurTimeObjects;

	public Zone1_lvl1(
			GameObject pastCamera, GameObject futurCamera,
			Game game, Player pastPlayer, Player futurPlayer, Medusa pastMedusa, Medusa futurMedusa,
			CapsuleReceiver zone1_pastCapsuleReceiver
	) {
		this.pastScene = new Scene("Zone1_lvl1_Past");
		this.futurScene = new Scene("Zone1_lvl1_Futur");
		this.futurScene.addGameObject(futurCamera);
		this.pastScene.addGameObject(pastCamera);

		ThrowableBottle throwableBottle = new ThrowableBottle("ThrowableBottle", pastPlayer);

		pastTimeObjects = Set.of(
			new Wall(new Vector2f(1, 5), new Vector2f(3, 8)),
			new Exit(
				"Zone1_lvl1_CapsuleExit", 4.0f, 1.0f, pastPlayer,pastMedusa ,
				"zone1_lvl3_past", game::changeLeftScene, new Vector2f(7.0f, 29.0f)
			),
			new Exit(
				"Zone1_lvl1_Exit", 3.0f, 3.0f, pastPlayer, pastMedusa,
				"Zone1_lvl2_Past", game::changeLeftScene, new Vector2f(1.0f, 1.0f)
			),
			new Pickupable("Bottle", 5.0f, 5.0f, pastPlayer, throwableBottle),
			throwableBottle,
			new Medusa("pastMedusa",
				new Vector2f(0.5f, 0.5f),
				pastPlayer
			),
			pastPlayer,
			new SeedEmplacement("Zone1_lvl1_seedEmplacement",0.0f,0.0f,pastPlayer, pastScene,futurScene)

		);

		pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));

		CapsuleSender sender = new CapsuleSender(
			"capsuleSender", futurPlayer,
			new Pickupable("seedPickup", 3, 3, pastPlayer, new Seed("seed", pastPlayer)),
			zone1_pastCapsuleReceiver
		);

		futurPlayer.addToInventory(sender);

		futurTimeObjects = Set.of(
			new Wall(new Vector2f(1, 5), new Vector2f(5, 8)),
			new Exit(
				"Zone1_lvl1_Exit", 3.0f, 4.0f, futurPlayer,futurMedusa ,
				"zone1_lvl3_futur", game::changeRightScene, new Vector2f(1.0f, 1.0f)
			),
			new Medusa("pastMedusa",
				new Vector2f(0.5f, 0.5f),
				futurPlayer
			),
			futurPlayer,
			sender,
			new Wall(new Vector2f(6, 1), new Vector2f(7, 4))

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
