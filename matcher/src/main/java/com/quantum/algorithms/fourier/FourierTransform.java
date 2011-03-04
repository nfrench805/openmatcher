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
		double theta = 2 * Math.PI * j / N;

		if (N % 2 == 0) {
			Complex[] even = new Complex[N / 2];
			Complex[] odd = new Complex[N / 2];

			for (int k = 0; k < N / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}
			Complex twiddle_factor = new Complex(Math.cos(theta),
					-Math.sin(theta));
			if (!forward) {
				twiddle_factor = twiddle_factor.conjugate();
			}
			Complex transformed= (transform(even, forward, j).add(transform(odd, forward, j)
					.multiply(twiddle_factor)));
			
			if (forward) {
				return transformed;
			} else {
				return (transformed.divide(new Complex(2, 0)));
			}			
		}

		Complex projection_j = input[0];
		for (int k = 1; k < N; k++) {

			// double theta2= 2 * Math.PI * (N-k-1) * j / N;
			// exp(-i*theta2) = exp (+ i *2 * pi * (k+1) * j /N) =
			// W_kj.conjugate * exp (i*2*pi*j/N)
			Complex twiddle_factor = new Complex(Math.cos(theta * k),
					-Math.sin(theta * k));
			if (!forward) {
				twiddle_factor = twiddle_factor.conjugate();
			}
			projection_j = projection_j.add(twiddle_factor.multiply(input[k]));
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
	public static Object transformMultiDimensionalComplexMatrix(Object input,
			boolean forward) {
		// mdcm = Complex[10][20]...[100]
		MultiDimensionalComplexMatrix output = (MultiDimensionalComplexMatrix) (new MultiDimensionalComplexMatrix(
				input));
		// dimensionSize=Integer[10,20,....,100]
		Integer[] dimensionSize = (Integer[]) output.getDimensionSizes();

		// cycle through each dimension
		for (int d = 0; d < dimensionSize.length; d++) {
			output = transform(output, forward, d, new int[0]);
		}
		return output.getArray();
	}

	/**
	 * compute DFT along one dimension
	 * 
	 * @param input
	 * @param forward
	 * @param dimension
	 * @param subVector
	 * @return
	 */
	public static MultiDimensionalComplexMatrix transform(
			MultiDimensionalComplexMatrix input, final boolean forward,
			final int dimension, int[] subVector) {

		Integer[] dimensionSize = (Integer[]) input.getDimensionSizes();

		if (subVector.length == dimensionSize.length) {
			// temp will store intermediate DFT (or IDFT) 1-dimension
			// along dimension 'dimension'
			// firstly, initialize temp with data from input
			Complex[] temp = new Complex[dimensionSize[dimension]];
			for (int i = 0; i < dimensionSize[dimension]; i++) {
				subVector[dimension] = i;
				temp[i] = input.getElement(subVector);
			}
			// secondly, call transform (1-dimension)
			temp = transform(temp, forward);

			// finally, recopy result into multi dimensional array
			for (int i = 0; i < dimensionSize[dimension]; i++) {
				subVector[dimension] = i;
				input.set(temp[i], subVector);
			}
		} else {
			int[] vector = new int[subVector.length + 1];
			System.arraycopy(subVector, 0, vector, 0, subVector.length);
			if (subVector.length == dimension) {
				// value is not important once the recursion is done.
				// then an dft will be applied along the dimension d.
				vector[dimension] = 0;
				input = transform(input, forward, dimension, vector);
			} else {
				for (int i = 0; i < dimensionSize[subVector.length]; i++) {
					vector[subVector.length] = i;
					// further split along the next dimension
					input = transform(input, forward, dimension, vector);
				}
			}
		}
		// and return input transformed along dimension
		return input;
	}
}
