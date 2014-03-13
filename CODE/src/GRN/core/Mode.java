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
	
	public float getRiceRate(){
		return riceRate;
	}
	public int delay(){
		return launchDelay;
	}
	
	public int getStepRate(){
		return stepRate;
	}
}
