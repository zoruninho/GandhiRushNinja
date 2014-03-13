package GRN.core;

import java.util.ArrayList;

import android.util.Log;


public class GameEngine extends Thread {
	public boolean running = true;
	private ArrayList<RicePack> pack;
	private final int XSIZE, YSIZE, STEP = 1; 
	private GameDisplay view;
	private int x, y;
	private Level level;
	private long time;
	
	public GameEngine(Level level, int Xsize, int Ysize, GameDisplay view){
		this.level = level;
		this.pack = new ArrayList<RicePack>();
		this.XSIZE = Xsize;
		this.YSIZE = Ysize;
		this.view = view;
	}
	
	public void run(){
		Log.e("engine", "lancement engine");
		this.time = System.currentTimeMillis();
		long bufftime;
		pack.add(new RicePack(0, XSIZE, YSIZE/2, YSIZE, STEP));
		while(running){
			try {
				this.sleep(5);
			} catch (InterruptedException e) {}
			for(int i=0; i<pack.size(); i++){
				pack.get(i).nextStep();
				if(pack.get(i).isEnded()){
					pack.remove(i);
				}
			}
			
			bufftime=System.currentTimeMillis();
			if(bufftime-time > level.delay()){
				RicePack packrice = new RicePack(0, XSIZE, YSIZE/2, YSIZE, STEP); 
				pack.add(packrice);
				//FIXME erreur de suivi des courbes
				//provient du pas de déplacement
				
				time = bufftime;
			}
			view.refreshDisplay();
		}

	}
	
	public int[][] getDrawPos(){
		int values[][] = new int[pack.size()][2];
		for(int i=0; i<pack.size(); i++){
			values[i][0] = (int)pack.get(i).getXpos();
			values[i][1] = YSIZE-(int)pack.get(i).getYpos();
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
		
		//assure la suppression du bon élément de la liste 
		//cad augmente a chaque itération sauf si suppression
		//pour tenir compte du décalage de la liste après un remove()
		int differs = 0;
		
		for(int i=0; i<pos.length; i++){
			funcResult = func.function(pos[i][0]);
			
			buff = pos[i][1]-funcResult;
			if(buff < 0)
				buff = -buff;
			if(buff < hitbox && between(x, pos[i][0], xup) && between(y, funcResult, yup)){
				pack.remove(differs);
			}
			else
				differs++;
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
