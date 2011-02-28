package com.quantum.algorithms.fourier;

import org.apache.commons.math.complex.Complex;

public class CoordinateConverter {
	
/**
 * convert an image from rectangular coordinates
 * into log polar
 * algorithm used
 *  oRows = size(input, 1);
	    oCols = size(input, 2);
	    dTheta = 2*pi / oCols;                 % the step size for theta 
	    b = 10 ^ (log10(oRows) / oRows);       % base for the log-polar conversion 
	    for i = 1:oRows                        % rows     
	        for j = 1:oCols                    % columns
	            r = b ^ i - 1;                 % the log-polar
	            theta = j * dTheta;
	            x = round(r * cos(theta) + size(input,2) / 2);
	            y = round(r * sin(theta) + size(input,1) / 2);
	            if (x>0) & (y>0) & (x<size(input,2)) & (y<size(input,1)) 
	                output(i,j) = input(y,x);
	            end
	        end
	    end
 * @param input
 * @return
 */
	public static double[][] toLogPolar(final double[][] input){

		int rows = input.length;
		int cols = input[0].length;
		
		double[][] output = new double[cols][rows];
		
		double dTheta = 2* Math.PI / cols;
		double base = Math.pow(10, (Math.log10(rows) / rows));
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				double r = Math.pow(base, i) -1;
				double theta = j * dTheta;
				int x = (int) Math.round(r * Math.cos(theta) + cols / 2);
				int y = (int) Math.round(r * Math.sin(theta) + rows / 2);
				if (x>=0 && y>=0 && x<cols && y < rows){
					output[i][j] = input[i][j];
				}				
			}
		}
	   return output;
	}
	
	/**
	 * convert a Complex[][] array (as DFT) from rectangular coordinates
	 * into log polar coordinates
	 * @param input
	 * @return
	 */
	public static Complex[][] toLogPolar(final Complex[][] input){

		int rows = input.length;
		int cols = input[0].length;
		
		Complex[][] output = new Complex[cols][rows];
		
		double dTheta = 2* Math.PI / cols;
		double base = Math.pow(10, (Math.log10(rows) / rows));
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				double r = Math.pow(base, i) -1;
				double theta = j * dTheta;
				int x = (int) Math.round(r * Math.cos(theta) + cols / 2);
				int y = (int) Math.round(r * Math.sin(theta) + rows / 2);
				if (x>=0 && y>=0 && x<cols && y < rows){
					output[i][j] = input[i][j];
				}else{
					output[i][j] = new Complex(0,0);
				}
			}
		}
	   return output;
	}

}
