package com.quantum.algorithms.fourier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.complex.Complex;

public class DFTMatcher extends GenericMatcher implements IMatcher {

	private Map<Integer, Complex> W1 = null;
	private Map<Integer, Complex> W2 = null;

	/**
	 * initialize W
	 * 
	 * @param N1
	 * @param N2
	 */
	public Map<Integer, Complex> initialize(final int N) {
		logger.info("Initializing W...");
		Map<Integer, Complex> W = new HashMap<Integer, Complex>();

		for (int k = 0; k <= N / 2; k++) {
			for (int n = 0; n <= N / 2; n++) {
				double omega = Math.PI * 2 * k * n / N;
				W.put(Integer.valueOf(k * n), new Complex(Math.cos(omega),
						-Math.sin(omega)));
				W.put(Integer.valueOf(-k * n), new Complex(Math.cos(omega),
						Math.sin(omega)));
			}
		}

		return W;
	}


	/**
	 * compute Fourier transform F[k1][k2] = sum(-N1/2<n1<N1/2) of sum(
	 * -N2/2<n2<N2/2) of (f[k1][k2] * exp(-j*2*PI/N1*k1*n1) * exp(-j * 2 * PI *
	 * k2* n2/N2))
	 * 
	 * @param f
	 * @param k1
	 * @param k2
	 * @return
	 */
	public Complex getDFT(final double[][] f, final int k1, final int k2) {
		int N1 = f.length;
		int N2 = f[0].length;
		Complex F = new Complex(0, 0);
		Complex w1;
		Complex w2;

		if (W1 == null) {
			W1 = initialize(N1);
		}

		if (W2 == null) {
			W2 = initialize(N2);
		}

		for (int n1 = -N1 / 2; n1 < N1 / 2; n1++) {
			for (int n2 = -N2 / 2; n2 < N2 / 2; n2++) {

				w1 = W1.get(Integer.valueOf(k1 * n1)).conjugate();
				w2 = W2.get(Integer.valueOf(k2 * n2)).conjugate();

				F = F.add(w1.multiply(w2).multiply(f[n1 + N1 / 2][n2 + N2 / 2]));
			}

		}
		return F;
	}

	/**
	 * compute DFT of f using normal DFT method return an array type of
	 * Complex[][]
	 */
	public Complex[][] get2D_DFT(final double[][] f) {

		logger.info("Computing 2D DFT...");
		int N1 = f.length;
		int N2 = f[0].length;
		Complex[][] F = new Complex[N1][N2];

		for (int k1 = -N1 / 2; k1 < N1 / 2; k1++) {
			for (int k2 = -N2 / 2; k2 < N2 / 2; k2++) {
				F[k1 + N1 / 2][k2 + N2 / 2] = getDFT(f, k1, k2);
			}
		}
		logger.info("2D DFT computed...");
		return F;
	}

	/**
	 * compute Inverse Fourier transform r[n1][n2] = sum(-N1/2<k1<N1/2) of sum(
	 * -N2/2<k2<N2/2) of (R[k1][k2] * exp(+j*2*PI/N1*k1*n1) * exp(+j * 2 * PI *
	 * k2* n2/N2))
	 * 
	 * @param R
	 * @param n1
	 * @param n2
	 * @return
	 */
	public Complex getIDFT(final Complex[][] R, final int n1, final int n2) {
		int N1 = R.length;
		int N2 = R[0].length;
		Complex r = new Complex(0, 0);

		Complex w1;
		Complex w2;

		if (W1 == null) {
			W1 = initialize(N1);
		}

		if (W2 == null) {
			W2 = initialize(N2);
		}

		for (int k1 = -N1 / 2; k1 < N1 / 2; k1++) {
			for (int k2 = -N1 / 2; k2 < N2 / 2; k2++) {

				w1 = this.W1.get(Integer.valueOf(k1 * n1));
				w2 = this.W2.get(Integer.valueOf(k2 * n2));
				r = r.add(w1.multiply(w2).multiply(R[k1 + N1 / 2][k2 + N2 / 2]));
			}
		}

		int k1 = -N1 / 2;
		w1 = this.W1.get(Integer.valueOf(k1 * n1));
		for (int k2 = -N2 / 2; k2 < N2 / 2; k2++) {
			w2 = this.W2.get(Integer.valueOf(k2 * n2));
			r = r.add(w1.multiply(w2).multiply(R[0][n2 + N2 / 2]));
		}

		return r.divide(new Complex(N1 * N2, 0));
	}

	/**
	 * compute IDFT using normal method
	 */
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2) {
		Complex[][] r = new Complex[N1][N2];

		for (int k1 = -N1 / 2; k1 < N1 / 2; k1++) {
			for (int k2 = -N2 / 2; k2 < N2 / 2; k2++) {
				r[k1 + N1 / 2][k2 + N2 / 2] = this.getIDFT(R, k1, k2);
			}
		}
		return r;
	}

	/**
	 * compute dimension-1 DFT according with formulae F[k] = sum(n) { f[n] x
	 * (cos(omega[k,n]) - j x sin(omega[k,n]) )/sqrt(N) where omega[n] = 2*PI/N
	 * * k*n where -N/2<= n,k < +N/2 N is size of f
	 **/
	public Complex[] getDFT(Complex[] f) {
		return transform(f, true);
	}

	public Complex[] transform(final Complex[] f, final boolean forward) {
		// get nb items of serie f
		int N = f.length;
		double direction = forward ? 1L : -1L;

		Complex[] result = new Complex[N];

		for (int k = -N / 2; k < N / 2; k++) {
			result[k] = new Complex(0, 0);
			for (int n = -N / 2; n < N / 2; n++) {
				double omega = 2 * Math.PI * k * n / N;
				Complex W = new Complex(Math.cos(omega), direction
						* Math.sin(omega));
				result[k] = result[k].add(W.multiply(f[n]));
			}
			result[k] = result[k].divide(new Complex(Math.sqrt(N), 0));
		}
		return result;
	}

	/**
	 * compute dimension-1 Inverse DFT according with formulae F[k] = sum(n) {
	 * f[n] x (cos(omega[k,n]) + j x sin(omega[k,n]) )/sqrt(N) where omega[n] =
	 * 2*PI/N * k*n where -N/2<= n,k < +N/2 N is size of f
	 **/
	public Complex[] getIDFT(Complex[] F) {
		return transform(F, false);
	}

}
