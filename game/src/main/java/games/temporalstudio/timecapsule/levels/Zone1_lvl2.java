package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.timecapsule.objects.*;

public class Zone1_lvl2 implements TimeLevel{
	private Scene pastScene;
	private Scene futurScene;

	public Zone1_lvl2(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer){
		this.pastScene = new Scene("Zone1_lvl2_Past");
		this.futurScene = new Scene("Zone1_lvl2_Futur");

		Wall wall1 = new Wall("Zone1_lvl2_Wall1", 1f, 5.0f);
		Exit exitPast = new Exit(
				"Zone1_lvl2_Exit", 3.0f, 5.0f, pastPlayer.getGameObject(),
				"Zone2_Past", game::changeLeftScene
		);

		this.pastScene.addGameObject(wall1.getGameObject());
		this.pastScene.addGameObject(pastPlayer.getGameObject());
		this.pastScene.addGameObject(exitPast.getGameObject());
		this.pastScene.addGameObject(pastCamera);

		Exit exitFutur = new Exit(
				"Zone1_lvl2_Exit", 3.0f, 6.0f, futurPlayer.getGameObject(),
				"Zone2_Futur", game::changeRightScene
		);

		this.futurScene.addGameObject(futurCamera);
		this.futurScene.addGameObject(futurPlayer.getGameObject());
		this.futurScene.addGameObject(exitFutur.getGameObject());

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
