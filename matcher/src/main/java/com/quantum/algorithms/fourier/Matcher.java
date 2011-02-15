/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
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
	 * rotation angle estimated between candidate image and reference image
	 */
	private float angleCorrection = 0;

	/**
	 * compute cross power Spectrum
	 * 
	 * @param Ga
	 * @param Gb
	 * @return
	 */
	public Complex[][] getCrossPowerSpectrum(final Complex[][] Ga,
			final Complex[][] Gb) {

		int Ga_width = Ga.length;
		int Ga_height = Ga[0].length;
		int Gb_width = Gb.length;
		int Gb_height = Gb[0].length;

		Complex[][] R = new Complex[Ga_width][Gb_height];

		for (int i = 0; i < Ga_width; i++) {
			for (int j = 0; j < Ga_height; j++) {
				Complex tempo = new Complex(0, 0);
				for (int k = 0; k < Gb_width; k++) {
					tempo = tempo.add(Ga[i][k].multiply(Gb[k][j].conjugate()));
				}
				
				if (tempo.abs() > 0) {
					R[i][j] = tempo.multiply(1 / tempo.abs());
				} else {
					R[i][j] = tempo;
				}
			}
		}
		return R;
	}

	/**
	 * main method computing a score (probability that candidate image matches
	 * against reference image
	 * 
	 * @param reference
	 * @param candidate
	 * @return
	 */
	public double match(final Complex[][] reference, final Complex[][] candidate) {

		/**
		 * Step 1: compute FFT
		 */
		Complex[][] Fimage1 = getFFT(reference);
		Complex[][] Fimage2 = getFFT(candidate);

		/**
		 * Step 1.1 : calculate the cross-power spectrum by taking the complex
		 * conjugate of the second result, multiplying the Fourier transforms
		 * together elementwise, and normalizing this product elementwise.
		 */

		Complex[][] crossPowerSpectrum = getCrossPowerSpectrum(Fimage1, Fimage2);

		/**
		 * Step 1.2 Obtain the normalized cross-correlation by applying the
		 * inverse Fourier transform
		 */

		Complex[][] crossCorrelation=getFFTInverse(crossPowerSpectrum);
		
		
		/**
		 * Step 1.3 get peak
		 */
		
		Point2D max = getPeak(crossCorrelation);
		
		double matchingScore = crossCorrelation[(int) max.getX()][(int) max.getY()].getReal();
		
		logger.info(" max found (" + max.getX() +";" + max.getY()+") ="+matchingScore);		
		
		/**
		 * Setp 2: highpass filter
		 */

		/**
		 * Step 3: Log polar conversion
		 */

		/**
		 * Step 4: correlation Phase get angle Rotation between both images get
		 * scale difference between both images
		 */

		/**
		 * Step 5: Transformation
		 */

		/**
		 * Step 6: Translation
		 */
		return (matchingScore * 100.0);
	}

	/**
	 * return x,y as Point2D.Double 
	 * max value
	 * @param data
	 * @return
	 */
	public Point2D getPeak(final Complex[][] data){
		
		int xMax=0;
		int yMax = 0;
		double max = data[xMax][yMax].getReal();
		
		for (int i=0;i<data.length;i++){
			for (int j=0;j<data[0].length;j++) {
				if ( max<data[i][j].getReal()){
					max = data[i][j].getReal();
					xMax=i;
					yMax=j;
				}
			}
		}
		
		Point2D p=new Point2D.Double();
		p.setLocation(xMax, yMax);		
		return p;
	}
	/**
	 * compute matchingScore from convolvedImage
	 * 
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
	 * 
	 * @param data
	 * @return
	 */
	public Complex[][] getFFT(final Complex[][] data) {
		return (Complex[][]) FFT.mdfft(data, true);
	}
	
	/**
	 * return inverse fourier transform
	 * @param data
	 * @return
	 */
	public Complex[][] getFFTInverse(final Complex[][] data){
		return (Complex[][]) FFT.mdfft(data, false);
	}

	/**
	 * complete if necessary an array with 0 to be sure that array size will be
	 * a power of 2
	 * 
	 * @param data
	 * @return new array completed
	 */
	public Complex[][] complete(final Complex[][] data) {
		int width = data.length;
		int height = data[0].length;

		int newWidth = (int) nearestSuperiorPow2(width);
		int newHeight = (int) nearestSuperiorPow2(height);

		Complex[][] newData = new Complex[newWidth][newHeight];
		
		for (int i = 0;i<newWidth;i++){
			for (int j= 0;j<newHeight;j++){
				Complex tempo=new Complex(0,0);
				
				if (i<width && j<height){
					tempo=data[i][j];
				}
				
				newData[i][j] = tempo;
			}
		}
		return newData;
	}

	/**
	 * check if a number is power of 2
	 * 
	 * @param number
	 * @return
	 */
	public boolean isPowerOf2(final long number) {
		return FastFourierTransformer.isPowerOf2(number);
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
