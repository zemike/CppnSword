
public class Util {

	public static double gauss(double input){
		double sigma = 1;
		double mu = 0;
		
		double a = 1 /(sigma * Math.sqrt(2*Math.PI));
		double b = mu;
		double c = sigma;
		
		return a * Math.exp(- (input - b)*(input - b)/(2*c*c));
	}
}
