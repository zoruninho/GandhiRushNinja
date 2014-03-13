package GRN.core;

public class LinearFunction {
	private float a = 0, b = 0;
	
	public LinearFunction(int x, int y, int xup, int yup){
		a = ((float)yup-(float)y)/((float)xup-(float)x);
		b = (float)y - a*(float)x;
	}
	
	public int function(int x){
		return (int) (a*x+b);
	}

}
