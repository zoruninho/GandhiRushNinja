package GRN.core;

public enum Level {
	EASY(2000),
	MEDIUM(1000),
	HARD(500);
	
	private int launchDelay;
	
	Level(int delay){
		this.launchDelay = delay;
	}
	
	public int delay(){
		return launchDelay;
	}
}
