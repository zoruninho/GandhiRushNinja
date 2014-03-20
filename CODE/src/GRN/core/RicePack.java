package GRN.core;
import java.util.Random;


public class RicePack {
	private PackType type;
	private float step = 0,
			byStep;
	private int		dir=0, //0= g->d, 1=d->g
			//racine inférieure de la parabole
			min,
			//racine supérieure de la parabole
			max,
			//coefficient pour le sommet de la parabole
			b = 1,
			top = 1;
	
	public RicePack(int Xmin, int Xmax, int Ymin, int Ymax, Mode mode){
		Random rand = new Random();
		float f = rand.nextFloat();
		if(f <= mode.getRiceRate())
			type = PackType.RICE;
		else
			type = PackType.MOUSSA;
		
		//racine inférieure dans la première partie de la zone 
		min = Xmin+rand.nextInt((Xmax-Xmin)/2);
		//racine supérieure dans la seconde partie de la zone
		max = Xmax-rand.nextInt((Xmax-Xmin)/2);
		if(max-min < (Xmax-Xmin)/4){
			max+=(Xmax-Xmin)/8;
			min-=(Xmax-Xmin)/8;
		}
		//maximum de la parabole de base
		top = function((min+max)/2);
		//coefficient d'aplanissement de la courbe  (hauteur souhaitée/hauteur par défaut)
		b = Ymin+rand.nextInt(Ymax-Ymin);
		
		dir = rand.nextInt(2);
		
		//this.byStep = byStep;
		this.byStep = 1.0f*(max-min)/mode.getStepRate();
	}
	
	public int getYpos(){
		if(dir==0)
			return function(min+step);
		else
			return function(max-step);
		
	}

	public int getXpos(){
		if(dir==0)
			return (int)(min+step);
		else
			return (int)(max-step);
		
	}
	
	public PackType getType(){
		return type;
	}
	
	public void nextStep(){
		step+=byStep;
	}
	
	public boolean isEnded(int width, int dec){
		int y = this.getYpos();
		int x = this.getXpos();
		return (y < 0 || (dir == 1 && x < -dec) || (dir == 0 && width < x-dec));
	}
	
	public int function(float x){
		return (int)(-1.f*(x-min)*(x-max)*(1.f*b/top));
	}

	public String getFunction() {
		return new String("-(x-"+min+")*(x-"+max+")*("+b+"/"+top+") = "+"("+1.0f*b/top+")");
	}
	
}
