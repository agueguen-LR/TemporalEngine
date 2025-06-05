package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;

public class Zone3 implements TimeLevel{

	private Scene pastScene;
	private Scene futurScene;

	public Zone3(GameObject pastCamera, GameObject futurCamera){
		this.pastScene = new Scene("Zone3_Past");
		this.futurScene = new Scene("Zone3_Futur");
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
