package linear_regression;
import java.lang.Math;

public class Update {
	//cost function for all training examples
	double[][] x;
	double[] y;
	double[] theta;
	double alpha;
	double[][] x_bias;
	double m;
	
	//each row of x should be a training example
	public Update(double[][] x, double[] y) {
		this.x = x;
		this.y = y;
		this.x_bias = insertBias(x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
	}
	
	//
	public double cost(double[][] x, double[] y, double[] theta) {
		double sum = 0;
		double[] inner = hypothesisAll_minus_y(x, y, theta);
		for(double i: inner) {
			sum+= Math.pow(i, 2);
		}
		return sum/(2*m);
	}
	
	//hypothesis for all training examples - y value for the corresponding example
	public double[] hypothesisAll_minus_y(double[][] x, double[] y, double[] theta) {
		double h_all[] = new double[x.length];
		for(int i=0; i<x.length;i++) {
			h_all[i] = hypothesis_minus_y(x[i], y[i],theta);
		}
		return h_all;
	}
	
	public double hypothesis_minus_y(double[] x, double y, double[] theta) {
		double h = 0;
		for(int i=0; i<x.length;i++) {
			h += x[i]*theta[i];
		}
		return h-y;
	}
	
	//adds a column of 1's to the beggining of the matrix
	public double[][] insertBias(double[][] x) {
		double[][] x_bias = new double[x.length][x[0].length+1];
		for(int i = 0;i<x_bias.length;i++) {
			for(int j = 0; j<x_bias[0].length;j++) {
				if(j==0) {
					x_bias[i][j] = 0;
				}
				else {
					x_bias[i][j] = x[i][j-1];
				}
			}
		}
		return x_bias;
	}
}
