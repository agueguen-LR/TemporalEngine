package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.timecapsule.Entity.Enemy;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Set;

public class Zone1_lvl2 implements TimeLevel{
	private Scene pastScene;
	private Set<TimeObject> pastTimeObjects;
	private Scene futurScene;
	private Set<TimeObject> futurTimeObjects;

	public Zone1_lvl2(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer, Medusa pastMedusa, Medusa futureMedusa, CapsuleReceiver zone1_pastCapsuleReceiver) {
		this.pastScene = new Scene("Zone1_lvl2_Past");
		this.pastScene.addGameObject(pastCamera);
		this.futurScene = new Scene("Zone1_lvl2_Futur");
		this.futurScene.addGameObject(futurCamera);


		this.pastTimeObjects = Set.of(
				new Enemy(new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f(),
						new Vector2f[]{new Vector2f(2,6), new Vector2f(5,1),new Vector2f(0,3)},
						pastScene),
				new Wall(new Vector2f(1, 5), new Vector2f(6, 6)),

				new Exit(
						"Zone1_lvl2_Exit", 3.0f, 6.0f, pastPlayer,pastMedusa ,
						"Zone2_Past", game::changeLeftScene, new Vector2f(1.0f, 1.0f)),
				new Medusa("pastMedusa",
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), pastPlayer),
				pastPlayer,
				zone1_pastCapsuleReceiver

		);

		this.pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));

		this.futurTimeObjects = Set.of(
				new Enemy(new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f(),
						new Vector2f[]{new Vector2f(2,6), new Vector2f(5,1),new Vector2f(0,3)},
						futurScene),
				new Wall(new Vector2f(1, 5), new Vector2f(6, 6)),
				new Exit(
						"Zone1_lvl2_Exit", 3.0f, 6.0f, futurPlayer, futureMedusa,
						"Zone2_Futur", game::changeRightScene, new Vector2f(1.0f, 1.0f)),
				new Medusa("pastMedusa",
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), futurPlayer),
				futurPlayer,
				new Seed("The seedTM",futurPlayer)
		);

		this.futurTimeObjects.forEach((timeObject) -> this.futurScene.addGameObject(timeObject.getGameObject()));
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
