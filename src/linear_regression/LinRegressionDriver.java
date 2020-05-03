package linear_regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.io.*;


public class LinRegressionDriver {
	
	public static ArrayList<String> read(String filename) throws FileNotFoundException,IOException{
		return read(filename,false);
	}
	
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
	
	public static double[][] readStrdouble2D(ArrayList<String> numStrs,String delimeter){
		double[][] nums2D = new double[numStrs.size()][/*(numStrs.get(0).length()+1)/2*/];
		String tempStr = "";
		
		for(int i=0;i<numStrs.size();i++) {
			tempStr = numStrs.get(i);
			String strArray[] = tempStr.split(delimeter);
			
			double nums1D[] = new double[strArray.length];
			for(int j = 0;j<nums1D.length;j++) {
				System.out.println(strArray[j]);
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
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
				System.out.println(strArray[j]);
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
	public static double[][] read2D(String filename,String delimeter) throws IOException,FileNotFoundException{
		return readStrdouble2D(read(filename),delimeter);
	}

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
	
	public static double[][] removeFirst(double[][] data){
		double[][] newData = new double[data.length][];
		for(int i=0;i<data.length;i++) {
			newData[i] = removeFirst(data[i]);
		}
		return newData;
	}
	
	public static double[][] readCSV(String filename) throws IOException,FileNotFoundException{
		return removeFirst(readStrdouble2D(read(filename,true),",",true));
	}

	public static void main(String[] args) throws IOException,FileNotFoundException{
		// TODO Auto-generated method stub
		String path = "";
		String data_file = "ex1data1.txt";
		String data_loc = path+data_file;
		double[][] data = read2D(data_loc,",");
		
		for(double[] d1: data) {
			System.out.println("hi");
			for(double d2: d1) {
				System.out.print(d2+",");
				
			}
		}
		System.out.println("ping");
		System.out.println(data[0][0]+","+data[0][1]);
		System.out.println(data[data.length-1][0]);
		
		Update linReg = new Update(data);
		System.out.println(linReg.xStr());
		System.out.println(linReg.yStr());
		System.out.println(linReg.cost());
		linReg.iterateTheta();
		System.out.println(linReg.costHistoryStr());
		System.out.println("ping");
		System.out.println(linReg.thetaStr());
		
		String cubic_file = "cubic.csv";
		double[][] cubicData = readCSV(cubic_file);
		System.out.println(Update.print2D(cubicData));
		double[][] xTerms = Update.splitX(cubicData);
		System.out.println(Update.print2D(xTerms));
		
		double[] yTerms = Update.splitY(cubicData);
		System.out.println(Update.print1D(yTerms));
		double[][] polyTerms = Update.model(xTerms,3);
		Update cubicReg = new Update(polyTerms,yTerms);
		cubicReg.setAlpha(.0001);
		cubicReg.setIterations(10000);
		cubicReg.iterateTheta();
		System.out.println("ping");
		System.out.println(cubicReg.thetaStr());
		System.out.println(cubicReg.costHistoryStr());
		
		linReg.writeData("linReg.txt");
		linReg.writePrediction("linRegPrediction.txt");
		
		String outData = "cubicReg.txt";
		String outPred = "cubicRegPrediction.txt";
		cubicReg.writeData(outData);
		cubicReg.writePrediction(outPred);
		System.out.println(Update.print1D(yTerms));
		String pythonScript = "./plot.py";
		String commandStr = pythonScript+" "+outData + " " +outPred;
		Process process = Runtime.getRuntime().exec(commandStr);
		
		//ProcessBuilder py = new ProcessBuilder("touch test_touch.txt");
		//Process p = py.start();
		System.out.println(commandStr);
	}

}
