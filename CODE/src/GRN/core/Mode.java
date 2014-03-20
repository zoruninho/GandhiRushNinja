package GRN.core;

public enum Mode {
	EASY(0.8f, 2000, 150),
	NORMAL(0.6f, 1000, 120),
	HARD(0.5f, 500, 100),
	PEACE(1.f, 500, 100);
	/*
	EASY(2000),
	MEDIUM(1000),
	HARD(500);
	
	
	Level(int delay){
		this.launchDelay = delay;
	}
	*/
	private int launchDelay;
	private float riceRate;
	private int stepRate;

	
	Mode(float f, int delay, int step){
		riceRate = f;
		launchDelay = delay;
		stepRate = step;
	}
	
	public static Mode getMode(int i){
		switch(i){
		case 1:
			return Mode.EASY;
		case 2:
			return Mode.NORMAL;
		case 3:
			return Mode.HARD;
		default:
			return Mode.PEACE;
		}
	}
	
	public float getRiceRate(){
		return riceRate;
	}
	public int delay(){
		return launchDelay;
	}
	
	public int getStepRate(){
		return stepRate;
	}
	
	public void increment(){
		launchDelay-=100;
		
		if(riceRate > 0)
			riceRate-=0.02f;
	}
}
