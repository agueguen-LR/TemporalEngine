package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.View;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super(null, "icon.png");

		getLogger().info("Hello! There is nothing here for now...");

		Scene mainMenu = new Scene("MainMenu");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform());
		camera.addComponent(new View(.1f));

		mainMenu.addGameObject(camera);
		setMainMenu(mainMenu);
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }
}