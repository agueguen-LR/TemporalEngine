package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.View;

public class MainMenu implements SingleLevel{
	private Scene scene;

	public MainMenu(){
		this.scene = new Scene("MainMenu");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform());
		camera.addComponent(new View(.1f));
		this.scene.addGameObject(camera);
	}

	public Scene getScene(){
		return scene;
	}
}
