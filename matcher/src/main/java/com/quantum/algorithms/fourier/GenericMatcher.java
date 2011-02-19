/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Najoua Dergane
 * 
 */
public class GenericMatcher implements IMatcher {
	
	/**
	 * logger to get more information during execution
	 */
	protected Logger logger = Logger.getLogger(GenericMatcher.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.quantum.algorithms.fourier.IMatcher#getCrossPowerSpectrum(org.apache
	 * .commons.math.complex.Complex[][],
	 * org.apache.commons.math.complex.Complex[][])
	 */
	public Complex[][] getCrossPowerSpectrum(Complex[][] Ga, Complex[][] Gb) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.quantum.algorithms.fourier.IMatcher#match(double[][],
	 * double[][])
	 */
	public double match(double[][] reference, double[][] candidate) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.quantum.algorithms.fourier.IMatcher#getCrossPhaseSpectrum(org.apache
	 * .commons.math.complex.Complex[][],
	 * org.apache.commons.math.complex.Complex[][])
	 */
	public Complex[][] getCrossPhaseSpectrum(Complex[][] F, Complex[][] G) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.quantum.algorithms.fourier.IMatcher#get2D_DFT(double[][])
	 */
	public Complex[][] get2D_DFT(double[][] f) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.quantum.algorithms.fourier.IMatcher#get2D_IDFT(org.apache.commons
	 * .math.complex.Complex[][], int, int)
	 */
	public Complex[][] get2D_IDFT(Complex[][] R, int N1, int N2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.quantum.algorithms.fourier.IMatcher#getPeak(org.apache.commons.math
	 * .complex.Complex[][])
	 */
	public Point2D getPeak(final Complex[][] data) {

		int xMax = 0;
		int yMax = 0;
		double max = data[xMax][yMax].getReal();
		double average = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				average += data[i][j].getReal();
				if (max < data[i][j].getReal()) {
					max = data[i][j].getReal();
					xMax = i;
					yMax = j;
				}
			}
		}

		Point2D p = new Point2D.Double();
		logger.info("Maximum found :" + max + " [" + xMax + "][" + yMax + "]");
		logger.info("average =" + average / data.length / data[0].length);
		p.setLocation(xMax, yMax);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.quantum.algorithms.fourier.IMatcher#isPowerOf2(long)
	 */
	public boolean isPowerOf2(final long number) {
		return FastFourierTransformer.isPowerOf2(number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.quantum.algorithms.fourier.IMatcher#nearestSuperiorPow2(long)
	 */
	public long nearestSuperiorPow2(final long i) {
		long x = i > 0 ? ((i - 1) & i) : 1;
		return (!isPowerOf2(x)) ? nearestSuperiorPow2(x) : x << 1;
	}

}
