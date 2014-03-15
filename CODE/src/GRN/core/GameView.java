package GRN.core;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView implements GameDisplay, SurfaceHolder.Callback{

	Bitmap background,ricepack, moussaka, life, lifeLost;
	MediaPlayer ninja;
	public GameEngine engine;
	private SurfaceHolder holder;
	Paint paint, scorePaint;
	int sizeX;
	int sizeY;
	int x = 0, y = 0, xup = 0, yup = 0, combo = 0;
	int xs = 0, ys = 0;
	ArrayList<PointLine> points;
	boolean music=true;
	
	public GameView(GameActivity activity) {
		super(activity);
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		sizeX=size.x;
		sizeY=size.y;
		
		engine = new GameEngine(Mode.HARD, 3, size.x, size.y, this);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		ricepack = BitmapFactory.decodeResource(getResources(), R.drawable.ricepack);
		moussaka = BitmapFactory.decodeResource(getResources(), R.drawable.moussaka);
		life = BitmapFactory.decodeResource(getResources(), R.drawable.vie_indien);
		lifeLost = BitmapFactory.decodeResource(getResources(), R.drawable.vie_perdue);
		
		background=Bitmap.createScaledBitmap(background, size.x, size.y, true);
		moussaka=Bitmap.createScaledBitmap(moussaka, ricepack.getHeight(), ricepack.getWidth(), true);
		life=Bitmap.createScaledBitmap(life, life.getWidth()/4, life.getHeight()/4, true);
		lifeLost=Bitmap.createScaledBitmap(lifeLost, lifeLost.getWidth()/4, lifeLost.getHeight()/4, true);
		
		//Ninja
		ninja=MediaPlayer.create(activity,R.raw.ninja);
		
  	  	holder = getHolder();

        holder.addCallback(this); 
        
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        
        scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setStyle(Paint.Style.STROKE);
        scorePaint.setStrokeWidth(2);
        scorePaint.setTextSize(20);
        scorePaint.setColor(Color.WHITE);
        
        points = new ArrayList<PointLine>();
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
		}
	
	protected void onDraw(Canvas canvas) 
    {
		if(!points.isEmpty())
		{
	  		if((System.currentTimeMillis()-points.get(0).time)>700)
	  		{
	  			points.remove(0);
	  		}
		}
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawText("YOUR SCORE : "+engine.getScore(), 4*sizeX/5, sizeY/4, scorePaint);
		
		//Les vies
		for(int i=0;i<3;i++)
		{
			if(i<engine.getLife())
			{
				canvas.drawBitmap(life, (sizeX/15)+80*i, sizeY/15, null);
			}
			else
			{
				canvas.drawBitmap(lifeLost, (sizeX/15)+80*i, sizeY/15, null);
			}
		}
		
		int dec = ricepack.getWidth()/2;
		int pos[][] = engine.getDrawPos();
		for(int i=0; i<pos.length; i++){
			if(PackType.getType(pos[i][2])==PackType.MOUSSA)
			{
				canvas.drawBitmap(moussaka, (float)pos[i][0]-dec, (float)pos[i][1]-dec, null);
			}
			else
			{
				canvas.drawBitmap(ricepack, (float)pos[i][0]-dec, (float)pos[i][1]-dec, null);
			}
		}
		
		Path path = new Path();

	    if(points.size() > 1){
	        for(int i = points.size() - 2; i < points.size(); i++){
	            if(i >= 0){
	                PointLine point = points.get(i);

	                if(i == 0){
	                    PointLine next = points.get(i + 1);
	                    point.dx = ((next.x - point.x) / 3);
	                    point.dy = ((next.y - point.y) / 3);
	                }
	                else if(i == points.size() - 1){
	                    PointLine prev = points.get(i - 1);
	                    point.dx = ((point.x - prev.x) / 3);
	                    point.dy = ((point.y - prev.y) / 3);
	                }
	                else{
	                    PointLine next = points.get(i + 1);
	                    PointLine prev = points.get(i - 1);
	                    point.dx = ((next.x - prev.x) / 3);
	                    point.dy = ((next.y - prev.y) / 3);
	                }
	            }
	        }
	    }

	    boolean first = true;
	    for(int i = 0; i < points.size(); i++){
	        PointLine point = points.get(i);
	        if(first){
	            first = false;
	            path.moveTo(point.x, point.y);
	        }
	        else{
	            PointLine prev = points.get(i - 1);
	            path.cubicTo(prev.x + prev.dx, prev.y + prev.dy, point.x - point.dx, point.y - point.dy, point.x, point.y);
	        }
	    }
	    canvas.drawPath(path, paint);
	    
	    // La musique de fond
	    
	    
    }
	
	public boolean onTouchEvent(MotionEvent event) {
		
	  	if(event.getAction()==MotionEvent.ACTION_DOWN)
	  	{
	  		points.removeAll(points);
	  		combo=0;
	  		int dec = ricepack.getWidth()/2;
			xup = 0;
			x = (int)event.getX();
			y = (int)event.getY();
			engine.setDownPosition(x, y);
			
			PointLine point = new PointLine();
	        point.x = (int)event.getX();
	        point.y = (int)event.getY();
	        point.time = System.currentTimeMillis();
	        points.add(point);
	        invalidate();
	  	}
	  	
	  	if(event.getAction()==MotionEvent.ACTION_UP)
	  	{
	  		int dec = ricepack.getWidth()/2;

			int pos[][] = engine.getDrawPos();
			
			xup = (int)event.getX();
			yup = (int)event.getY();
			combo = engine.setUpPosition(xup, yup, dec, combo);
			if(combo>0)
			{
				Log.e("ninja","ninja");
				ninja.start();
			}
			
			PointLine point = new PointLine();
	        point.x = (int)event.getX();
	        point.y = (int)event.getY();
	        point.time = System.currentTimeMillis();
	        points.add(point);
	        invalidate();
	  	}
	  	
	  	if(event.getAction()==MotionEvent.ACTION_MOVE)
	  	{
	  		int dec = ricepack.getWidth()/2;
	  		int pos[][] = engine.getDrawPos();
			
			xup = (int)event.getX();
			yup = (int)event.getY();
			combo = engine.setUpPosition(xup, yup, dec, combo);
			if(combo>0)
			{
				Log.e("ninja","ninja");
				ninja.start();
			}
			
	  		if(points.size()>10)
	  		{
	  			points.remove(0);
	  		}
			PointLine point = new PointLine();
	        point.x = (int)event.getX();
	        point.y = (int)event.getY();
	        points.add(point);
	        invalidate();
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
	
	class PointLine extends Point{
	    float x, y;
	    float dx, dy;
	    long time;

	    @Override
	    public String toString() {
	        return x + ", " + y;
	    }
	}

	@Override
	public void gameOver(int score) {
		// TODO Auto-generated method stub
		Canvas canvas;
		canvas = null;
		canvas.drawText("GAMEOVER OVER", sizeX/2, sizeY/2, null);
	}
}
