package linear_regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.io.*;


/**
 * @author matthew_welty
 * This class has an example linear regression on a set of cubic data
 * it also contains several methods to read in files and get the data into a useful format
 */
public class LinRegressionDriver {
	
	/**
	 * @param filename name of the file to be read in without skipping the first line of the file
	 * @return each line of the file put into the arraylist
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<String> read(String filename) throws FileNotFoundException,IOException{
		return read(filename,false);
	}
	
	/**
	 * @param filename name of the file to be read in
	 * @param skipFirst specify if you want to skip the first line of the file
	 * @return each line of the file put into the arraylist
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<String> read(String filename, boolean skipFirst) throws FileNotFoundException,IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = new ArrayList<String>();
		String line = "";
		if(skipFirst) {
			line=reader.readLine();
		}
		while(line!=null) {
			line = reader.readLine();
			if(line==null) {
				break;
			}
			lines.add(line);
		}
		reader.close();
		return lines;
	}
	
	/**
	 * @param numStrs Array of strings to be split
	 * @param delimeter specifies what character/string to strings in the array will be split by
	 * @return 2d array of double where the strings in the numStr arrayList
	 * have been split according to the delimeter and converted to doubles
	 */
	public static double[][] readStrdouble2D(ArrayList<String> numStrs,String delimeter){
		double[][] nums2D = new double[numStrs.size()][/*(numStrs.get(0).length()+1)/2*/];
		String tempStr = "";
		
		for(int i=0;i<numStrs.size();i++) {
			tempStr = numStrs.get(i);
			String strArray[] = tempStr.split(delimeter);
			
			double nums1D[] = new double[strArray.length];
			for(int j = 0;j<nums1D.length;j++) {
				
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
	/**
	 * @param numStrs Array of strings to be split
	 * @param delimeter specifies what character/string to strings in the array will be split by
	 * @param removeFirst specifies if you will remove the first column from the output
	 * @return 2d array of double where the strings in the numStr arrayList
	 * have been split according to the delimeter and converted to doubles
	 */
	public static double[][] readStrdouble2D(ArrayList<String> numStrs,String delimeter, boolean removeFirst){
		double[][] nums2D = new double[numStrs.size()][/*(numStrs.get(0).length()+1)/2*/];
		String tempStr = "";
		
		for(int i=0;i<numStrs.size();i++) {
			tempStr = numStrs.get(i);
			String strArray[] = tempStr.split(delimeter);
			
			double nums1D[] = new double[strArray.length];
			for(int j = 0;j<nums1D.length;j++) {
				if(removeFirst && j==0) {
					continue;
				}
				
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
	/**
	 * @param filename file to read in data from
	 * @param delimeter specifies how each line is split
	 * @return data from file as 2D double array
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static double[][] read2D(String filename,String delimeter) throws IOException,FileNotFoundException{
		return readStrdouble2D(read(filename),delimeter);
	}

	/**
	 * @param data removes first element from array
	 * @return data without first element
	 */
	public static double[] removeFirst (double[] data) {
		double[] newData = new double[data.length-1];
		for(int i=1; i<data.length;i++) {
			if(i==0) {
				continue;
			}
			newData[i-1] = data[i];
		}
		return newData;
	}
	
	/**
	 * @param data removes first column from 2D array
	 * @return data without first column
	 */
	public static double[][] removeFirst(double[][] data){
		double[][] newData = new double[data.length][];
		for(int i=0;i<data.length;i++) {
			newData[i] = removeFirst(data[i]);
		}
		return newData;
	}
	
	/**
	 * @param filename csv file to be read in
	 * @return csv data converted to 2D double array
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static double[][] readCSV(String filename) throws IOException,FileNotFoundException{
		return removeFirst(readStrdouble2D(read(filename,true),",",true));
	}

	public static void main(String[] args) throws IOException,FileNotFoundException{
		// TODO Auto-generated method stub
		

		String cubic_file = "cubic.csv";//input csv file name
		Update cubicReg = new Update(cubic_file,3);//3 is the degree of polynomial to be fit
		cubicReg.setAlpha(.0001); //fit was diverging with default alpha
		cubicReg.setIterations(10000); //increased iterations to improve fit
		cubicReg.iterateTheta(); //actually calling update method
		
		
		String outData = "cubicReg.txt"; //output file name for the original data set with theta paramaters
		String outPred = "cubicRegPrediction.txt";// output file name for the predicted value of y for all the input data
		cubicReg.writeData(outData); //write the data to file
		cubicReg.writePrediction(outPred);//write the predictions to file
		
		String pythonScript = "./plot.py";//the python executable to be called
		String commandStr = pythonScript+" "+outData + " " +outPred;//command to be called
		Process process = Runtime.getRuntime().exec(commandStr);//running the python executable with correct input file names
	}

}
