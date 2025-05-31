package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.View;
import games.temporalstudio.timecapsule.levels.*;

import java.util.Map;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super(null, "icon.png");

		getLogger().info("Hello! There is nothing here for now...");

		MainMenu mainMenu = new MainMenu();
		setMainMenu(mainMenu.getScene());

		GameObject pastCamera = new GameObject("pastCamera");
		pastCamera.addComponent(new Transform());
		pastCamera.addComponent(new View(.1f));

		GameObject futureCamera = new GameObject("futureCamera");
		futureCamera.addComponent(new Transform());
		futureCamera.addComponent(new View(.1f));

		Map<String, Level> levels = Map.of(
				"cave1", new Zone1_lvl1(pastCamera, futureCamera),
				"cave2", new Zone1_lvl2(pastCamera, futureCamera),
				"caveCapsulePast", new Zone1_pastCapsule(pastCamera),
				"factory", new Zone2(pastCamera, futureCamera),
				"boat", new Zone3(pastCamera, futureCamera),
				"finale", new Zone4(pastCamera, futureCamera)
		);

		setFirstLeftScene(createPastScenes(levels));
		setFirstRightScene(createFutureScenes(levels));
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	private Scene createPastScenes(Map<String, Level> levels){

		Scene start = ((TimeLevel)levels.get("cave1")).getPastScene();
		start.addChild(((SingleLevel)levels.get("caveCapsulePast")).getScene());
		start.addChild(((TimeLevel)levels.get("cave2")).getPastScene());
		Scene area2 = ((TimeLevel)levels.get("factory")).getPastScene();
		start.addChild(area2);
		Scene area3 = ((TimeLevel)levels.get("boat")).getPastScene();
		area2.addChild(area3);
		area3.addChild(((TimeLevel)levels.get("finale")).getPastScene());

		return start;
	}

	private Scene createFutureScenes(Map<String, Level> levels){

		Scene start = ((TimeLevel)levels.get("cave1")).getFuturScene();
		start.addChild(((TimeLevel)levels.get("cave2")).getFuturScene());
		Scene area2 = ((TimeLevel)levels.get("factory")).getFuturScene();
		start.addChild(area2);
		Scene area3 = ((TimeLevel)levels.get("boat")).getFuturScene();
		area2.addChild(area3);
		area3.addChild(((TimeLevel)levels.get("finale")).getFuturScene());

		return start;
	}
}