

package linear_regression;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author matthew_welty
 * This class allows you to create an Update object which you can then call to do 
 * iterative updates to your parameters which will begin to give you a fit to the data
 */



/**
 * @author matthew_welty
 *
 */
public class Update {
	//cost function for all training examples
	/**
	 * each row of x corresponds to 1 training example, so you can have any number of 
	 * parameters that you want
	 */
	double[][] x;
	
	/**
	 * each element of y corresponds to 1 training example
	 */
	double[] y;
	/**
	 *  theta is the array of the fitting paramaters
	 */
	double[] theta;
	/**
	 * alpha is the step size of iterations
	 */
	double alpha=.01;
	/**
	 * x_bias is the same as the x matrix but with a column of 1's prepended to x,
	 * so that we can have an intercept term in our fit
	 */
	double[][] x_bias;
	/**
	 * m is the number of training examples = y.length and x.length
	 */
	double m;
	/**
	 * number of updates to theta
	 */
	int iterations = 1500;
	/**
	 * costHistory contains the costFunction history as a function of iteration#, if the 
	 * fit is going well the cost should decrease with every iteration, if the cost is increasing
	 * the fit may begin to diverge
	 */
	ArrayList<Double> costHistory = new ArrayList<>();
	
	//filename corresponds to a CSV file in the format output by R, 
	//the first row is column headers, the first column is row indices
	/**
	 * @param filename csv filename, format for the csv should be in the format output by R,
	 * the first row is column headers, the first column is row indices
	 * @param n the degree of the polynomial fit
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Update(String filename,int n) throws IOException,FileNotFoundException{
		this(LinRegressionDriver.readCSV(filename),n);
	}
	
	//creates Update object for data and fitting as a n degree polynomial
	/**
	 * @param data 2D array where the last column is the y values
	 * @param n the degree of the polynomial fit
	 */
	public Update(double[][] data, int n) {
		double[][] xTerms = Update.splitX(data);
		double[] yTerms = Update.splitY(data);
		double[][] polyTerms = Update.model(xTerms, n);
		this.x = polyTerms;
		this.y = yTerms;
		this.x_bias = insertBias(this.x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
		
	}
	
	//each row of x should be a training example
	/**
	 * @param x data points which will be fit to y
	 * @param y values to be fit to
	 */
	public Update(double[][] x, double[] y) {
		this.x = x;
		this.y = y;
		this.x_bias = insertBias(x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
	}
	
	
	/**
	 * @param data 2D array where the last column is the y values
	 */
	public Update(double[][] data) {
		assignXY(data);
		this.x_bias = insertBias(x);
		this.theta = new double[x_bias[0].length];
		this.m = x.length;
	}
	
	/**
	 * @param alpha this determines the step size of the updates to the thetas,
	 * if the fit diverges decrease alpha
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	
	/**
	 * @param iter sets the iteration number for the object
	 */
	public void setIterations(int iter) {
		this.iterations = iter;
	}
	
	/**
	 * @param iter the number of updates to perform on the thetas,
	 * if the fit is converging but doesn't appear to have reached a minimum, increase iter
	 */
	public void iterateTheta(int iter) {
		for(int i=0;i<iter;i++) {
			updateThetas();
			//System.out.println(i);
			cost();
		}
	}
	
	
	/**
	 * performs the default number of iterations
	 */
	public void iterateTheta() {
		iterateTheta(iterations);
	}
	
	//Assign y to the last column of the data and x as the rest
	/**
	 * @param data assigns the last column to Y and the rest as X
	 */
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
	
	
	/**
	 * @param data input data containing both x and y
	 * @return returns data containing only x values
	 */
	public static double[][] splitX(double[][] data){
		double[][] trans = Update.transpose(data);
		//this.y = trans[trans.length-1];
		double[][] trans_noY = new double[trans.length-1][trans[0].length];
		for(int i=0;i<trans.length;i++) {
			if(i==trans.length-1) {
				continue;
			}
			trans_noY[i] = trans[i];
		}
		return Update.transpose(trans_noY);
	}
	
	/**
	 * @param data input data containing both x and y
	 * @return returns data containing only y values
	 */
	public static double[] splitY(double[][] data){
		double[][] trans = Update.transpose(data);		
		return trans[trans.length-1];
	}
	
	/**
	 * @return cost calculated for current set of x,y,theta values
	 * updates costHistory
	 */
	public double cost() {
		double cost = cost(x_bias,y,theta);
		costHistory.add(cost);
		return cost;
	}
	
	//
	/**
	 * @param x values with bias term
	 * @param y values 
	 * @param theta parameters being fit
	 * @return the cost of the fit with current set of thetas
	 */
	public double cost(double[][] x, double[] y, double[] theta) {
		double sum = 0;
		double[] inner = hypothesisAll_minus_y(x, y, theta);
		for(double i: inner) {
			sum+= Math.pow(i, 2);
		}
		return sum/(2*m);
	}
	
	/**
	 * @param x values with bias term
	 * @param y values
	 * @param theta values to be updated
	 */
	public void updateThetas(double[][] x, double[] y, double[] theta) {
		double[] h_minus_y = hypothesisAll_minus_y(x, y, theta);
		double[] updatedTheta = new double[theta.length];
		
		for(int j=0; j<theta.length;j++) {
			updatedTheta[j] = theta[j] - alpha/m*sum1D(updateThetaInner(h_minus_y,x,j));
		}
		
		this.theta = updatedTheta;	
	}
	
	/**
	 * updates current set of thetas based on current state of the system
	 */
	public void updateThetas() {
		updateThetas(x_bias, y, theta);
	}
	
	
	/**
	 * @param h_minus_y hypothesis function - y value for each training example
	 * @param x x_bias value in usual format
	 * @param thetaIndex the index of the theta being updated
	 * @return the gradient of our theta at theta index
	 */
	public double[] updateThetaInner(double[] h_minus_y, double[][] x, int thetaIndex) {
		double[] inner = new double[h_minus_y.length];
		for(int i=0;i<h_minus_y.length;i++) {
			inner[i] = h_minus_y[i]*x[i][thetaIndex];
		}
		return inner;
	}
	
	/**
	 * @param data 
	 * @return the sum of data
	 */
	private double sum1D(double[] data) {
		double sum = 0;
		for(double d: data) {
			sum+=d;
		}
		return sum;
	}
	
	
	/**
	 * @param x the x_bias value
	 * @param y the usual y values
	 * @param theta fitting parameters
	 * @return the hypothesis value - y for all training examples
	 */
	public double[] hypothesisAll_minus_y(double[][] x, double[] y, double[] theta) {
		double h_all[] = new double[x.length];
		for(int i=0; i<x.length;i++) {
			h_all[i] = hypothesis_minus_y(x[i], y[i],theta);
		}
		return h_all;
	}
	
	/**
	 * @param x x_bias for single example
	 * @param y for single example
	 * @param theta fitting parameters
	 * @return the hypothesis value - y for a single training example
	 */
	public double hypothesis_minus_y(double[] x, double y, double[] theta) {
		double h = 0;
		for(int i=0; i<x.length;i++) {
			h += x[i]*theta[i];
		}
		return h-y;
	}
	
	/**
	 * @param x single example to predict the value of 
	 * @param theta fitted parameters
	 * @return the predicted value of x using the fit
	 */
	public double predict(double[] x, double[] theta) {
		double h = 0;
		for(int i=0; i<x.length;i++) {
			h += x[i]*theta[i];
		}
		return h;
	}
	
	
	
	/**
	 * @param x_bias x values for a single example with bais element
	 * @return predicted value for a single example
	 */
	public double predict(double[] x_bias) {
		return predict(x_bias,theta);
	}
	
	//x_bias
	/**
	 * @param x_bias each row is a training example to calculate predicted value
	 * @return predicted value for each example in x_bias
	 */
	public double[] predict(double[][] x_bias) {
		double[] h = new double[x_bias.length];
		for(int i=0; i<h.length;i++) {
			h[i] = predict(x_bias[i]);
		}
		return h;
		
	}
	//calculates the values of the model between max and min for polynomial of degree n
	/**
	 * @param max value of range of values to be predicted
	 * @param min value of range of values to be predicted
	 * @param n degree of polynomial
	 * @param numPoints number of points to predict between max and min
	 * @return predicted value of points between max and min
	 */
	public double[] predictPoly(double max, double min, int n, int numPoints) {
		double[] range = new double[numPoints];
		for(int i=0; i<numPoints;i++) {
			range[i] = min + (i*(max-min))/(double)numPoints;
		}
		double[][] poly = model(range, numPoints);
		return predict(insertBias(poly));
		
	}
	
	//adds a column of 1's to the beggining of the matrix
	/**
	 * @param x arbitrary 2D matrix
	 * @return original matrix with column of 1's prepended
	 */
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
	/**
	 * @param data all the matrix rows should be the same size and same for columns
	 * @return transpose of the matrix
	 */
	public static double[][] transpose(double[][] data){
		double[][] newData = new double[data[0].length][data.length];
		for (int i=0;i<data.length;i++) {
			for(int j =0;j<data[0].length;j++) {
				newData[j][i] = data[i][j];
			}
		}
		return newData;
	}
	
	/**
	 * @param data double values to be printed
	 * @param delimeter string to be interjected between double values
	 * @return
	 */
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
	
	/**
	 * @param data values to be converted to a string
	 * @param delimeter1 each row is separated by this
	 * @param delimeter2 each row element is separated by this
	 * @return
	 */
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
	
	/**
	 * @param data values to be converted to a string
	 * @return each datum separated by a newline
	 */
	public static String print1D(double[] data) {
		return print1D(data,"\n");
	}
	
	/**
	 * @param data values to be converted to a string
	 * @return each element in a row is separated by a comma, each row by a newline
	 */
	public static String print2D(double[][] data) {
		return print2D(data,"\n",",");
	}
	
	/**
	 * @return x values with bias as a string
	 */
	public String xStr() {
		return print2D(x_bias);
	}
	
	/**
	 * @return y values as a string
	 */
	public String yStr() {
		return print1D(y);
	}
	
	/**
	 * @return theta parameters as a string
	 */
	public String thetaStr() {
		return print1D(theta,",");
	}
	
	/**
	 * @param x arbitrary 2D array with x.length = y.length
	 * @param y array to be appended to x
	 * @return x appended with y
	 */
	public double[][] append2D(double[][] x, double[] y){
		double[][] all = new double[x.length][];
		for(int i=0;i<x.length;i++) {
			double[] current = new double[x[i].length+1];
			for(int j=0;j<current.length;j++) {
				if(j==current.length-1) {
					current[j] = y[i];
				}
				else {
					current[j] = x[i][j];
				}
			}
			all[i] = current;
		}
		return all;
	}
	
	/**
	 * @param <type> dataType of the list
	 * @param data to be converted into a string
	 * @param delimeter value separating each datum
	 * @return
	 */
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
	
	/**
	 * @return String version of costHistory
	 */
	public String costHistoryStr() {
		return Update.printList(costHistory, "\n");
	}
	
	//model the data as polynomial of degree n
	/**
	 * @param data datum to be raised to ith power where i is in the range (1,n)
	 * @param n degree of polynomial
	 * @return data raised to ith power where i is in the range (1,n)
	 */
	public static double[] model(double data,int n) {
		if(n<=0) {
			System.out.println("cannot accept value of n <= 0");
			return new double[0];
		}
		else {
			double[] poly = new double[n];
			for(int i=0;i<n;i++) {
				poly[i] = Math.pow(data, i+1);
			}
			return poly;
		}
	}
	
	/**
	 * @param data each element is for a different training example
	 * @param n polynomial power to fit to
	 * @return 2D array with additional polynomial features added in
	 */
	public static double[][] model(double[] data, int n){
		double[][] poly = new double[data.length][];
		for(int i =0; i<data.length;i++) {
			poly[i] = model(data[i],n);
		}
		return poly;
	}
	
	/**
	 * @param data 2D array, 1D array nested inside 2D array
	 * @param n polynomial to fit
	 * @return elements in data have been raised to the power i up to n to create extra 
	 * fitting features
	 */
	public static double[][] model(double[][] data, int n){
		return model(transpose(data)[0],n);
	}
	
	/**
	 * @param filename output file name to put original data and thetas
	 * @throws IOException
	 */
	public void writeData(String filename) throws IOException{
		//write x, y, theta on top
		String thetaStr = thetaStr();
		String all = print2D(append2D(x, y));
		String master = thetaStr+"\n"+all;
		save(filename,master);
	}
	
	/**
	 * @param filename prediction output file name
	 * @throws IOException
	 * writes predicted output for all input data to a file
	 */
	public void writePrediction(String filename) throws IOException{
		String thetaStr = thetaStr();
		double[] y_out = predict(x_bias);
		String all = print2D(append2D(x,y_out));
		String master = thetaStr+"\n"+all;
		save(filename,master);
	}
	
	/**
	 * @param filename to write output to
	 * @param str everything to be written to the file as a single string
	 * @throws IOException
	 */
	public static void save(String filename,String str) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write(str);
		writer.close();
	}

}
