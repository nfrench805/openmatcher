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
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import filters.Filter;

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
	 * @throws IOException
	 */
	public void match(final InputStream reference, final InputStream candidate)
			throws IOException {
		logger.info("matching reference against candidate...");
		Complex[][] imgRef = greyScale(ImageIO.read(reference));
		Complex[][] imgCand = greyScale(ImageIO.read(candidate));
		match(imgRef, imgCand);
	}

	/**
	 * match Complex[][] ref against Complex[][] search
	 * 
	 * @param ref
	 * @param search
	 */
	public void match(final Complex[][] ref, final Complex[][] search) {

		int N = ref.length;
		int M = ref[0].length;

		// compute FFT
		logger.info("compute FFT of reference");
		Complex[][] FFT_ref = transform(ref, true);

		logger.info("compute FFT of searc");
		Complex[][] FFT_search = transform(search, true);

		// get crossPowerSpectrum
		logger.info("compute cross Power Spectrum");
		Complex[][] S = crossPowerSpectrum(FFT_ref, FFT_search);

		S = Filter.applyHighPass(S, Math.pow(N / 2, 2) + Math.pow(M / 2, 2));
		// get POC as Inverse DFT of cross Power Spectrum
		logger.info("compute POC");
		Complex[][] POC = (Complex[][]) transform(S, false);		

		Point2D peak = getPeak(POC);
	}

	/**
	 * return coordinates of Peak (max real of input), else 0,0
	 * 
	 * @param input
	 * @return
	 */
	public Point2D getPeak(final Complex[][] input) {
		Complex peak = new Complex(0, 0);
		Point2D coordinatesOfPeak = new Point2D.Double();

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				logger.info("POC[" + i + "][" + j + "]=("
						+ input[i][j].getReal() + ";"
						+ input[i][j].getImaginary() + ")");
				if (input[i][j].getReal() > peak.getReal()) {
					peak = input[i][j];
					coordinatesOfPeak.setLocation(i, j);
				}
			}
		}

		logger.info("Peak value:(" + peak.getReal() + ","+peak.getImaginary()+") at coordinates "
				+ coordinatesOfPeak.getX() + "," + coordinatesOfPeak.getY());
		return coordinatesOfPeak;
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
		logger.info("Compute crossPowerSpectrum...");
		int N = F.length;
		int M = F[0].length;
		Complex[][] S = new Complex[N][M];
		int O = G.length;
		int P = G[0].length;

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
		int N = (int) nearestSuperiorPow2(input.length);
		int M = (int) nearestSuperiorPow2(input[0].length);

		// copy array into output
		// and complete with zero Complex if necessary
		// extended size
		Complex[][] output = new Complex[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				output[i][j] = input[i % input.length][j % input[0].length];
			}
		}

		// compute FFT
		output = (Complex[][]) FFT.mdfft(output, forward);
		if (!forward){
			for (int i=0;i<N;i++){
				output[i] = FastFourierTransformer.scaleArray(output[i], 1/Math.sqrt(N*M));
			}
		}
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
		long x = i > 0 ? ((i - 1) & i) : 1;
		return (!FastFourierTransformer.isPowerOf2(x)) ? nearestSuperiorPow2(x)
				: x << 1;
	}

	/**
	 * convert image as greyScale Complex[][] array
	 * 
	 * @param image
	 * @return
	 */
	public Complex[][] greyScale(final BufferedImage image) {
		logger.info("convert image into greyScale...");
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
