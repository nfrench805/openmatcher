package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.complex.Complex;

public class DFTMatcher extends GenericMatcher implements IMatcher {

	private Map<Integer, Complex> W1 = null;
	private Map<Integer, Complex> W2 = null;

	public double match(double[][] reference, double[][] candidate) {
		int N1 = reference.length;
		int N2 = reference[0].length;

		logger.info("width=" + N1 + " height=" + N2);
		W1 = initialize(N1);
		W2 = initialize(N2);
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
		Complex F = new Complex(f[N1 / 2][N2 / 2], 0);
		Complex W1 = this.W1.get(Integer.valueOf(-k1 * N1 / 2)).conjugate();
		Complex W2 = this.W2.get(Integer.valueOf(-k2 * N2 / 2)).conjugate();
		F = F.add(W1.multiply(W2).multiply(f[0][0]));

		for (int n1 = 0; n1 < N1 / 2; n1++) {
			for (int n2 = 0; n2 < N2 / 2; n2++) {

				// double theta1 = 2 * Math.PI * k1 * n1 / N1;
				// double theta2 = 2 * Math.PI * k2 * n2 / N2;
				// Complex W1 = new Complex(Math.cos(theta1),
				// -Math.sin(theta1));
				// Complex W2 = new Complex(Math.cos(theta2),
				// -Math.sin(theta2));
				W1 = this.W1.get(Integer.valueOf(k1 * n1)).conjugate();
				W2 = this.W2.get(Integer.valueOf(k2 * n2)).conjugate();

				// n1>0 && n2>0
				Complex a = W1.multiply(W2).multiply(
						f[n1 + N1 / 2][n2 + N2 / 2]);
				// n1>0 && n2<0
				Complex b = W1.multiply(W2.conjugate()).multiply(
						f[n1 + N1 / 2][-n2 + N2 / 2]);
				// n1<0 && n2>0
				Complex c = W1.conjugate().multiply(W2)
						.multiply(f[-n1 + N1 / 2][n2 + N2 / 2]);
				// n1<0 && n2<0
				Complex d = W1.multiply(W2).conjugate()
						.multiply(f[-n1 + N1 / 2][-n2 + N2 / 2]);
				F = (n1 == 0 && n2 == 0) ? F : F.add(a).add(b).add(c).add(d);
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
		Complex r = R[N1 / 2][N2 / 2];
		Complex W1 = this.W1.get(Integer.valueOf(n1 * N1 / 2));
		Complex W2 = this.W2.get(Integer.valueOf(n2 * N2 / 2));
		r = r.add(W1.multiply(W2).add(R[0][0]));

		for (int k1 = 0; k1 < N1 / 2; k1++) {
			for (int k2 = 0; k2 < N2 / 2; k2++) {

				W1 = this.W1.get(Integer.valueOf(k1 * n1));
				W2 = this.W2.get(Integer.valueOf(k2 * n2));

				// k1>0 && k2>0
				Complex a = W1.multiply(W2).multiply(
						R[k1 + N1 / 2][k2 + N2 / 2]);
				// k1>0 && k2<0
				Complex b = W1.multiply(W2.conjugate()).multiply(
						R[k1 + N1 / 2][-k2 + N2 / 2]);
				// k1<0 && k2>0
				Complex c = W1.conjugate().multiply(W2)
						.multiply(R[-k1 + N1 / 2][k2 + N2 / 2]);
				// k1<0 && k2<0
				Complex d = W1.multiply(W2).conjugate()
						.multiply(R[-k1 + N1 / 2][-k2 + N2 / 2]);
				r = (k1 == 0 && k2 == 0) ? r : r.add(a).add(b).add(c).add(d);

			}
		}

		return r.divide(new Complex(N1 * N2, 0));

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
