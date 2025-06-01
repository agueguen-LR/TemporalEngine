package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.timecapsule.objects.*;

public class Zone1_pastCapsule implements SingleLevel{
	private Scene scene;

	public Zone1_pastCapsule(GameObject pastCamera, Game game, Player pastPlayer) {
		this.scene = new Scene("Zone1_pastCapsule");

		Exit exit = new Exit(
				"Zone1_pastCapsule_Exit", 1.0f, 1.0f, pastPlayer.getGameObject(),
				"Zone1_lvl1_Past", game::changeLeftScene
		);

		this.scene.addGameObject(pastPlayer.getGameObject());
		this.scene.addGameObject(exit.getGameObject());
		this.scene.addGameObject(pastCamera);
	}

	public Scene getScene(){
		return scene;
	}
}
