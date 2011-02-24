package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.math.complex.Complex;

public class DFTMatcher extends GenericMatcher implements IMatcher {

	public double match(double[][] reference, double[][] candidate) {
		int N1 = reference.length;
		int N2 = reference[0].length;

		logger.info("width=" + N1 + " height=" + N2);
		//initialize(N1, N2);
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

		for (int n1 = -N1 / 2; n1 < N1 / 2; n1++) {
			for (int n2 = -N2 / 2; n2 < N2 / 2; n2++) {
				double theta1 = 2 * Math.PI * k1 * n1 / N1;
				double theta2 = 2 * Math.PI * k2 * n2 / N2;
				Complex W1 = new Complex(Math.cos(theta1), -Math.sin(theta1));
				Complex W2 = new Complex(Math.cos(theta2), -Math.sin(theta2));

				F = F.add(W1.multiply(W2).multiply(f[n1 + N1 / 2][n2 + N2 / 2]));

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
	 * compute and return normalized IDFT element (n1,n2) of R(n1,n2)
	 * IDFT(n1,n2) = Sum(k1,k2) of (1/(N1*N2) *R[n1,n2] * Complex(0,+ j *2 * PI
	 * * k1 * n1/N1)* Complex(0,+ j *2 * PI * k2 * n2/N2))
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

		for (int k1 = -N1 / 2; k1 < N1 / 2; k1++) {
			for (int k2 = -N2 / 2; k2 < N2 / 2; k2++) {
				double theta1 = 2 * Math.PI * k1 * n1 / N1;
				double theta2 = 2 * Math.PI * k2 * n2 / N2;
				Complex W1 = new Complex(Math.cos(theta1), Math.sin(theta1));
				Complex W2 = new Complex(Math.cos(theta2), Math.sin(theta2));
				r = r.add(W1.multiply(W2).multiply(R[k1 + N1 / 2][k2 + N2 / 2]));

			}
		}

		return r.divide(new Complex(N1*N2,0));
		
	}

	/**
	 * compute IDFT using normal method
	 */
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2) {
		Complex[][] r = new Complex[N1][N2];
		for (int n1 = -N1 / 2; n1 < N1 / 2; n1++) {
			for (int n2 = -N2 / 2; n2 < N2 / 2; n2++) {
				r[n1 + N1 / 2][n2 + N2 / 2] = getIDFT(R, n1, n2);
			}
		}		
		return r;
	}

}
