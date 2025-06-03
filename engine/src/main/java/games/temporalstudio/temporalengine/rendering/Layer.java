package games.temporalstudio.temporalengine.rendering;

public enum Layer{

	BACKGROUND(0),
	OBJECT(1),
	FOREGROUND(2),
	UI(3),
	EFFECT(4);

	private int z;

	private Layer(int z){
		this.z = z;
	}

	// GETTERS
	public int z(){
		return z;
	}
}
