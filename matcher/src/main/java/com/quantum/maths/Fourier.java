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
	private static Complex[][] root;

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
		//root = initializeRoot(4, forward);
		//return forward?transformRadix4(input):inverseTransformRadix4(input);
		 return forward ? transformRadix2(input) :
		 inverseTransformRadix2(input);
	}

	/**
	 * compute Discrete Fourier Transform known as DFT of input signal implement
	 * radix2
	 * 
	 * @param input
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @return transformed signal
	 */
	public static Complex[] transformRadix2(final Complex[] input) {

		final int N = input.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = input[0];
			break;
		case 2:
			// N is even, we can use radix-2
			S[0] = (input[0].add(input[1]));
			S[1] = input[0].subtract(input[1]);
			break;
		default:

			final int M = N / 2;
			final double omega = -Math.PI / M;
			final Complex Wn = new Complex(0, omega);
			Complex[] even = new Complex[M];
			Complex[] odd = new Complex[M];
			for (int k = 0; k < input.length / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}
			even = transformRadix2(even);
			odd = transformRadix2(odd);

			for (int k = 0; k < M; k++) {
				final Complex f2 = odd[k].multiply((Wn.multiply(k)).exp());
				S[k] = even[k].add(f2);
				S[k + M] = even[k].subtract(f2);
			}
			break;
		}
		return S;
	}

	/**
	 * compute Inverse Discrete Fourier Transform known as IDFT of input signal
	 * implement radix2
	 * 
	 * @param input
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @return transformed signal
	 */
	public static Complex[] inverseTransformRadix2(final Complex[] input) {

		final int N = input.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = input[0];
			break;
		case 2:
			// N is even, we can use radix-2
			S[0] = (input[0].add(input[1])).divide(norm2);
			S[1] = input[0].subtract(input[1]).divide(norm2);
			break;
		default:

			final int M = N / 2;
			final double omega = -Math.PI / M;
			final Complex Wn = new Complex(0, -omega);

			// if (N % 2 == 0) {
			Complex[] even = new Complex[M];
			Complex[] odd = new Complex[M];
			for (int k = 0; k < input.length / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}
			even = inverseTransformRadix2(even);
			odd = inverseTransformRadix2(odd);

			for (int k = 0; k < M; k++) {
				final Complex f2 = odd[k].multiply((Wn.multiply(k)).exp());
				S[k] = (even[k].add(f2)).multiply(0.5);
				S[k + M] = (even[k].subtract(f2)).multiply(0.5);
			}
			break;
		}
		return S;
	}

	/**
	 * initialize radix-p
	 * 
	 * @param radixLength
	 * @param forward
	 * @return
	 */
	public static Complex[][] initializeRoot(final int radixLength,
			final boolean forward) {
		Complex[][] root = new Complex[radixLength][radixLength];
		switch (radixLength) {
		case 2:
			root[0][0] = new Complex(1, 0);
			root[0][1] = root[0][0];
			root[1][0] = root[0][0];
			root[1][1] = new Complex(-1, 0);
			break;		
		default:
			for (int p = 0; p < radixLength; p++) {
				for (int l = 0; l < radixLength; l++) {
					root[p][l] = forward ? (new Complex(0, -2 * Math.PI * p * l
							/ radixLength)).exp() : (new Complex(0, 2 * Math.PI
							* p * l / radixLength)).exp();
				}
			}
		}

		return root;
	}

	/**
	 * compute Discrete Fourier Transform known as DFT (if forward = true) or
	 * Inverse DFT (if forward = false) of input signal implement radix4
	 * 
	 * @param x
	 *            signal as complex array (1-dimension)
	 * @param forward
	 *            , true if forward transform, false if inverse DFT
	 * @param p
	 *            , radix used (split x in p vectors and compute each FFT
	 *            according with FFT-radix method
	 * @return transformed signal
	 */
	public static Complex[] transformRadixP(final Complex[] x,
			final boolean forward, final int p) {

		final int radixLength = p;
		final int N = x.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = x[0];
			break;
		case 2:
			S[0] = forward ? (x[0].add(x[1])) : (x[0].add(x[1])).divide(norm2);
			S[1] = forward ? x[0].subtract(x[1]) : x[0].subtract(x[1]).divide(
					norm2);
			break;
		default:
			final int M = N / radixLength;
			Complex[][] f = new Complex[radixLength][M];
			// Complex[] radix = new Complex[radixLength];

			Complex[][] root = initializeRoot(radixLength, forward);

			final double omega = forward ? -2 * Math.PI / N : 2 * Math.PI / N;

			// initialize f by splitting x in radixLength
			for (int i = 0; i < radixLength; i++) {
				for (int k = 0; k < M; k++) {
					f[i][k] = x[radixLength * k + i];
				}
				f[i] = transformRadixP(f[i], forward, p);
			}

			Complex Wn = new Complex(0, omega);

			for (int n = 0; n < M; n++) {
				for (int l = 0; l < radixLength; l++) {
					final int index = n + l * M;
					S[index] = f[0][n];
					for (int i = 1; i < radixLength; i++) {
						Complex Wpn = (Wn.multiply(i * n)).exp();
						// root[p][l] = forward ? (new Complex(0, -2 * Math.PI *
						// p * l
						// / radixLength)).exp() : (new Complex(0, 2 * Math.PI
						// * p * l / radixLength)).exp();
						S[index] = S[index].add(f[i][n].multiply(root[l][i]
								.multiply(Wpn)));
					}
					S[index] = forward ? S[index] : S[index]
							.multiply((1.0 / radixLength));
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
	 * @param p
	 *            , radix used (split x in p vectors and compute each FFT
	 *            according with FFT-radix method
	 * @return transformed signal
	 */
	public static Complex[] transformRadix4(final Complex[] x) {

		final int radixLength = 4;
		final int N = x.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = x[0];
			break;
		case 2:
			S[0] = (x[0].add(x[1]));
			S[1] = x[0].subtract(x[1]);
			break;
		default:
			final int M = N / radixLength;
			Complex[][] f = new Complex[radixLength][M];
			// Complex[] radix = new Complex[radixLength];

			// Complex[][] root = initializeRoot(radixLength, forward);

			final double omega = -2 * Math.PI / N;

			// initialize f by splitting x in radixLength
			for (int i = 0; i < radixLength; i++) {
				for (int k = 0; k < M; k++) {
					f[i][k] = x[radixLength * k + i];
				}
				f[i] = transformRadix4(f[i]);
			}

			Complex Wn = new Complex(0, omega);

			for (int n = 0; n < M; n++) {
				S[n] = f[0][n];
				S[n + M] = f[0][n];
				S[n + 2 * M] = f[0][n];
				S[n + 3 * M] = f[0][n];

				for (int i = 1; i < radixLength; i++) {
					Complex Wpn = (Wn.multiply(i * n)).exp();
					S[n] = S[n].add(f[i][n].multiply(Wpn));
					S[n + M] = S[n + M].add(f[i][n].multiply(root[1][i]
							.multiply(Wpn)));
					S[n + 2 * M] = S[n + 2 * M].add(f[i][n].multiply(root[2][i]
							.multiply(Wpn)));
					S[n + 3 * M] = S[n + 3 * M].add(f[i][n].multiply(root[3][i]
							.multiply(Wpn)));
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
	 * @param p
	 *            , radix used (split x in p vectors and compute each FFT
	 *            according with FFT-radix method
	 * @return transformed signal
	 */
	public static Complex[] inverseTransformRadix4(final Complex[] x) {

		final int radixLength = 4;
		final int N = x.length;
		Complex[] S = new Complex[N];

		switch (N) {
		case 1:
			S[0] = x[0];
			break;
		case 2:
			S[0] = (x[0].add(x[1])).divide(norm2);
			S[1] = x[0].subtract(x[1]).divide(norm2);
			break;
		default:
			final int M = N / radixLength;
			Complex[][] f = new Complex[radixLength][M];
			// Complex[] radix = new Complex[radixLength];

			// Complex[][] root = initializeRoot(radixLength, forward);

			final double omega = 2 * Math.PI / N;

			// initialize f by splitting x in radixLength
			for (int i = 0; i < radixLength; i++) {
				for (int k = 0; k < M; k++) {
					f[i][k] = x[radixLength * k + i];
				}
				f[i] = inverseTransformRadix4(f[i]);
			}

			Complex Wn = new Complex(0, omega);

			for (int n = 0; n < M; n++) {
				S[n] = f[0][n];
				S[n + M] = f[0][n];
				S[n + 2 * M] = f[0][n];
				S[n + 3 * M] = f[0][n];

				for (int i = 1; i < radixLength; i++) {
					Complex Wpn = (Wn.multiply(i * n)).exp();
					S[n] = S[n].add(f[i][n].multiply(Wpn));
					S[n + M] = S[n + M].add(f[i][n].multiply(root[1][i]
							.multiply(Wpn)));
					S[n + 2 * M] = S[n + 2 * M].add(f[i][n].multiply(root[2][i]
							.multiply(Wpn)));
					S[n + 3 * M] = S[n + 3 * M].add(f[i][n].multiply(root[3][i]
							.multiply(Wpn)));
				}

				S[n] = S[n].multiply((1.0 / radixLength));

				S[n + M] = S[n + M].multiply((1.0 / radixLength));
				S[n + 2 * M] = S[n + 2 * M].multiply((1.0 / radixLength));
				S[n + 3 * M] = S[n + 3 * M].multiply((1.0 / radixLength));

			}
			break;
		}
		return S;
	}

}
