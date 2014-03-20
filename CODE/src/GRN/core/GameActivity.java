package GRN.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends Activity{
		// Identifiant de la boîte de dialogue de victoire
		public static final int VICTORY_DIALOG = 0;
		// Identifiant de la boîte de dialogue de défaite
		public static final int DEFEAT_DIALOG = 1;
	
		MediaPlayer tunak;
		String difficulty;
		
		// Le moteur physique du jeu
		private GameView mView = null;
		
		private GameEngine engine = null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();  

			mView = new GameView(this, difficulty=extras.get("difficulty").toString());
			setContentView(mView);
			
			this.engine=mView.engine;
			tunak = MediaPlayer.create(this, R.raw.tunak);
			tunak.setVolume(0.4f,0.4f);
			tunak.start();
		}

		protected void onResume() {
			super.onResume();
			Log.e("resume","resume");
			mView.reload(this, difficulty, engine);
			mView.resume();
		} 

		@Override
		protected void onPause() {
			super.onStop();
			Log.e("stop","stop");
			engine=mView.engine;
			mView.pause();
		}

		@Override
		public Dialog onCreateDialog (int id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			switch(id) {
			case VICTORY_DIALOG:
				builder.setCancelable(false)
				.setMessage("Bravo, vous avez gagné !")
				.setTitle("Champion ! Le roi des Zörglubienotchs est mort grâce à vous !")
				.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// L'utilisateur peut recommencer s'il le veut
						//mEngine.reset();
						//mEngine.resume();
					}
				});
				break;

			case DEFEAT_DIALOG:
				builder.setCancelable(false)
				.setMessage("La Terre a été détruite à cause de vos erreurs.")
				.setTitle("Bah bravo !")
				.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//mEngine.reset();
						//mEngine.resume();
					}
				});
			}
			return builder.create();
		}

		@Override
		public void onPrepareDialog (int id, Dialog box) {
			// A chaque fois qu'une boîte de dialogue est lancée, on arrête le moteur physique
			//mEngine.stop();
		}
	}




	




