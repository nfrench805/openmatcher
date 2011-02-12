/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Pascal Dergane
 * 
 */
public class Matcher {
	private FastFourierTransformer FFT = new FastFourierTransformer();
	private Logger logger = Logger.getLogger(Matcher.class.getName());

	public int match(final Complex[][] image1, final Complex[][] image2) {
		int matchingScore = 0;

		Complex[][] Fimage1 = (Complex[][]) FFT.mdfft(image1, true);
		
		Complex[][] Fimage2 = (Complex[][]) FFT.mdfft(image2, true);
		
		int height = Fimage1[0].length;
		int width = Fimage1.length;
		
		Complex[][] result = new Complex[width][height];
		
		for(int j=0;j<height;j++){
			for (int i = 0;i<width;i++){
				result[i][j]= Fimage1[i][j].multiply(Fimage2[i][j]);
			}		
		}
		
		Complex[][] convolvedImage =(Complex[][]) FFT.mdfft(result, false);

		//FIXME: method used to compute score does not reflect reality
		//we expect a great score when HIT, and 0 if no HIT
		//same thing with rotation
		for(int j=0;j<convolvedImage[0].length;j++){
			for (int i = 0;i<convolvedImage.length;i++){
				matchingScore+=convolvedImage[i][j].getReal();
			}		
		}
		return matchingScore;
	}

	/**
	 * complete if necessary an array with 0 to be sure that array size will be
	 * a power of 2
	 * 
	 * @param data
	 * @return new array completed
	 */
	public Complex[][] complete(Complex[][] data) {
		int width = data.length;
		int height = data[0].length;
		
		if (!isPowerOf2(width)){
			
		}
		
		if (!isPowerOf2(height)){
			
		}
		
		return null;
	}

	/**
	 * check if a number is power of 2
	 * 
	 * @param number
	 * @return
	 */
	public boolean isPowerOf2(final long number) {
		return ((number > 0) && (number & (number - 1)) == 0);
	}
	
	/**
	 * return nearest power of 2
	 * @param i
	 * @return
	 */
	public long  nearestSuperiorPow2(final long i)
	{			
		long x = i>0 ?((i - 1) & i):1;

		   return (!isPowerOf2(x)) ? nearestSuperiorPow2(x) : x << 1;
		}
}
