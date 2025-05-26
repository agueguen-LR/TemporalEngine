package games.temporalstudio.timecapsule;

import games.temporalstudio.temporalengine.Game;

public class TimeCapsule extends Game{
	private static TimeCapsule instance;

	private TimeCapsule() {
		TimeCapsule.instance = this;
	}

	public static TimeCapsule get(){
		if (TimeCapsule.instance == null) {
			TimeCapsule.instance = new TimeCapsule();
		}
		return TimeCapsule.instance;
	}
}
