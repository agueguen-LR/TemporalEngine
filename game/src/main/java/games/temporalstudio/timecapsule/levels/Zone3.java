package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.View;

public class Zone3 implements Level{
	private Scene pastScene;
	private Scene futurScene;

	public Zone3(){
		this.pastScene = new Scene("Zone3_Past");
		this.futurScene = new Scene("Zone3_Futur");

		GameObject pastCamera = new GameObject("pastCamera");
		pastCamera.addComponent(new Transform());
		pastCamera.addComponent(new View(.1f));
		this.pastScene.addGameObject(pastCamera);

		GameObject futurCamera = new GameObject("futurCamera");
		futurCamera.addComponent(new Transform());
		futurCamera.addComponent(new View(.1f));
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
