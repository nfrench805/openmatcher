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
	public MatchingScore match(final InputStream reference,
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
	public MatchingScore match(final Complex[][] ref, final Complex[][] search,
			final int p) {
		// compute FFT
		logger.fine("compute FFT of reference");
		Complex[][] FFT_ref = transform(ref, true);

		// FFT_ref = Filter.applyHighPass(FFT_ref, 0);

		logger.fine("compute FFT of searc");
		Complex[][] FFT_search = transform(search, true);
		// FFT_search = Filter.applyHighPass(FFT_search, 0);

		// compute Band Limited POC
		Complex[][] POC = getPOC(FFT_ref, FFT_search);

		// display 2 greater Peak
		List<Peak> peakList = getPeaks(POC);
		return getScore(peakList, p);
	}

	/**
	 * return score of p first peak
	 * 
	 * @param peakList
	 * @param p
	 * @return
	 */
	public MatchingScore getScore(final List<Peak> peakList, final int p) {
		MatchingScore matchingScore = new MatchingScore();
		double score = 0.0;
		
		final int nb = Math.min(peakList.size(),p);
		for (int i = 0; i < nb; i++) {
			System.out.println("Peak["+i+"]="+peakList.get(i).getAmplitude()+" at ("+
					(int) peakList.get(i).getPoint().getX()+","+
					(int) peakList.get(i).getPoint().getY());
			score += peakList.get(i).getAmplitude();
		}
		logger.info("------------------------------------");
		logger.info("Score returned =" + score + " with p=" + nb);
		logger.info("------------------------------------");
		
		matchingScore.setScore(score);
		matchingScore.setHorizontal_shift(peakList.get(0).getRelativePixel().getX());
		matchingScore.setVertical_shift(peakList.get(0).getRelativePixel().getY());
		return matchingScore;

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
	public Complex[][] getPOC(final Complex[][] F,
			final Complex[][] G) {
		// get crossPowerSpectrum
		final Complex[][] S = crossPowerSpectrum(F, G);
			
		logger.fine("Cross Power Spectrum size:" + S.length + "," + S[0].length);
		// get POC as Inverse DFT of cross Power Spectrum
		logger.fine("compute band limited POC");
		final Complex[][] POC = (Complex[][]) transform(S, false);
		return POC;
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
	public List<Peak> getPeaks(final Complex[][] input) {

		List<Peak> peakList = new ArrayList<Peak>();

		// Complex peak = new Complex(0, 0);

		final int N = input.length;
		final int M = input[0].length;

		logger.fine("N=" + N + " M=" + M);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Peak peak = new Peak();
				Point2D coordinatesOfPeak = new Point2D.Double();
				coordinatesOfPeak.setLocation(i, j);
				Point2D relativeCoordinatesOfPeak = new Point2D.Double();
				relativeCoordinatesOfPeak.setLocation(i-N/2, j-M/2);
				
				double amplitude = amplitudeOf(input[i][j]);
				double phase = phaseOf(input[i][j]);
				peak.setPoint(coordinatesOfPeak);
				peak.setRelativePixel(relativeCoordinatesOfPeak);
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
		final int N = F.length;
		final int M = F[0].length;
		Complex[][] S = new Complex[N][M];
		final int O = G.length;
		final int P = G[0].length;

		logger.fine("N=" + N + " M=" + M + " O=" + O + " P=" + P);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				S[i][j] = F[i][j].multiply(G[i][j].conjugate());
				S[i][j] = S[i][j].divide(new Complex(S[i][j].abs(), 0));
			}
		}
		return S;
	}

	/**
	 * Compute FFT (or Inverse FFT) of input
	 * @param input
	 * @param forward true if FFT, false if inverse of FFT
	 * @return
	 */
	public Complex[][] transform(final Complex[][] input, final boolean forward) {
		// get size of array (take nearest power of 2 if necessary)
		final long N = nearestSuperiorPow2((long) input.length);
		final long M = nearestSuperiorPow2((long) input[0].length);
		

		System.out.println("N=" + N + " M=" + M + "(input size=" + input.length + ","
				+ input[0].length);

		// copy array into output
		// and complete with zero Complex if necessary
		// extended size
		Complex[][] output = new Complex[(int) N][(int) M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {				
				if ((i >= input.length) || (j >= input[0].length)) {
					output[i][j] = new Complex(0, 0);
				} else {
					output[i][j] = input[i][j];
				}

			}
		}

		final Complex normalizeFactor = new Complex (Math.sqrt(N * M),0); 
		// compute FFT
		output = (Complex[][]) FFT.mdfft(output, forward);
		if (!forward) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					output[i][j] = output[i][j].divide(normalizeFactor);
				}
			}
		}

		
		logger.fine("N=" + output.length + " M=" + output[0].length);
		return (!forward)?shiftOrigine(output,(int) (N/2),(int) (M/2)):output;
	}

	/**
	 * shift topleft origin to x,y
	 * @param input
	 * @param x
	 * @param y
	 * @return
	 */
	public Complex[][] shiftOrigine(final Complex[][] input, final int x,final int y){
		final int N=input.length;
		final int M=input[0].length;
		Complex[][] output = new Complex[N][M];
		for (int i=0;i<N;i++){
			for (int j=0;j<M;j++){
				output[i][j] = input[(i+x) %N][(j+y)%M];
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
