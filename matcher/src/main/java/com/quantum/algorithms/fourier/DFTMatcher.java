package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;

import org.apache.commons.math.complex.Complex;

public class DFTMatcher extends GenericMatcher implements IMatcher {

	public double match(double[][] reference, double[][] candidate) {
		int N1 = reference.length;
		int N2 = reference[0].length;
		
		logger.info("width=" + N1 + " height=" + N2);
		// we suppose images with same size
		logger.info("computing DFT of reference...");
		Complex[][] F = get2D_DFT(reference);
		logger.info("computing DFT of candidate...");
		Complex[][] G = get2D_DFT(candidate);
		logger.info("computing cross phase spectrum...");
		Complex[][] R = getCrossPhaseSpectrum(F, G);
		logger.info("computing POC...");
		Complex[][] POC = get2D_IDFT(R, N1, N2);
		logger.info("looking for a peak in POC...");
		Point2D peak = getPeak(POC);
		Complex score = POC[(int) peak.getX()][(int) peak.getY()];		
		return score.getReal();
	}

	private final double PI_X_2 = 2 * Math.PI;

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
				F=F.add(new Complex(0,-PI_X_2 * (k1*n1/N1 + k2*n2/N2)).exp().multiply(f[n1][n2]));
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
				F[k1][k2] = getDFT(f, k1, k2);				
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
				r=r.add((new Complex(0,PI_X_2 * (k1*n1/N1 + k2*n2/N2)).exp()).multiply(R[k1][k2]));
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
				r[k1][k2] = getIDFT(R, k1, k2);
			}
		}
		return r;
	}

}
