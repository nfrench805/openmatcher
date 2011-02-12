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
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				logger.info("[" + i + "][" + j + "]=" + Fimage1[i][j].getReal()
						+ " +i* " + Fimage1[i][j].getImaginary());
			}
		}
		Complex[][] Fimage2 = (Complex[][]) FFT.mdfft(image2, true);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				logger.info("[" + i + "][" + j + "]=" + Fimage2[i][j].getReal()
						+ " +i* " + Fimage2[i][j].getImaginary());
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
}
