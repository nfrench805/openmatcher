/**
 * 
 */
package com.quantum.matcher;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcher {

	// private IMatcher matcher=new FFTMatcher();

	// private MatcherUnit<FFTMatcher> matcher = new MatcherUnit<FFTMatcher>();

	/**
	 * logger
	 */
	protected Logger logger = Logger.getLogger(ImageMatcher.class.getName());

	private FastFourierTransformer FFT = new FastFourierTransformer();

	public ImageMatcher() {
		// matcher.set(new FFTMatcher());
	}

	/**
	 * match image reference against candidate
	 * 
	 * @param reference
	 * @param candidate
	 * @param p
	 *            number of P first of peak to take account to compute score of
	 *            Matching
	 * @throws IOException
	 */
	public double match(final InputStream reference,
			final InputStream candidate, final int p) throws IOException {
		logger.fine("matching reference against candidate...");
		Complex[][] imgRef = greyScale(ImageIO.read(reference));
		Complex[][] imgCand = greyScale(ImageIO.read(candidate));
		return match(imgRef, imgCand, p);
	}

	/**
	 * match Complex[][] reference against Complex[][] search
	 * 
	 * @param ref
	 *            image as complex[][] reference
	 * @param search
	 *            image as complex[][] candidate
	 * @param p
	 *            define the number of peak to sum to compute matching score
	 */
	public double match(final Complex[][] ref, final Complex[][] search,
			final int p) {
		// compute FFT
		logger.fine("compute FFT of reference");
		Complex[][] FFT_ref = transform(ref, true);

		// FFT_ref = Filter.applyHighPass(FFT_ref, 0);

		logger.fine("compute FFT of searc");
		Complex[][] FFT_search = transform(search, true);
		// FFT_search = Filter.applyHighPass(FFT_search, 0);

		// compute Band Limited POC
		Complex[][] POC = getBandLimitedPOC(FFT_ref, FFT_search, false);

		// display 2 greater Peak
		List<Peak> peakList = getPeak(POC);
		return getScore(peakList, p);
	}

	/**
	 * return score of p first peak
	 * 
	 * @param peakList
	 * @param p
	 * @return
	 */
	public double getScore(final List<Peak> peakList, final int p) {
		double score = 0;
		int nb = (p < peakList.size()) ? p : peakList.size();
		for (int i = 0; i < nb; i++) {
			score += peakList.get(i).getAmplitude();
		}
		logger.info("------------------------------------");
		logger.info("Score returned =" + score + " with p=" + nb);
		logger.info("------------------------------------");
		return score;

	}

	/**
	 * from F (FFT of reference image ) and G (FFT of search image) compute Band
	 * Limited Phase Only Correlation Inverse FFT of crossPowerSpectrum (F,G)
	 * limited between -K1,+K1 and -K2,+K2 where K1 = max (x | projection of
	 * Amplitude/k2 >= average) K2 = max (x | projection of Amplitude/k2 >=
	 * average)
	 * 
	 * @param F
	 * @param G
	 * @param limited
	 *            if true, then compute K1, K2 and return Band limited POC else
	 *            return original POC without computing K1,K2
	 * @return
	 */
	public Complex[][] getBandLimitedPOC(final Complex[][] F,
			final Complex[][] G, final boolean limited) {
		int N = F.length;
		int M = F[0].length;

		int K1 = N / 2;
		int K2 = M / 2;

		// get K1 and K2
		if (limited) {
			K1 = getKx(F);
			K2 = getKy(F);
		}

		logger.fine("K1=" + K1 + " K2=" + K2);

		// get crossPowerSpectrum
		Complex[][] S = crossPowerSpectrum(F, G);
		logger.fine("Cross Power Spectrum size:" + S.length + "," + S[0].length);

		Complex[][] Sk1k2 = new Complex[2 * K1 + 1][2 * K2 + 1];
		if (limited) {
			for (int i = -K1; i <= K1; i++) {
				for (int j = -K2; j <= K2; j++) {
					// logger.fine("i="+i+" j="+j+" N="+N+" M="+M);
					Sk1k2[i + K1][j + K2] = S[(i + N / 2) % N][(j + M / 2) % M];
				}
			}
		}

		// get POC as Inverse DFT of cross Power Spectrum
		logger.fine("compute band limited POC");
		Complex[][] POC = limited ? (Complex[][]) transform(Sk1k2, false)
				: (Complex[][]) transform(S, false);
		return POC;
	}

	/**
	 * compute Kx as first x > N/2 where amplitudeOf(sum(F/y)) > average of
	 * Amplitude projected where N is length of F following X
	 * 
	 * @param F
	 *            FFT of image
	 * @return 0 or value between 0 and N/2-1
	 */
	public int getKx(final Complex[][] F) {
		int N = F.length;
		int M = F[0].length;
		double[] projectionOfamplitude = new double[N];
		double average = 0;

		/**
		 * project on X axis / Y axis
		 */
		for (int i = 0; i < N; i++) {
			projectionOfamplitude[i] = 0;
			for (int j = 0; j < M; j++) {
				projectionOfamplitude[i] += amplitudeOf(F[i][j]);
			}
			average += projectionOfamplitude[i];
		}
		average = average / N;

		for (int i = N / 2; i < N; i++) {
			if (projectionOfamplitude[i] > average) {
				return (i - N / 2);
			}
		}

		return 0;
	}

	/**
	 * compute Ky as first y > M/2 where amplitudeOf(sum(F/x)) > average of
	 * Amplitude projected where M is length of F following Y
	 * 
	 * @param F
	 *            FFT of image
	 * @return 0 or value between 0 and M/2-1
	 */
	public int getKy(final Complex[][] F) {
		int N = F.length;
		int M = F[0].length;
		double[] projectionOfamplitude = new double[M];
		double average = 0;

		/**
		 * project on Y axis / X axis
		 */
		for (int j = 0; j < M; j++) {
			projectionOfamplitude[j] = 0;
			for (int i = 0; i < N; i++) {
				projectionOfamplitude[j] += amplitudeOf(F[i][j]);
			}
			average += projectionOfamplitude[j];
		}
		average = average / M;

		for (int i = M / 2; i < M; i++) {
			if (projectionOfamplitude[i] > average) {
				return (i - M / 2);
			}
		}

		return 0;
	}

	/**
	 * return Magnitude/Amplitude of complex input
	 * 
	 * @param input
	 * @return return amplitude as sqrt(real^2+imaginary^2)
	 */
	public double amplitudeOf(final Complex input) {
		return Math.sqrt(Math.pow(input.getReal(), 2)
				+ Math.pow(input.getImaginary(), 2));
	}

	/**
	 * return phase of complex
	 * 
	 * @param input
	 * @return phase (angle in Radian between PI and -PI)
	 */
	public double phaseOf(final Complex input) {
		return Math.atan2(input.getImaginary(), input.getReal());
	}

	/**
	 * return coordinates of Peak (max real of input), else 0,0
	 * 
	 * @param input
	 * @return
	 */
	public List<Peak> getPeak(final Complex[][] input) {

		List<Peak> peakList = new ArrayList<Peak>();

		// Complex peak = new Complex(0, 0);

		int N = input.length;
		int M = input[0].length;

		logger.fine("N=" + N + " M=" + M);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Peak peak = new Peak();
				Point2D coordinatesOfPeak = new Point2D.Double();
				coordinatesOfPeak.setLocation(i, j);
				double amplitude = amplitudeOf(input[i][j]);
				double phase = phaseOf(input[i][j]);
				peak.setPoint(coordinatesOfPeak);
				peak.setAmplitude(amplitude);
				peak.setPhase(phase);
				peakList.add(peak);
			}
		}
		Collections.sort(peakList);
		return peakList;
	}

	/**
	 * compute crossPowerSpectrum of Complex[][] F and G
	 * 
	 * @param F
	 *            , FFT (Complex[][] array)
	 * @param G
	 *            , FFT (Complex[][] array)
	 * @return cross Power Spectrum as (F*G.Conjugate() /
	 *         (F*G.conjugate()).abs())
	 */
	public Complex[][] crossPowerSpectrum(final Complex[][] F,
			final Complex[][] G) {
		logger.fine("Compute crossPowerSpectrum...");
		int N = F.length;
		int M = F[0].length;
		Complex[][] S = new Complex[N][M];
		int O = G.length;
		int P = G[0].length;

		logger.fine("N=" + N + " M=" + M + " O=" + O + " P=" + P);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				S[i][j] = F[i][j].multiply(G[i][j].conjugate());
				S[i][j] = S[i][j].divide(new Complex(S[i][j].abs(), 0));
			}
		}
		return S;
	}

	public Complex[][] transform(final Complex[][] input, final boolean forward) {
		// get size of array (take nearest power of 2 if necessary)
		long N = nearestSuperiorPow2((long) input.length);
		long M = nearestSuperiorPow2((long) input[0].length);
		Complex ONE = new Complex(1, 0);

		logger.fine("N=" + N + " M=" + M + "(input size=" + input.length + ","
				+ input[0].length);

		// copy array into output
		// and complete with zero Complex if necessary
		// extended size
		Complex[][] output = new Complex[(int) N][(int) M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// Complex e= (!forward)?new
				// Complex(0,-Math.PI*(i+j)).exp():ONE;
				if ((i >= input.length) || (j >= input[0].length)) {
					output[i][j] = new Complex(0, 0);
				} else {
					// output[i][j] = input[i][j].multiply(e);
					output[i][j] = input[i][j];
				}

			}
		}

		// compute FFT
		output = (Complex[][]) FFT.mdfft(output, forward);
		if (!forward) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					output[i][j] = output[i][j].multiply(1L / Math.sqrt(N * M));
				}
			}
		}

		logger.fine("N=" + output.length + " M=" + output[0].length);
		return output;
	}

	/**
	 * return nearest greater power of 2
	 * 
	 * @param i
	 *            value to evaluate
	 * @return i if i is power of 2, else nearest superior power of 2 for
	 *         example nearestSuperiorPow2(7) will return 8.
	 */
	public long nearestSuperiorPow2(final long i) {
		if (FastFourierTransformer.isPowerOf2(i)) {
			return i;
		} else {
			long x = i > 0 ? ((i - 1) & i) : 1;
			return (!FastFourierTransformer.isPowerOf2(x)) ? nearestSuperiorPow2(x)
					: x << 1;
		}
	}

	/**
	 * convert image as greyScale Complex[][] array
	 * 
	 * @param image
	 * @return
	 */
	public Complex[][] greyScale(final BufferedImage image) {
		logger.fine("convert image into greyScale...");
		int width = image.getWidth();
		int height = image.getHeight();
		Complex[][] imgRef = new Complex[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pix = (int) image.getRGB(x, y);
				// int alpha=(pix >> 24) & 0x0ff;
				int r = (pix >> 16) & 0x0ff;
				int g = (pix >> 8) & 0x0ff;
				int b = pix & 0x0ff;
				imgRef[x][y] = new Complex((r + g + b) / 3.0, 0);
			}
		}
		return imgRef;
	}

	/**
	 * create an image from a double array
	 * 
	 * @param img
	 * @param width
	 * @return
	 */
	public static BufferedImage CreateImageFromMatrix(double[] img, int width) {
		int[] grayImage = new int[img.length];
		double[] scales = (double[]) img.clone();
		Arrays.sort(scales);
		double min = scales[0];
		double max = scales[scales.length - 1];
		for (int i = 0; i < grayImage.length; i++) {
			double v = img[i];
			v -= min;
			v /= (max - min);
			short val = (short) (v * 255);
			grayImage[i] = (val << 16) | (val << 8) | (val);
		}
		BufferedImage bi = new BufferedImage(width, img.length / width,
				BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, width, img.length / width, grayImage, 0, width);
		return bi;
	}

	/**
	 * display an image
	 * 
	 * @param image
	 */
	public void displayImage(final Image image) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
