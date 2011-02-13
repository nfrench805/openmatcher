/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Pascal Dergane An FFT-Based Technique for Translation, Rotation, and
 *         Scale-Invariant Image Registration by B. Srinivasa Reddy and
 *         B.N.Chatterji, 1996
 * 
 */
public class Matcher {
	/**
	 * logger to get more information during execution
	 */
	private Logger logger = Logger.getLogger(Matcher.class.getName());
	/**
	 * pointer on Fouirer Transformer (object allowing to compute FFT, and
	 * reverseFFT
	 */
	private FastFourierTransformer FFT = new FastFourierTransformer();
	/**
	 * rotation angle estimated 
	 * between candidate image and reference image
	 */
	private float angleCorrection = 0; 

	/**
	 * main method computing a score (probability that candidate image matches
	 * against reference image
	 * 
	 * @param reference
	 * @param candidate
	 * @return
	 */
	public int match(final Complex[][] reference, final Complex[][] candidate) {
		
		/**
		 * Step 1: compute FFT
		 */
		Complex[][] Fimage1 = getFFT(reference);
		Complex[][] Fimage2 = getFFT(candidate);
		
		/**
		 * Setp 2: highpass filter
		 */
		
		/**
		 * Step 3: Log polar conversion
		 */
		
		/**
		 * Step 4: correlation Phase
		 * get angle Rotation between both images
		 * get scale difference between both images
		 */
		
		/**
		 * Step 5: Transformation
		 */
		
		/**
		 * Step 6: Translation 
		 */

		int height = Fimage1[0].length;
		int width = Fimage1.length;

		Complex[][] result = new Complex[width][height];

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				result[i][j] = Fimage1[i][j].multiply(Fimage2[i][j]);
			}
		}

		Complex[][] convolvedImage = (Complex[][]) FFT.mdfft(result, false);

		int matchingScore = getMatchingScore(convolvedImage);
		return matchingScore;
	}

	/**
	 * compute matchingScore from convolvedImage
	 * @param matchingScore
	 * @param convolvedImage
	 * @return
	 */
	public int getMatchingScore(final Complex[][] convolvedImage) {
		int matchingScore = 0;
		// FIXME: method used to compute score does not reflect reality
		// we expect a great score when HIT, and 0 if no HIT
		// same thing with rotation
		for (int j = 0; j < convolvedImage[0].length; j++) {
			for (int i = 0; i < convolvedImage.length; i++) {
				matchingScore += convolvedImage[i][j].getReal();
			}
		}
		return matchingScore;
	}
	
	/**
	 * return FFT of data
	 * @param data
	 * @return
	 */
	public Complex[][] getFFT(final Complex[][] data){
		return (Complex[][]) FFT.mdfft(data, true);
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

		if (!isPowerOf2(width)) {

		}

		if (!isPowerOf2(height)) {

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
	 * 
	 * @param i
	 * @return
	 */
	public long nearestSuperiorPow2(final long i) {
		long x = i > 0 ? ((i - 1) & i) : 1;

		return (!isPowerOf2(x)) ? nearestSuperiorPow2(x) : x << 1;
	}
}
