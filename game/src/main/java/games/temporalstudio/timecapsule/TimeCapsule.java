package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super();

		setTitle(getI18n().getSentence("game.title", getVersion()));
		setMainMenu(new Scene("MainMenu"));
		setFirstLeftScene(this.createPastScenes());
		setFirstRightScene(this.createFutureScenes());
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	public Scene createPastScenes(){
		Scene past = new Scene("Past");
		past.addChild(new Scene("PastChild1"));
		past.addChild(new Scene("PastChild2"));
		past.addChild(new Scene("PastChild3"));
		Scene pastChild4 = new Scene("PastChild4");
		past.addChild(pastChild4);
		pastChild4.addChild(new Scene("PastChild4Child1"));
		return past;
	}

	public Scene createFutureScenes() {
		Scene future = new Scene("Future");
		future.addChild(new Scene("FutureChild1"));
		future.addChild(new Scene("FutureChild2"));
		future.addChild(new Scene("FutureChild3"));
		Scene futureChild4 = new Scene("FutureChild4");
		future.addChild(futureChild4);
		futureChild4.addChild(new Scene("FutureChild4Child1"));
		return future;
	}
}
