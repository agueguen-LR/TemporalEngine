package games.temporalstudio.temporalengine.rendering;

public enum Layer{

	BACKGROUND(0, true),
	OBJECT(1, false),
	FOREGROUND(2, true),
	UI(3, false),
	EFFECT(4, false);

	private int z;
	private boolean forMapping;

	private Layer(int z, boolean forMapping){
		this.z = z;
		this.forMapping = forMapping;
	}

	// GETTERS
	public int z(){ return z; }
	public boolean isForMapping(){ return forMapping; }
}
