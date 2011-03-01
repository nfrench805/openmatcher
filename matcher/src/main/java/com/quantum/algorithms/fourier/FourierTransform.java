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

		for (int k = 0; k < N; k++) {
			double theta = 2 * Math.PI * k * j / N;
			Complex w_jk = new Complex(Math.cos(theta),
					- Math.sin(theta)) ;
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
}