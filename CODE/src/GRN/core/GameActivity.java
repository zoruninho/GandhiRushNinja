package GRN.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class GameActivity extends Activity{
		// Identifiant de la boîte de dialogue de victoire
		public static final int VICTORY_DIALOG = 0;
		// Identifiant de la boîte de dialogue de défaite
		public static final int DEFEAT_DIALOG = 1;
	
		MediaPlayer tunak;
		String difficulty;
		Handler handler;
		Activity THIS = this;
		
		boolean musicActivated=false, soundsActivated=false;
		// Le moteur physique du jeu
		private GameView mView = null;
		
		private GameEngine engine = null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			int score = prefs.getInt("highscore", 0); //0 is the default value
			Log.e("score","score :"+score);
			super.onCreate(savedInstanceState);
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();  

			musicActivated=extras.getBoolean("musicActivated");
			soundsActivated=extras.getBoolean("soundsActivated");
			
			mView = new GameView(this, difficulty=extras.get("difficulty").toString(),soundsActivated);
			setContentView(mView);
			
			this.engine=mView.engine;
			
			if(musicActivated)
			{
				tunak = MediaPlayer.create(this, R.raw.tunak);
				tunak.setVolume(0.4f,0.4f);
				tunak.start();
			}
		}

		protected void onResume() {
			super.onResume();
			Log.e("resume","resume");
			mView = new GameView(this, difficulty, engine);
			mView.resume();
			if(musicActivated)
			{
				tunak.start();
			}
		} 

		@Override
		protected void onPause() {
			super.onStop();
			Log.e("stop","stop");
			engine=mView.engine;
			
			if(musicActivated)
			{
			tunak.pause();
			}
			
			mView.pause();
			mView.recycleAll();
		}

		@Override
		public Dialog onCreateDialog (int id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			Log.e("gameoverActivity","gameoverActivity");
			switch(id) {

			case DEFEAT_DIALOG:
				Log.e("gameoverActivityDIALOG","gameoverActivityDIALOG");
				if(musicActivated)
				{
					tunak.pause();
				}
				mView.pause();
				mView.recycleAll();
            	builder.setCancelable(false)
				.setMessage("Retentez votre chance ! Vous en ressortirez surement Gandhi !")
				.setTitle("C'est la fin ...")
				.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						reloadGame();
					}
				});
            	Log.e("gameoverActivityDIALOG2","gameoverActivityDIALOG2");
            break;
		    }
			Looper.prepare();
			Log.e("gameoverActivityEND","gameoverActivityEND");
			Dialog dialog = builder.create();
			dialog.show();

			Looper.loop();
			Looper.myLooper().quit();
			return  dialog;
		}

		@Override
		public void onPrepareDialog (int id, Dialog box) {
			// A chaque fois qu'une boîte de dialogue est lancée, on arrête le moteur physique
			//mEngine.stop();
		}
		
		public void reloadGame()
		{
			mView = new GameView(this, difficulty, soundsActivated);
			setContentView(mView);
			
			this.engine=mView.engine;
			tunak = MediaPlayer.create(this, R.raw.tunak);
			tunak.setVolume(0.4f,0.4f);
			tunak.start();
		}
		
		public void storeScore(int score)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("highscore", score);
			editor.commit();
		}
	}




	




