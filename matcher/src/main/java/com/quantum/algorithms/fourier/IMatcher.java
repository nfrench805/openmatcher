package com.quantum.algorithms.fourier;

import java.util.Map;

import org.apache.commons.math.complex.Complex;

public interface IMatcher {


	/**
	 * compute cross phase normalized
	 * 
	 * @param F
	 * @param G
	 * @return
	 */
	public abstract Complex[][] getCrossPhaseSpectrum(final Complex[][] F,
			final Complex[][] G);

	/**
	 * compute 2D DFT
	 * 
	 * @param f
	 * @return
	 */
	public abstract Complex[][] get2D_DFT(final double[][] f);

	/**
	 * compute Inverse Discrete Fourier
	 * 
	 * @param R
	 * @param N1
	 * @param N2
	 * @return
	 */
	public abstract Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2);
	
	/**
	 * compute DFT of dimension-1 complex vector
	 * @param f
	 * @return
	 */
	public abstract Complex[] getDFT(final Complex[] f);
	
	/**
	 * compute Inverse DFT of dimension-1 complex vector
	 * @param F
	 * @return
	 */
	public abstract Complex[] getIDFT(final Complex[] F);
	
	/**
	 * initialize class
	 * @param N
	 * @return
	 */
	public abstract Map<Integer, Complex> initialize(final int N);

}