package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;

public class Zone1_lvl2 implements TimeLevel{
	private Scene pastScene;
	private Scene futurScene;

	public Zone1_lvl2(GameObject pastCamera, GameObject futurCamera){
		this.pastScene = new Scene("Zone1_lvl2_Past");
		this.futurScene = new Scene("Zone1_lvl2_Futur");

		this.pastScene.addGameObject(pastCamera);
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
