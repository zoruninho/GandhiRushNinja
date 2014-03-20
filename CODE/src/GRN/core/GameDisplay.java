package GRN.core;


public interface GameDisplay {
	
	public void refreshDisplay();
	public void gameOver(int score);
	public void packKilled(int x, int y);
}
