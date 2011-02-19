package com.quantum.algorithms.fourier;

import org.apache.commons.math.complex.Complex;

public class DFTMatcher extends GenericMatcher implements IMatcher {

	
	public double match(double[][] reference, double[][] candidate) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * Compute DFT element(k1,k2) of f(k1,k2)
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

		for (int n1 = 0; n1 < N1; n1++) {
			for (int n2 = 0; n2 < N2; n2++) {
				double a1 = -2 * Math.PI * k1 * n1 / N1;
				double a2 = -2 * Math.PI * k2 * n2 / N2;
				Complex W1 = new Complex(Math.cos(a1), Math.sin(a1));
				Complex W2 = new Complex(Math.cos(a2), Math.sin(a2));
				F = F.add(W1.multiply(W2).multiply(f[n1][n2]));
			}
		}
		return F;
	}

	/**
	 * compute DFT of f using normal DFT method return an array type of
	 * Complex[][]
	 */
	public Complex[][] get2D_DFT(final double[][] f) {
		int N1 = f.length;
		int N2 = f[0].length;
		Complex[][] F = new Complex[N1][N2];

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				F[k1][k2] = this.getDFT(f, k1, k2);
			}
		}
		return F;
	}

	/**
	 * compute and return normalized IDFT element (n1,n2) of R(n1,n2)
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

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				double a1 = 2 * Math.PI * k1 * n1 / N1;
				double a2 = 2 * Math.PI * k2 * n2 / N2;
				Complex W1 = new Complex(Math.cos(a1), Math.sin(a1));
				Complex W2 = new Complex(Math.cos(a2), Math.sin(a2));
				r = r.add(W1.multiply(W2).multiply(R[k1][k2]));
			}
		}
		return r.multiply(1L / (N1 * N2));

	}

	/**
	 * compute IDFT using normal method
	 */
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2) {
		Complex[][] r = new Complex[N1][N2];
		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				r[k1][k2] = this.getIDFT(R, k1, k2);
			}
		}
		return r;
	}

}
