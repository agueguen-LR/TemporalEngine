package games.temporalstudio.timecapsule;

import static org.lwjgl.glfw.GLFW.*;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.View;
import games.temporalstudio.timecapsule.levels.*;
import games.temporalstudio.timecapsule.objects.CapsuleReceiver;
import games.temporalstudio.timecapsule.objects.Player;
import games.temporalstudio.timecapsule.objects.Seed;

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

		Player pastPlayer = new Player("pastPlayer", 1, 1, new int[]{
				GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_E, GLFW_KEY_Q
		});
		Player futurePlayer = new Player("futurePlayer", 1, 1, new int[]{
				GLFW_KEY_UP, GLFW_KEY_LEFT, GLFW_KEY_DOWN, GLFW_KEY_RIGHT, GLFW_KEY_ENTER, GLFW_KEY_RIGHT_CONTROL
		});

		CapsuleReceiver zone1_pastCapsuleReceiver = new CapsuleReceiver("zone1_pastCapsuleReceiver", 3.0f, 3.0f);

		Map<String, Level> levels = Map.of(
				"cave1", new Zone1_lvl1(pastCamera, futureCamera, this, pastPlayer, futurePlayer, zone1_pastCapsuleReceiver),
				"cave2", new Zone1_lvl2(pastCamera, futureCamera, this, pastPlayer, futurePlayer),
				"caveCapsulePast", new Zone1_pastCapsule(pastCamera, this, pastPlayer, zone1_pastCapsuleReceiver),
				"factory", new Zone2(pastCamera, futureCamera),
				"boat", new Zone3(pastCamera, futureCamera),
				"finale", new Zone4(pastCamera, futureCamera)
		);

		zone1_pastCapsuleReceiver.setScene(((SingleLevel)levels.get("caveCapsulePast")).getScene());

		setFirstLeftScene(createPastScenes(levels));
		setFirstRightScene(createFutureScenes(levels));
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	private Scene createPastScenes(Map<String, Level> levels){

		Scene cave1 = ((TimeLevel)levels.get("cave1")).getPastScene();
		Scene cave2 = ((TimeLevel)levels.get("cave2")).getPastScene();
		Scene caveCapsulePast = ((SingleLevel)levels.get("caveCapsulePast")).getScene();
		Scene factory = ((TimeLevel)levels.get("factory")).getPastScene();
		Scene boat = ((TimeLevel)levels.get("boat")).getPastScene();
		Scene finale = ((TimeLevel)levels.get("finale")).getPastScene();

		cave1.addChild(cave2);
		cave1.addChild(caveCapsulePast);
		cave2.addChild(factory);
		factory.addChild(boat);
		boat.addChild(finale);

		return cave1;
	}

	private Scene createFutureScenes(Map<String, Level> levels){

		Scene cave1 = ((TimeLevel)levels.get("cave1")).getFuturScene();
		Scene cave2 = ((TimeLevel)levels.get("cave2")).getFuturScene();
		Scene factory = ((TimeLevel)levels.get("factory")).getFuturScene();
		Scene boat = ((TimeLevel)levels.get("boat")).getFuturScene();
		Scene finale = ((TimeLevel)levels.get("finale")).getFuturScene();

		cave1.addChild(cave2);
		cave2.addChild(factory);
		factory.addChild(boat);
		boat.addChild(finale);

		return cave1;
	}
}