/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author pascal dergane
 * 
 */
public class GenericMatcher<T>  {
	
	private T matcherUnit;
	
	public GenericMatcher(){
		//default constructor		
	}
	
				
	/**
	 * compute DFT of real array (dimension-2)
	 * @param f
	 * @return
	 */
	public Complex[][] get2D_DFT(final double[][] f) {		
		return ((IMatcher) getMatcherUnit()).get2D_DFT(f);
	}
	
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2){
		return ((IMatcher) getMatcherUnit()).get2D_IDFT(R,N1,N2);
	}
	
	/**
	 * initialize class
	 * @param N
	 * @return
	 */
	public Map<Integer, Complex> initialize(final int N) {
		return ((IMatcher) getMatcherUnit()).initialize(N);
	}
	
	/**
	 * logger to get more information during execution
	 */
	protected Logger logger = Logger.getLogger(GenericMatcher.class.getName());

	
	
	/**
	 * compute cross phase Spectrum as F* conjugate(G) / abs(F*conjugate(G))
	 * @param F
	 * @param G
	 * @return
	 */
	public Complex[][] getCrossPhaseSpectrum(final Complex[][] F,
			final Complex[][] G) {
		int N1 = F.length;
		int N2 = F[0].length;

		Complex[][] R = new Complex[N1][N2];

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				Complex tempo = F[k1][k2].multiply(G[k1][k2].conjugate());
				R[k1][k2] = (tempo.abs() > 0) ? tempo
						.multiply(1L / tempo.abs()) : tempo;
			}
		}
		return R;
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

	/**
	 * @param matcherUnit the matcherUnit to set
	 */
	public void setMatcherUnit(final T matcherUnit) {
		this.matcherUnit = matcherUnit;
	}

	/**
	 * @return the matcherUnit
	 */
	public T getMatcherUnit() {
		return matcherUnit;
	}

}
