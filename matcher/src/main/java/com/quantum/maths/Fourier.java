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

	private static Complex norm2 = new Complex(Math.sqrt(2), 0);

	// private static Complex norm4 = new Complex(Math.sqrt(4), 0);

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

		final int N = input.length;
		Complex[] S = new Complex[N];

		if (N == 1) {
			S[0] = input[0];

		} else if (N == 2) {
			// N is even, we can use radix-2
			S[0] = (input[0].add(input[1])).divide(norm2);
			S[1] = input[0].subtract(input[1]).divide(norm2);
			return S;
		}

		final double omega = -2 * Math.PI / N;
		final Complex Wn = forward ? new Complex(0, omega).exp() : new Complex(
				0, -omega).exp();

//		if (N % 4 == 0 && forward) {
//
//			Complex[] even = new Complex[N / 2];
//			Complex[] odd1 = new Complex[N / 4];
//			Complex[] odd2 = new Complex[N / 4];
//
//			odd1[0] = input[1];
//			odd2[0] = input[N / 4 - 1];
//
//			for (int k = 0; k < even.length; k++) {
//				even[k] = input[2 * k];
//				if (k < odd1.length & k > 0) {
//					odd1[k] = input[4 * k + 1];
//					odd2[k] = input[4 * k - 1];
//				}
//			}
//
//			even = transform(even, forward);
//			odd1 = transform(odd1, forward);
//			odd2 = transform(odd2, forward);
//
//			for (int k = 0; k < N / 4; k++) {
//				final Complex Wnk = Wn.pow(new Complex(k, 0));
//				final Complex Wnkminus = Wn.conjugate();
//
//				Complex f1 = (Wnk.multiply(odd1[k])).add((Wnkminus
//						.multiply(odd2[k])));
//				Complex f2 = (Wnk.multiply(odd1[k])).subtract((Wnkminus
//						.multiply(odd2[k])));
//				
//				f2 = Complex.I.negate().multiply(odd1[k]);
//				
//				S[k] = (even[k].add(odd1[k].multiply(Wnk))).divide(norm2);
//				S[k + N / 2] = (even[k].subtract((odd1[k].multiply(Wnk)))).divide(norm2);
//				
//				S[k + N / 4] = (even[k + N / 4].add(f2)).divide(norm2);
//				S[k + 3 * N / 4] = (even[k + N / 4].subtract(f2)).divide(norm2);
//				
//
//				// final Complex w4 = forward ? Complex.I.negate().multiply(
//				// (Wn.pow(new Complex(k, 0)))) : Complex.I.multiply((Wn
//				// .pow(new Complex(k, 0))));				
//				// final Complex f4 = odd[k + N / 4].multiply(w4);
//				
//				//final Complex f2 = odd[k].multiply(w);
//				//S[k] = even[k].add(f2).divide(norm2);
//				S[k + N / 2] = even[k].subtract(f2).divide(norm2);
//				// S[k + N / 4] = even[k + N / 4].add(f4).divide(norm2);
//				
//				// S[k + 3 * N / 4] = even[k + N /
//				// 4].subtract(f4).divide(norm2);
//			}
//			return S;
//		}

		if (N % 2 == 0) {
			Complex[] even = new Complex[N / 2];
			Complex[] odd = new Complex[N / 2];
			for (int k = 0; k < input.length / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}
			even = transform(even, forward);
			odd = transform(odd, forward);

			for (int k = 0; k < N / 2; k++) {
				final Complex w = Wn.pow(new Complex(k, 0));
				// final Complex w4 = forward ? Complex.I.negate().multiply(
				// (Wn.pow(new Complex(k, 0)))) : Complex.I.multiply((Wn
				// .pow(new Complex(k, 0))));
				final Complex f2 = odd[k].multiply(w);
				// final Complex f4 = odd[k + N / 4].multiply(w4);
				S[k] = even[k].add(f2).divide(norm2);
				// S[k + N / 4] = even[k + N / 4].add(f4).divide(norm2);
				S[k + N / 2] = even[k].subtract(f2).divide(norm2);
				// S[k + 3 * N / 4] = even[k + N /
				// 4].subtract(f4).divide(norm2);
			}
		}

		return S;
	}

}
