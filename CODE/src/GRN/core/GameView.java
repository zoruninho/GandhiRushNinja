package GRN.core;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
	Paint paint, scorePaint;
	int sizeX;
	int sizeY;
	int x = 0, y = 0, xup = 0, yup = 0, combo = 0;
	int xs = 0, ys = 0;
	ArrayList<PointLine> points;
	
	public GameView(GameActivity activity) {
		super(activity);
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		sizeX=size.x;
		sizeY=size.y;
		
		engine = new GameEngine(Mode.PEACE, 3, size.x, size.y, this);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		ricepack = BitmapFactory.decodeResource(getResources(), R.drawable.ricepack);
		
		background=Bitmap.createScaledBitmap(background, size.x, size.y, true);
		
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

			// Pour dessiner à 50 fps
			/*try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}*/
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
		canvas.drawText("YOUR SCORE : "+engine.getScore(), 4*sizeX/5, sizeY/8, scorePaint);
		
		int dec = ricepack.getWidth()/2;
		int pos[][] = engine.getDrawPos();
		for(int i=0; i<pos.length; i++){
			canvas.drawBitmap(ricepack, (float)pos[i][0]-dec, (float)pos[i][1]-dec, null);
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
