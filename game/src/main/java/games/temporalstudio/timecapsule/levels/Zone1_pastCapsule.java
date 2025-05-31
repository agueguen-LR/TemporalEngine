package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;

public class Zone1_pastCapsule implements SingleLevel{
	private Scene scene;

	public Zone1_pastCapsule(GameObject pastCamera){
		this.scene = new Scene("MainMenu");

		this.scene.addGameObject(pastCamera);
	}

	public Scene getScene(){
		return scene;
	}
}
