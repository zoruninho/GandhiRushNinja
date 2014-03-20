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

public class Menu extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    Button newGame = (Button)findViewById(R.id.bNew);
	    Button exit = (Button)findViewById(R.id.bExit);
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
	        case R.id.bNew:
	            final String[] levels = {"Easy", "Moyen", "Hard"};
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("Select difficulty");
	            builder.setItems(levels, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                	Intent game = new Intent(Menu.this,GRN.core.GameActivity.class);
	                	game.putExtra("difficulty", item+1);
	                	startActivity(game);
	                }
	            });
	            AlertDialog alert = builder.create();
	            alert.show();
	    }
	}
	
}
