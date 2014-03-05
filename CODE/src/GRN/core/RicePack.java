import java.util.Random;


public class RicePack {
	private int step = 0,
			byStep,
			dir=0, //0= g->d, 1=d->g
			//racine inférieure de la parabole
			min,
			//racine supérieure de la parabole
			max,
			//coefficient pour le sommet de la parabole
			b = 1,
			top = 1;
	
	
	public RicePack(int Xmin, int Xmax, int Ymin, int Ymax, int byStep){
		Random rand = new Random();
		
		//racine inférieure dans la première partie de la zone 
		min = Xmin+rand.nextInt((Xmax-Xmin)/2);
		//racine supérieure dans la seconde partie de la zone
		max = Xmax-rand.nextInt((Xmax-Xmin)/2);
		if(max-min < 200){
			max+=100;
			min-=100;
		}
		//maximum de la parabole de base
		top = function((min+max)/2);
		//coefficient d'aplanissement de la courbe  (hauteur souhaitée/hauteur par défaut)
		b = Ymin+rand.nextInt(Ymax-Ymin);
		
		dir = rand.nextInt(2);
		
		this.byStep = (max-min)/500;
		
	}
	
	public int getYpos(){
		if(dir==0)
			return function(min+step);
		else
			return function(max-step);
		
	}

	public int getXpos(){
		if(dir==0)
			return min+step;
		else
			return max-step;
		
	}
	
	public void nextStep(){
		step+=byStep;
	}
	
	public boolean isEnded(){
		int pos = this.getXpos();
		return !(min < pos & pos < max);
	}
	
	public int function(int x){
		return (int)(-1.f*(x-min)*(x-max)*(1.f*b/top));
	}
	
}
