
public class GameEngine extends Thread {
	public boolean running = true;
	private RicePack pack[];
	private final int XSIZE, YSIZE, STEP = 1; 
	private GameDisplay view;
	private int x, y;
	
	public GameEngine(int nbPack, int Xsize, int Ysize, GameDisplay view){
		pack = new RicePack[nbPack];
		for(int i=0; i<pack.length; i++){
			pack[i] = new RicePack(0, Xsize, Ysize/2, Ysize, STEP);
		}
		
		this.XSIZE = Xsize;
		this.YSIZE = Ysize;
		this.view = view;
	}
	
	public void run(){
		
		while(running){
			try {
				this.sleep(5);
			} catch (InterruptedException e) {}
			for(int i=0; i<pack.length; i++){
				pack[i].nextStep();
				if(pack[i].isEnded()){
					pack[i] = new RicePack(0, XSIZE, YSIZE/2, YSIZE, STEP);
					System.out.println("tombé ! "+i);
				}
			}
			view.refreshDisplay();
		}

	}
	
	public int[][] getDrawPos(){
		int values[][] = new int[pack.length][2];
		for(int i=0; i<pack.length; i++){
			values[i][0] = (int)pack[i].getXpos();
			values[i][1] = YSIZE-(int)pack[i].getYpos();
		}
		return values;
	}
	
	/**
	 * définie position de départ d'une ligne de découpe
	 * @param x
	 * @param y
	 */
	public void setDownPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * indique la position de la fin d'une ligne de découpe<br>
	 * puis calcule et découpe les items sur cette ligne
	 * @param xup
	 * @param yup
	 */
	public void setUpPosition(int xup, int yup, int hitbox){
		LinearFunction func = new LinearFunction(x, y, xup, yup);
		int buff, funcResult, posY;
		int pos[][] = getDrawPos();
		
		for(int i=0; i<pos.length; i++){
			funcResult = func.function(pos[i][0]);
			
			buff = pos[i][1]-funcResult;
			if(buff < 0)
				buff = -buff;
			if(buff < hitbox && between(x, pos[i][0], xup) && between(y, funcResult, yup)){
				pack[i] = new RicePack(0, XSIZE, YSIZE/2, YSIZE, STEP);
			}
		}
	}
	
	public boolean between(int min, int val, int max){
		if(max < min){
			int buff = min;
			min = max;
			max = buff;
		}
		
		return (min <= val && val <= max);
	}

}
