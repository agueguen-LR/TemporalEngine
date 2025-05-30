package games.temporalstudio.timecapsule;

import games.temporalstudio.timecapsule.test.TestGame;

public class Launcher{

	public static void main(String[] args){
		boolean isEngineTest = false;

		if(args.length > 0 && args[0].equalsIgnoreCase("engine"))
			isEngineTest = true;

		if(!isEngineTest)
			new TimeCapsule().run(new String[]{"Time Capsule"});
		else
			new TestGame().run(new String[0]);
	}
}
