package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.Entity.Enemy;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

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
				new Enemy(new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f(),
						new Vector2f[]{new Vector2f(-2,-4), new Vector2f(2,-2),new Vector2f(0,-5)},
						pastScene),
				new Wall(new Vector2f(1, 5), new Vector2f(3, 8)),
				new Exit(
						"Zone1_lvl1_CapsuleExit", 4.0f, 1.0f, pastPlayer,pastMedusa ,
						"Zone1_pastCapsule", game::changeLeftScene, new Vector2f(7.0f, 28.0f)
				),
				new Exit(
						"Zone1_lvl1_Exit", 3.0f, 3.0f, pastPlayer, pastMedusa,
						"Zone1_lvl2_Past", game::changeLeftScene, new Vector2f(1.0f, 1.0f)
				),
				new Pickupable("Bottle", 5.0f, 5.0f, pastPlayer, throwableBottle),
				throwableBottle,
				new Medusa("pastMedusa",
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), pastPlayer),
				pastPlayer
		);

		pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));

		CapsuleSender sender = new CapsuleSender(
				"capsuleSender", futurPlayer,
				new Pickupable("seedPickup", 3, 3, pastPlayer, new Seed("seed", pastPlayer)),
				zone1_pastCapsuleReceiver
		);
		futurPlayer.addToInventory(sender);

		futurTimeObjects = Set.of(
				new Enemy(new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f(),
						new Vector2f[]{new Vector2f(-2,-4), new Vector2f(2,-2),new Vector2f(0,-5)},
						futurScene),
				new Wall(new Vector2f(1, 5), new Vector2f(5, 8)),
				new Exit(
						"Zone1_lvl1_Exit", 3.0f, 4.0f, futurPlayer,futurMedusa ,
						"Zone1_lvl2_Futur", game::changeRightScene, new Vector2f(1.0f, 1.0f)
				),
				new Medusa("pastMedusa",
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), futurPlayer),
				futurPlayer,
                sender
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
