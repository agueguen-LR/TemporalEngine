package games.temporalstudio.timecapsule.levels;

import org.joml.Vector2f;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import games.temporalstudio.temporalengine.rendering.component.View;

public class MainMenu implements SingleLevel{
	private Scene scene;

	public MainMenu(){
		this.scene = new Scene("MainMenu");

		GameObject bg = new GameObject("Background");
		bg.addComponent(new Transform(
			new Vector2f(-(640f/360 - 1)/2, 0),
			new Vector2f(640f/360, 1)
		));
		bg.addComponent(new TileRender("home_page", null));

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform());
		camera.addComponent(new View(1f));

		this.scene.addGameObject(camera);
		this.scene.addGameObject(bg);
	}

	public Scene getScene(){
		return scene;
	}
}
