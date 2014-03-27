package GRN.core;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class GameEngine extends Thread {
	public boolean running = true;
	private ArrayList<RicePack> pack;
	private final int XSIZE, YSIZE, DEC;
	private final float OPEN = 0.5f;
	private GameDisplay view;
	private int x, y;
	private long time;
	private int life;
	private int score = 0;
	private Mode mode;
	private boolean pause = false;

	public GameEngine(Mode mode, int life, int Xsize, int Ysize,
			GameDisplay view, int dec) {
		this.mode = mode;
		this.DEC = dec;
		this.pack = new ArrayList<RicePack>();
		this.life = life;
		this.XSIZE = Xsize;
		this.YSIZE = Ysize;
		this.view = view;
	}

	public void run() {
		int w;
		int buffscore = 0;
		this.time = System.currentTimeMillis();
		long bufftime;
		pack.add(new RicePack(0 - (int) (XSIZE * OPEN), XSIZE
				+ (int) (XSIZE * OPEN), YSIZE / 2, YSIZE, mode));
		while (running) {
			try {
				this.sleep(5);
			} catch (InterruptedException e) {
			}

			while (pause) {
				Random rand = new Random();
				w = rand.nextInt();
			}

			for (int i = 0; i < pack.size(); i++) {
				pack.get(i).nextStep();
				if (pack.get(i).isEnded(this.XSIZE, DEC)) {
					if (pack.get(i).getType() == PackType.RICE)
					{
						Log.e("crash","crash");
						decreaseLife();
					}
					pack.remove(i);
					i--;
				}
			}

			bufftime = System.currentTimeMillis();
			if (bufftime - time > mode.delay()) {
				RicePack packrice = new RicePack(0 - (int) (XSIZE * OPEN),
						XSIZE + (int) (XSIZE * OPEN), YSIZE / 2, YSIZE, mode);
				pack.add(packrice);
				time = bufftime;
			}
			if (buffscore + 10 < score) {
				mode.increment();
				buffscore = score;
			}
			view.refreshDisplay();
		}

	}

	public int[][] getDrawPos() {
		ArrayList<RicePack> pack = (ArrayList<RicePack>) this.pack.clone();
		int values[][] = new int[pack.size()][3];
		for (int i = 0; i < pack.size(); i++) {
			values[i][0] = (int) pack.get(i).getXpos();
			values[i][1] = YSIZE - (int) pack.get(i).getYpos();
			values[i][2] = pack.get(i).getType().getValue();
		}
		return values;
	}

	/**
	 * définie position de départ d'une ligne de découpe
	 * 
	 * @param x
	 * @param y
	 */
	public void setDownPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * indique la position de la fin d'une ligne de découpe<br>
	 * puis calcule et découpe les items sur cette ligne
	 * 
	 * @param xup
	 * @param yup
	 */
	public int setUpPosition(int xup, int yup, int combo) {
		LinearFunction func = new LinearFunction(x, y, xup, yup);
		int buff, funcResult, posY;
		int pos[][] = getDrawPos();

		// assure la suppression du bon élément de la liste
		// cad augmente a chaque itération sauf si suppression
		// pour tenir compte du décalage de la liste après un remove()
		int differs = 0;

		for (int i = 0; i < pos.length; i++) {
			funcResult = func.function(pos[i][0]);

			buff = pos[i][1] - funcResult;
			if (buff < 0)
				buff = -buff;
			if (buff < DEC && between(x, pos[i][0], xup)
					&& between(y, funcResult, yup)) {
				if (pack.get(differs).getType() == PackType.MOUSSA)
					decreaseLife();
				else {
					score += ++combo;
					view.packKilled(pos[i][0], pos[i][1]);
				}
				pack.remove(differs);
			} else
				differs++;
		}

		return combo;
	}

	public boolean between(int min, int val, int max) {
		if (max < min) {
			int buff = min;
			min = max;
			max = buff;
		}

		return (min <= val && val <= max);
	}

	private void decreaseLife() {
		if (mode != Mode.PEACE)
			life--;
		if (life <= 0)
			gameOver();
	}

	private void gameOver() {
		this.running = false;
		Log.e("gameoverEngine","gameoverEngine");
		view.gameOver(score);
	}

	public int getScore() {
		return score;
	}

	public int getLife() {
		return life;
	}

	public void onPause() {
		pause = true;
	}

	public void onResume() {
		pause = false;
	}

}
