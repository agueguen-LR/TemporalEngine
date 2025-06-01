package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.timecapsule.objects.TimeObject;

import java.util.Set;

public class Zone2 implements TimeLevel{
	private Scene pastScene;
	private Set<TimeObject> pastTimeObjects;
	private Scene futurScene;
	private Set<TimeObject> futurTimeObjects;

	public Zone2(GameObject pastCamera, GameObject futurCamera){
		this.pastScene = new Scene("Zone2_Past");
		this.futurScene = new Scene("Zone2_Futur");
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
