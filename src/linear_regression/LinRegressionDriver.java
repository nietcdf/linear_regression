package linear_regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;


public class LinRegressionDriver {
	
	public static ArrayList<String> read(String filename) throws FileNotFoundException,IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = new ArrayList<String>();
		String line = "";
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
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
	public static double[][] read2D(String filename,String delimeter) throws IOException,FileNotFoundException{
		return readStrdouble2D(read(filename),delimeter);
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
		
	}

}
