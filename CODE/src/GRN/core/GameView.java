package GRN.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView implements GameDisplay, SurfaceHolder.Callback{

	Bitmap background,ricepack;
	public GameEngine engine;
	private SurfaceHolder holder;
	int sizeX;
	int sizeY;
	int x = 0, y = 0, xup = 0, yup = 0;
	int xs = 0, ys = 0;
	
	public GameView(GameActivity activity) {
		super(activity);
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		sizeX=size.x;
		sizeY=size.y;
		
		engine = new GameEngine(Level.EASY, size.x, size.y, this);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		ricepack = BitmapFactory.decodeResource(getResources(), R.drawable.ricepack);
		
		background=Bitmap.createScaledBitmap(background, size.x, size.y, true);
		
  	  	holder = getHolder();

        holder.addCallback(this); 
	}

	@SuppressLint("WrongCall")
	@Override
	public void refreshDisplay() {
		// TODO Auto-generated method stub
		Canvas canvas;
		canvas = null;

			try {
				canvas = holder.lockCanvas();
				synchronized (holder) {
					onDraw(canvas);
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}

			// Pour dessiner à 50 fps
			/*try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}*/
		}
	
	protected void onDraw(Canvas canvas) 
    {
		Log.e("draw","onDraw");
		canvas.drawBitmap(background, 0, 0, null);
		
		int dec = ricepack.getWidth()/2;
		int pos[][] = engine.getDrawPos();
		for(int i=0; i<pos.length; i++){
			canvas.drawBitmap(ricepack, (float)pos[i][0]-dec, (float)pos[i][1]-dec, null);
		}
    }
	
	public boolean onTouchEvent(MotionEvent event) {
		
	  	if(event.getAction()==MotionEvent.ACTION_DOWN)
	  	{
	  		int dec = ricepack.getWidth()/2;
			xup = 0;
			x = (int)event.getX();
			y = (int)event.getY();
			engine.setDownPosition(x, y);
	  	}
	  	
	  	if(event.getAction()==MotionEvent.ACTION_UP)
	  	{
	  		int dec = ricepack.getWidth()/2;

			int pos[][] = engine.getDrawPos();
			xs = pos[0][0]-dec;
			ys = pos[0][1]-dec;
			
			xup = (int)event.getX();
			yup = (int)event.getY();
			engine.setUpPosition(xup, yup, dec);
	  	}
  		return true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) 
    {
		 //for stoping the game
     }
    
    @SuppressLint("WrongCall")
    public void surfaceCreated(SurfaceHolder holder) 
    {
    	Log.e("view","surfaceCreated");
    	engine.start();
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
