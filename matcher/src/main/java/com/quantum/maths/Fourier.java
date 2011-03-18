/**
 * 
 */
package com.quantum.maths;

import org.apache.commons.math.complex.Complex;

/**
 * @author Pascal Dergane
 * 
 */
public class Fourier {

	private static Complex norm2 = new Complex(2, 0);
	private static Complex norm4 = new Complex(4, 0);

	/**
	 * compute Discrete Fourier Transform known as DFT (if forward = true) or
	 * Inverse DFT (if forward = false) of input signal
	 * 
	 * 
	 * @param input
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @return transformed signal
	 */
	public static Complex[] transform(final Complex[] input, boolean forward) {
		return transformRadix2(input, forward);
	}

	/**
	 * compute Discrete Fourier Transform known as DFT (if forward = true) or
	 * Inverse DFT (if forward = false) of input signal implement radix2
	 * 
	 * @param input
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @return transformed signal
	 */
	private static Complex[] transformRadix2(final Complex[] input,
			boolean forward) {

		final int N = input.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = input[0];
			break;
		case 2:
			// N is even, we can use radix-2
			S[0] = forward ? (input[0].add(input[1]))
					: (input[0].add(input[1])).divide(norm2);
			S[1] = forward ? input[0].subtract(input[1]) : input[0].subtract(
					input[1]).divide(norm2);
			break;
		default:

			final int M = N/2;
			final double omega = - Math.PI / M;
			final Complex Wn = forward ? new Complex(0, omega)
					: new Complex(0, -omega);

			if (N % 2 == 0) {
				Complex[] even = new Complex[M];
				Complex[] odd = new Complex[M];
				for (int k = 0; k < input.length / 2; k++) {
					even[k] = input[2 * k];
					odd[k] = input[2 * k + 1];
				}
				even = transformRadix2(even, forward);
				odd = transformRadix2(odd, forward);

				for (int k = 0; k < M; k++) {
					final Complex f2 = odd[k].multiply((Wn
							.multiply(k)).exp());
					S[k] = (forward) ? even[k].add(f2) : (even[k].add(f2))
							.multiply(0.5);
					S[k + M] = forward ? even[k].subtract(f2) : (even[k]
							.subtract(f2)).multiply(0.5);
				}
			}
			break;
		}
		return S;
	}

	/**
	 * compute Discrete Fourier Transform known as DFT (if forward = true) or
	 * Inverse DFT (if forward = false) of input signal implement radix4
	 * 
	 * @param x
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @return transformed signal
	 */
	private static Complex[] transformRadix4(final Complex[] x, boolean forward) {

		final int radixLength = 2;
		final int N = x.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = x[0];
			break;
		case 2:
			S[0] = x[0].add(x[1]);
			S[1] = x[0].subtract(x[1]);
			break;
		default:
			final int M = N / radixLength;
			Complex[][] f = new Complex[radixLength][M];
			Complex[][] F = new Complex[radixLength][M];

			// initialize f by splitting x in radixLength
			for (int k = 0; k < M; k++) {
				for (int i = 0; i < radixLength; i++) {
					f[i][k] = x[radixLength * k + i];
				}
			}

			Complex[] radix = new Complex[radixLength];
			// initialize radix exp(-j p*PI/2*n)
			for (int p = 0; p < radixLength; p++) {
				radix[p] = forward ? (new Complex(0, -p * 2 * Math.PI / N))
						: (new Complex(0, p * 2 * Math.PI / N));
			}

			// compute FFT of f[p]
			for (int p = 0; p < radixLength; p++) {
				F[p] = transformRadix4(f[p], forward);
			}

			for (int n = 0; n < M; n++) {
				for (int l = 0; l < radixLength; l++) {
					final int index = radixLength * n + l;
					S[index] = new Complex(0, 0);
					for (int p = 0; p < radixLength; p++) {
						S[index] = S[index].add(F[p][n].multiply((radix[p]
								.multiply(index)).exp()));
					}
					if (!forward) {
						S[index] = S[index].multiply(1 / radixLength);
					}
				}
			}
			break;
		}

		return S;
	}

	/**
	 * compute Discrete Fourier Transform known as DFT (if forward = true) or
	 * Inverse DFT (if forward = false) of input signal implement Split radix
	 * 
	 * @param input
	 * @param forward
	 * @return
	 */
	private static Complex[] transformSplitRadix(final Complex[] input,
			boolean forward) {

		final int N = input.length;
		Complex[] S = new Complex[N];

		if (N == 1) {
			S[0] = input[0];

		} else if (N == 2) {
			// N is even, we can use radix-2
			S[0] = forward ? (input[0].add(input[1]))
					: (input[0].add(input[1])).divide(norm2);
			S[1] = forward ? input[0].subtract(input[1]) : input[0].subtract(
					input[1]).divide(norm2);
			return S;
		}

		final double omega = -2 * Math.PI / N;
		final Complex Wn = forward ? new Complex(0, omega).exp() : new Complex(
				0, -omega).exp();

		if (N % 2 == 0) {
			Complex[] even = new Complex[N / 2];
			Complex[] odd = new Complex[N / 2];
			for (int k = 0; k < input.length / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}
			even = transformRadix2(even, forward);
			odd = transformRadix4(odd, forward);

			for (int k = 0; k < N / 2; k++) {
				final Complex f2 = odd[k].multiply(Wn.pow(new Complex(k, 0)));
				S[k] = (forward) ? even[k].add(f2) : even[k].add(f2).divide(
						norm2);
				S[k + N / 2] = forward ? even[k].subtract(f2) : even[k]
						.subtract(f2).divide(norm2);
			}
		}
		return S;
	}

}
