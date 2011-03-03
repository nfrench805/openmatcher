package com.quantum.algorithms.fourier;

import org.apache.commons.math.complex.Complex;

/**
 * propose DFT and IDFT methods
 * 
 * @author Pascal Dergane
 * 
 */
public class FourierTransform {

	/**
	 * singleton constructor
	 */
	private FourierTransform() {
		// define a singleton
	}

	/**
	 * compute DFT of Complex[] array
	 * 
	 * @return DFT Complex[] vector
	 */
	public static Complex[] dft(final Complex[] input) {
		return transform(input, true);
	}

	/**
	 * compute inverse of dft
	 * 
	 * @param input
	 * @return
	 */
	public static Complex[] idft(final Complex[] input) {

		return transform(input, false);
	}

	/**
	 * compute transformed serie
	 * 
	 * @param input
	 * @param forward
	 *            true then Exp(-j*2PI/N*k*n) (typically to compute DFT), false
	 *            to compute IDFT
	 * @return
	 */
	public static Complex[] transform(final Complex[] input,
			final boolean forward) {
		int N = input.length;
		Complex[] output = new Complex[N];

		for (int j = 0; j < N; j++) {
			output[j] = transform(input, forward, j);
		}
		return output;
	}

	/**
	 * compute jth element of transformed(input) according with forward flag
	 * (true for DFT, false for IDFT)
	 * 
	 * @param input
	 * @param forward
	 * @param j
	 * @return
	 */
	public static Complex transform(final Complex[] input,
			final boolean forward, final int j) {
		int N = input.length;
		Complex projection_j = new Complex(0, 0);
		double theta = 2 * Math.PI * j / N;
			
		for (int k = 0; k < N; k++) {			
			//double theta2= 2 * Math.PI * (N-k-1) * j / N;
			//exp(-i*theta2) = exp (+ i *2 * pi * (k+1) * j /N) = W_kj.conjugate * exp (i*2*pi*j/N)			
			Complex w_jk = new Complex(Math.cos(theta*k),
					- Math.sin(theta*k)) ;
			if (!forward){
				w_jk=w_jk.conjugate();
			}
			projection_j = projection_j.add(w_jk.multiply(input[k]));
		}
		if (forward) {
			return projection_j;
		} else {
			return (projection_j.divide(new Complex(N, 0)));
		}
	}
	
	/**
	 * return transformed Matrix (dft if forward is true, inverse dft else)
	 * multiDimensionalComplexMatrix input
	 * 
	 * @param input
	 * @param forward
	 * @return
	 */
	public static Object transformMultiDimensionalComplexMatrix(Object input,boolean forward){
		Object output = null;
		//mdcm =        Complex[10][20]...[100]
		MultiDimensionalComplexMatrix mdcm = new MultiDimensionalComplexMatrix(input);
		//dimensionSize=Integer[10,20,....,100]
		Integer[] dimensionSize = mdcm.getDimensionSizes();
				
        //cycle through each dimension
        for (int i = 0; i < dimensionSize.length; i++) {
            
        }               
		return output;
	}
}
