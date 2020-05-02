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
	
	public static Double[][] readStrDouble2D(ArrayList<String> numStrs,String delimeter){
		Double[][] nums2D = new Double[numStrs.size()][/*(numStrs.get(0).length()+1)/2*/];
		String tempStr = "";
		
		for(int i=0;i<numStrs.size();i++) {
			tempStr = numStrs.get(i);
			String strArray[] = tempStr.split(delimeter);
			Double nums1D[] = new Double[strArray.length];
			for(int j = 0;j<nums1D.length;j++) {
				nums1D[j] = Double.valueOf(strArray[j]);
			}
			nums2D[i] = nums1D;
		}
		
		return nums2D;
	}
	
	public static Double[][] read2D(String filename,String delimeter) throws IOException,FileNotFoundException{
		return readStrDouble2D(read(filename),delimeter);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String data_file = "ex1data.txt";
		
		
		
		
	}

}
