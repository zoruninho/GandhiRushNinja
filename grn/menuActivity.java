    case R.id.bSon:
	        	Log.e("MuteSong","MuteSong");
	        	/*if( findViewById(R.id.bSon).getBackground() == "son.png" )
	        	 findViewById(R.id.bSon).setBackgroundResource(R.drawable.sonmute);
	        	else 
		        	 findViewById(R.id.bSon).setBackgroundResource(R.drawable.son);*/
	        break;
	        case R.id.bMusic:
	        	Log.e("MuteMusic","MuteMusic");
	        	/*if( ! findViewById(R.id.bMusic).getBackground() == "musique" )
		        	 findViewById(R.id.bMusic).setBackgroundResource(R.drawable.musique);
		        	else 
			        	 findViewById(R.id.bSon).setBackgroundResource(R.drawable.son);*/
	        break;
	        case R.id.bHighScore:
	        	 AlertDialog.Builder builder_hg = new AlertDialog.Builder(this);
	        	 LayoutInflater inflater = getLayoutInflater();
	        	    final View hglay = inflater.inflate(R.layout.popup_hg, null);
	        	    builder_hg.setView(hglay);
	        	    AlertDialog alerthg = builder_hg.create();
		            alerthg.show();
	        	break;