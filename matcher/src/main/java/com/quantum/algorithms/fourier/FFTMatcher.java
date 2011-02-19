/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Pascal Dergane An FFT-Based Technique for Translation, Rotation, and
 *         Scale-Invariant Image Registration by B. Srinivasa Reddy and
 *         B.N.Chatterji, 1996
 * 
 */
public class FFTMatcher extends GenericMatcher {
	
	/**
	 * pointer on Fouirer Transformer (object allowing to compute FFT, and
	 * reverseFFT
	 */
	private FastFourierTransformer FFT = new FastFourierTransformer();

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#getCrossPowerSpectrum(org.apache.commons.math.complex.Complex[][], org.apache.commons.math.complex.Complex[][])
	 */
	@Override
	public Complex[][] getCrossPowerSpectrum(final Complex[][] Ga,
			final Complex[][] Gb) {

		int Ga_width = Ga.length;
		int Ga_height = Ga[0].length;

		Complex[][] R = new Complex[Ga_width][Ga_height];

		for (int i = 0; i < Ga_width; i++) {
			for (int j = 0; j < Ga_height; j++) {
				Complex tempo = Ga[i][j].multiply(Gb[i][j].conjugate());
				R[i][j] = (0L != tempo.abs()) ? tempo
						.multiply(1L / tempo.abs()) : tempo;
			}
		}
		return R;
	}

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#match(double[][], double[][])
	 */
	public double match(final double[][] reference, final double[][] candidate) {
		/**
		 * Step 1: create a Complex[][] matrix, where real part is reference,
		 * and imaginary part for candidate
		 */

		int N1 = reference.length;
		int N2 = reference[0].length;
		N1=(int) ((this.isPowerOf2(N1))?N1:this.nearestSuperiorPow2(N1));
		N2=(int) ((this.isPowerOf2(N2))?N2:this.nearestSuperiorPow2(N2));
		
		double[][] newRef=new double [N1][N2];
		double[][] newCand=new double [N1][N2];
		
		
		for (int i=0;i<N1;i++){
			for (int j=0;j<N2;j++){
				newRef[i][j]=reference[i%reference.length][j%reference[0].length];
				newCand[i][j]=candidate[i%candidate.length][j%candidate[0].length];
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

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#getCrossPhaseSpectrum(org.apache.commons.math.complex.Complex[][], org.apache.commons.math.complex.Complex[][])
	 */
	public Complex[][] getCrossPhaseSpectrum(final Complex[][] F,
			final Complex[][] G) {
		int N1 = F.length;
		int N2 = F[0].length;

		Complex[][] R = new Complex[N1][N2];

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				Complex tempo = F[k1][k2].multiply(G[k1][k2].conjugate());
				R[k1][k2] = (tempo.abs() > 0) ? tempo
						.multiply(1L / tempo.abs()) : tempo;
			}
		}
		return R;
	}

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#getDFT(double[][], int, int)
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

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#get2D_DFT(double[][])
	 */
	public Complex[][] get2D_DFT(final double[][] f) {
		int N1 = f.length;
		int N2 = f[0].length;
		Complex[][] F = new Complex[N1][N2];

		// for (int k1=0;k1<N1;k1++){
		// for(int k2=0;k2<N2;k2++){
		// F[k1][k2] = this.getDFT(f,k1,k2);
		// }
		// }

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				F[k1][k2] = new Complex(f[k1][k2], 0);
			}
		}

		F = (Complex[][]) FFT.mdfft(F, true);

		return F;
	}

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#getIDFT(org.apache.commons.math.complex.Complex[][], int, int)
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

	/* (non-Javadoc)
	 * @see com.quantum.algorithms.fourier.IMatcher#get2D_IDFT(org.apache.commons.math.complex.Complex[][], int, int)
	 */
	public Complex[][] get2D_IDFT(final Complex[][] R, final int N1,
			final int N2) {
		Complex[][] r = new Complex[N1][N2];
		// for (int k1 = 0; k1 < N1; k1++) {
		// for (int k2 = 0; k2 < N2; k2++) {
		// r[k1][k2] = this.getIDFT(R, k1, k2);
		// }
		// }

		r = (Complex[][]) FFT.mdfft(R, false);

		for (int k1 = 0; k1 < N1; k1++) {
			for (int k2 = 0; k2 < N2; k2++) {
				r[k1][k2] = r[k1][k2].multiply(1L / Math.sqrt(N1 * N2));
			}
		}
		return r;
	}

	
}
