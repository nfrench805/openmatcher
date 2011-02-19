/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Pascal Dergane An FFT-Based Technique for Translation, Rotation, and
 *         Scale-Invariant Image Registration by B. Srinivasa Reddy and
 *         B.N.Chatterji, 1996
 * 
 */
public class FFTMatcher extends GenericMatcher implements IMatcher {

	/**
	 * pointer on Fourier Transformer (object allowing to compute FFT, and
	 * reverseFFT
	 */
	private FastFourierTransformer FFT = new FastFourierTransformer();

	/**
	 * match reference against candidate
	 */
	public double match(final double[][] reference, final double[][] candidate) {
		/**
		 * Step 1: create a Complex[][] matrix, where real part is reference,
		 * and imaginary part for candidate
		 */

		int N1 = reference.length;
		int N2 = reference[0].length;
		N1 = (int) ((this.isPowerOf2(N1)) ? N1 : this.nearestSuperiorPow2(N1));
		N2 = (int) ((this.isPowerOf2(N2)) ? N2 : this.nearestSuperiorPow2(N2));

		double[][] newRef = new double[N1][N2];
		double[][] newCand = new double[N1][N2];

		//assume there's a defined value for extract pixels
		for (int i = 0; i < N1; i++) {
			for (int j = 0; j < N2; j++) {				
				newRef[i][j] = reference[i % reference.length][j
						% reference[0].length];
				newCand[i][j] = candidate[i % candidate.length][j
						% candidate[0].length];
			}
		}

		logger.info("width=" + N1 + " height=" + N2);
		// we suppose images with same size
		Complex[][] F = this.get2D_DFT(newRef);
		Complex[][] G = this.get2D_DFT(newCand);
		Complex[][] R = getCrossPhaseSpectrum(F, G);
		Complex[][] POC = this.get2D_IDFT(R, N1, N2);
		Point2D peak = this.getPeak(POC);
		Complex score = POC[(int) peak.getX()][(int) peak.getY()];
		return score.getReal();
	}

	/**
	 * compute DFT of f using FFT return an array type of Complex[][]
	 */
	public Complex[][] get2D_DFT(final double[][] f) {
		int N1 = f.length;
		int N2 = f[0].length;
		Complex[][] F = new Complex[N1][N2];

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				F[k1][k2] = new Complex(f[k1][k2], 0);
			}
		}

		F = (Complex[][]) FFT.mdfft(F, true);

		return F;
	}

	/**
	 * compute IDFT using FFT method
	 */
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2) {
		Complex[][] r = new Complex[N1][N2];

		r = (Complex[][]) FFT.mdfft(R, false);

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				r[k1][k2] = r[k1][k2].multiply(1L / Math.sqrt(N1 * N2));
			}
		}
		return r;
	}

}
