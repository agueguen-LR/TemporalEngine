package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.timecapsule.levels.*;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super(null, "icon.png");

		getLogger().info("Hello! There is nothing here for now...");

		MainMenu mainMenu = new MainMenu();
		setMainMenu(mainMenu.getScene());
		setFirstLeftScene(createPastScenes());
		setFirstRightScene(createFutureScenes());
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	private Scene createPastScenes(){
		Level zone1_lvl1 = new Zone1_lvl1();
		Zone1_pastCapsule zone1_pastCapsule = new Zone1_pastCapsule();
		Level zone1_lvl2 = new Zone1_lvl2();
		Level zone2 = new Zone2();
		Level zone3 = new Zone3();
		Level zone4 = new Zone4();

		Scene start = zone1_lvl1.getPastScene();
		start.addChild(zone1_pastCapsule.getScene());
		start.addChild(zone1_lvl2.getPastScene());
		Scene area2 = zone2.getPastScene();
		start.addChild(area2);
		Scene area3 = zone3.getPastScene();
		area2.addChild(area3);
		area3.addChild(zone4.getPastScene());

		return start;
	}

	private Scene createFutureScenes(){
		Level zone1_lvl1 = new Zone1_lvl1();
		Level zone1_lvl2 = new Zone1_lvl2();
		Level zone2 = new Zone2();
		Level zone3 = new Zone3();
		Level zone4 = new Zone4();

		Scene start = zone1_lvl1.getFuturScene();
		start.addChild(zone1_lvl2.getFuturScene());
		Scene area2 = zone2.getFuturScene();
		start.addChild(area2);
		Scene area3 = zone3.getFuturScene();
		area2.addChild(area3);
		area3.addChild(zone4.getFuturScene());

		return start;
	}
}