package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	private Scene scene;

	public TimeCapsule(){
		super();

		setTitle(getI18n().getSentence("game.title", getVersion()));
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }
}
