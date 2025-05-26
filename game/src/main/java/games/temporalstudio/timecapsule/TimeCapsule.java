package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;

public class TimeCapsule extends Game{
	private static TimeCapsule instance;
	private Scene scene;

	private TimeCapsule(String title) {
		super(title);
		TimeCapsule.instance = this;
	}

	public static TimeCapsule get(){
		if (TimeCapsule.instance == null) {
			TimeCapsule.instance = new TimeCapsule("Time Capsule");
		}
		return TimeCapsule.instance;
	}

}
