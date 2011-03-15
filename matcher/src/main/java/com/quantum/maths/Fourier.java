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
		if (N == 1) {
			return input;
		}

		final double omega = 2 * Math.PI / N;
		final Complex Wn = forward ? new Complex(0, -omega).exp()
				: new Complex(0, omega).exp();
		final Complex norm = new Complex(2,0);
		Complex[] S = new Complex[N];
		//TODO: complete 4-radix method
		if (N % 4 == 0) {
			//we can use radix-4
			
			
			for (int q=0;q<=N/4 -1;q++){
				for (int p=0;p<4;p++){
					S[N/4*p+q] = 	
				}
			 
			}
		}
		if (N % 2 == 0) {
			// N is even, we can use radix-2
			Complex[] even = new Complex[N / 2];
			Complex[] odd = new Complex[N / 2];
			for (int k = 0; k < input.length / 2; k++) {
				even[k] = input[2 * k];
				odd[k] = input[2 * k + 1];
			}

			even = transform(even, forward);
			odd = transform(odd, forward);

			for (int k = 0; k < N/2; k++) {
				final Complex w = Wn.pow(new Complex(k, 0));
				final Complex f1 = even[k];
				final Complex f2 = odd[k].multiply(w);
				
				S[k] = (forward) ? f1.add(f2) : f1
						.add(f2).divide(norm);
				S[k+N/2] = (forward)?f1.add(f2.negate()):f1.add(f2.negate()).divide(norm);
			}
		}
		return S;
	}

	/**
	 * return kth item of input fourier transform
	 * 
	 * @param input
	 *            signal to transform
	 * @param forward
	 *            if true, compute DFT, else compute IDFT
	 * @param k
	 *            position of item to compute
	 * @return
	 */
	private static Complex transform(Complex[] input, boolean forward, int k) {
		int N = input.length;
		Complex S = new Complex(0, 0);
		double omega = (forward) ? -2 * Math.PI * k / N : 2 * Math.PI * k / N;
		Complex Wn = new Complex(0, -2 * Math.PI / N).exp();
		Complex Wkn = Wn.pow(new Complex(k, 0));

		if (N % 2 == 0) {
			Complex[] even = new Complex[N / 2];
			Complex[] odd = new Complex[N / 2];

			for (int n = 0; n < N / 2; n++) {
				even[n] = input[2 * n];
				odd[n] = input[2 * n + 1];
			}

			S = (forward) ? transform(even, forward, k).add(
					transform(odd, forward, k).multiply(Wkn)) : transform(even,
					forward, k).add(
					transform(odd, forward, k).multiply(Wkn.conjugate()));
			return (forward) ? S : S.divide(new Complex(N, 0));
		} else {
			for (int n = 0; n < N; n++) {
				S = S.add(input[n].multiply((new Complex(0, omega * n)).exp()));
			}
			return (forward) ? S : S.divide(new Complex(N, 0));
		}

	}

}
