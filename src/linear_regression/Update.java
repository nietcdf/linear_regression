package linear_regression;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Update {
	//cost function for all training examples
	double[][] x;
	double[] y;
	double[] theta;
	double alpha=.01;
	double[][] x_bias;
	double m;
	int iterations = 1500;
	ArrayList<Double> costHistory = new ArrayList<>();
	
	
	//each row of x should be a training example
	public Update(double[][] x, double[] y) {
		this.x = x;
		this.y = y;
		this.x_bias = insertBias(x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
	}
	
	public Update(double[][] data) {
		assignXY(data);
		this.x_bias = insertBias(x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public void setIterations(int iter) {
		this.iterations = iter;
	}
	
	public void iterateTheta(int iter) {
		for(int i=0;i<iter;i++) {
			updateThetas();
			//System.out.println(i);
			cost();
		}
	}
	
	public void iterateTheta() {
		iterateTheta(iterations);
	}
	
	//Assign y to the last column of the data and x as the rest
	private void assignXY(double[][] data) {
		double[][] trans = Update.transpose(data);
		this.y = trans[trans.length-1];
		double[][] trans_noY = new double[trans.length-1][trans[0].length];
		for(int i=0;i<trans.length;i++) {
			if(i==trans.length-1) {
				continue;
			}
			trans_noY[i] = trans[i];
		}
		this.x = Update.transpose(trans_noY);
	}
	
	public double cost() {
		double cost = cost(x_bias,y,theta);
		costHistory.add(cost);
		return cost;
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
	
	public void updateThetas(double[][] x, double[] y, double[] theta) {
		double[] h_minus_y = hypothesisAll_minus_y(x, y, theta);
		double[] updatedTheta = new double[theta.length];
		
		for(int j=0; j<theta.length;j++) {
			updatedTheta[j] = theta[j] - alpha/m*sum1D(updateThetaInner(h_minus_y,x,j));
		}
		
		this.theta = updatedTheta;	
	}
	
	public void updateThetas() {
		updateThetas(x_bias, y, theta);
	}
	
	//used x_bias
	public double[] updateThetaInner(double[] h_minus_y, double[][] x, int thetaIndex) {
		double[] inner = new double[h_minus_y.length];
		for(int i=0;i<h_minus_y.length;i++) {
			inner[i] = h_minus_y[i]*x[i][thetaIndex];
		}
		return inner;
	}
	
	private double sum1D(double[] data) {
		double sum = 0;
		for(double d: data) {
			sum+=d;
		}
		return sum;
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
					x_bias[i][j] = 1;
				}
				else {
					x_bias[i][j] = x[i][j-1];
				}
			}
		}
		return x_bias;
	}
	
	//returns transpose of 2D double array
	public static double[][] transpose(double[][] data){
		double[][] newData = new double[data[0].length][data.length];
		for (int i=0;i<data.length;i++) {
			for(int j =0;j<data[0].length;j++) {
				newData[j][i] = data[i][j];
			}
		}
		return newData;
	}
	
	public static String print1D(double[] data,String delimeter) {
		String master = "";	
		for(int i =0; i<data.length; i++) {
			double d=data[i];
			if(i!=data.length-1) {
				master+=Double.toString(d)+delimeter;
			}
			else {
				master+=Double.toString(d);
			}
		}
		return master;
	}
	
	public static String print2D(double[][] data, String delimeter1,String delimeter2) {
		String master = "";
		for(int i =0; i<data.length; i++) {
			double[] d=data[i];
			if(i!=data.length-1) {
				master+=print1D(d,delimeter2)+delimeter1;
			}
			else {
				master+=print1D(d,delimeter2);
			}
		}
		return master;
	}
	
	public static String print1D(double[] data) {
		return print1D(data,"\n");
	}
	
	public static String print2D(double[][] data) {
		return print2D(data,"\n",",");
	}
	
	public String xStr() {
		return print2D(x_bias);
	}
	
	public String yStr() {
		return print1D(y);
	}
	
	public String thetaStr() {
		return print1D(theta);
	}
	
	public static <type> String printList(List<type> data, String delimeter){
		String master = "";	
		for(int i =0; i<data.size(); i++) {
			type d=data.get(i);
			if(i!=data.size()-1) {
				master+=d.toString()+delimeter;
			}
			else {
				master+=d.toString();
			}
		}
		return master;
	}
	
	public String costHistoryStr() {
		return Update.printList(costHistory, "\n");
	}

}
