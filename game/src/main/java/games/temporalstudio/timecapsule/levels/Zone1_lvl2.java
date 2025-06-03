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

	public Zone1_lvl2(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer){
		this.pastScene = new Scene("Zone1_lvl2_Past");
		this.pastScene.addGameObject(pastCamera);
		this.futurScene = new Scene("Zone1_lvl2_Futur");
		this.futurScene.addGameObject(futurCamera);

		this.pastTimeObjects = Set.of(
				new Enemy("dracula",new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f[]{new Vector2f(2,2), new Vector2f(5,1),
								new Vector2f(1,3)}, pastScene),
				new Wall("Zone1_lvl2_Wall2", 2f, 6.0f),
				new Wall("Zone1_lvl2_Wall3", 3f, 5.0f),
				new Wall("Zone1_lvl2_Wall4", 4f, 4.0f),
				new Wall("Zone1_lvl2_Wall5", 5f, 3.0f),
				new Exit(
						"Zone1_lvl2_Exit", 3.0f, 6.0f, pastPlayer.getGameObject(),
						"Zone2_Past", game::changeLeftScene),
				new Medusa("pastMedusa",
						(int)pastPlayer.getTransform().getPosition().x, (int)pastPlayer.getTransform().getPosition().y,
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), pastPlayer),
				pastPlayer
		);

		this.pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));

		this.futurTimeObjects = Set.of(
				new Enemy("dracula",new Vector4f(0,0.5f, 0.75f, 1),
						new Vector2f[]{new Vector2f(2,2), new Vector2f(5,1),
								new Vector2f(1,3)}, futurScene),
				new Wall("Zone1_lvl2_Wall2", 2f, 6.0f),
				new Wall("Zone1_lvl2_Wall3", 3f, 5.0f),
				new Wall("Zone1_lvl2_Wall4", 4f, 4.0f),
				new Wall("Zone1_lvl2_Wall5", 5f, 3.0f),
				new Exit(
						"Zone1_lvl2_Exit", 3.0f, 6.0f, futurPlayer.getGameObject(),
						"Zone2_Futur", game::changeRightScene),
				new Medusa("pastMedusa",
						(int)futurPlayer.getTransform().getPosition().x, (int)futurPlayer.getTransform().getPosition().y,
						new Vector2f(0.5f, 0.5f),
						new Vector4f(0.25f,0,0.75f,1), futurPlayer),
				futurPlayer
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
