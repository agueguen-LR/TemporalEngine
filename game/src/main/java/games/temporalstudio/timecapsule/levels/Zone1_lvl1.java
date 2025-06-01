package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.objects.*;

public class Zone1_lvl1 implements TimeLevel{
	private Scene pastScene;
	private Scene futurScene;

	public Zone1_lvl1(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer) {
		this.pastScene = new Scene("Zone1_lvl1_Past");
		this.futurScene = new Scene("Zone1_lvl1_Futur");


		Exit exitPast = new Exit(
				"Zone1_lvl1_Exit", 3.0f, 3.0f, pastPlayer.getGameObject(),
				"Zone1_lvl2_Past", game::changeLeftScene
		);
		Exit capsuleExit = new Exit(
				"Zone1_lvl1_CapsuleExit", 4.0f, 1.0f, pastPlayer.getGameObject(),
				"Zone1_pastCapsule", game::changeLeftScene
		);
		Exit exitFutur = new Exit(
				"Zone1_lvl1_Exit", 3.0f, 4.0f, futurPlayer.getGameObject(),
				"Zone1_lvl2_Futur", game::changeRightScene
		);

		this.pastScene.addGameObject(pastCamera);
		this.pastScene.addGameObject(pastPlayer.getGameObject());
		this.pastScene.addGameObject(exitPast.getGameObject());
		this.pastScene.addGameObject(capsuleExit.getGameObject());


		this.futurScene.addGameObject(exitFutur.getGameObject());
		this.futurScene.addGameObject(futurPlayer.getGameObject());
		this.futurScene.addGameObject(futurCamera);
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
