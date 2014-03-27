package GRN.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;


public class Menu extends Activity implements OnClickListener {

	boolean musicActivated = true, soundsActivated=true;
	ImageButton music, sounds;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    music = (ImageButton)findViewById(R.id.bMusic);
	    sounds = (ImageButton)findViewById(R.id.bSon);
	    Button newGame = (Button)findViewById(R.id.bNew);
	    Button exit = (Button)findViewById(R.id.bExit);
	    
	    music.setOnClickListener(this);
	    sounds.setOnClickListener(this);
	    newGame.setOnClickListener(this);
	    exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
	    //check which button was clicked with its id
	    switch(view.getId()) {
	        case R.id.bExit:
	            finish();
	            break;
	        case R.id.bMusic:
	            if(musicActivated)
	            {
	            	musicActivated=false;
	            	music.setImageResource(R.drawable.musiquemute);
	            }
	            else
	            {
	            	musicActivated=true;
	            	music.setImageResource(R.drawable.musique);
	            }
	            break;
	        case R.id.bSon:
	            if(soundsActivated)
	            {
	            	soundsActivated=false;
	            	sounds.setImageResource(R.drawable.sonmute);
	            }
	            else
	            {
	            	soundsActivated=true;
	            	sounds.setImageResource(R.drawable.son);
	            }
	            break;
	        case R.id.bNew:
	            final String[] levels = {"Easy", "Moyen", "Hard"};
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("Select difficulty");
	            builder.setItems(levels, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                	Intent game = new Intent(Menu.this,GRN.core.GameActivity.class);
	                	game.putExtra("difficulty", item+1);
	                	game.putExtra("musicActivated", musicActivated);
	                	game.putExtra("soundsActivated", soundsActivated);
	                	startActivity(game);
	                }
	            });
	            AlertDialog alert = builder.create();
	            alert.show();
	    }
	}
	
}
