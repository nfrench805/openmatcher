/**
 * 
 */
package com.quantum.matcher;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.apache.commons.math.complex.Complex;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcherTest extends TestCase {

	private ImageMatcher matcher = new ImageMatcher();
	// private ClassLoader loader = getClass().getClassLoader();
	private String refFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	private String candFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	private String candFilename2 = "C:/workspace/matcher/src/test/resources/data/1/001_1_2.bmp";
	private String candFilename3 = "C:/workspace/matcher/src/test/resources/data/1/001_1_3.bmp";
	private String candFilename4 = "C:/workspace/matcher/src/test/resources/data/F_24_11.gif";
	private String finger1 = "C:/workspace/matcher/src/test/resources/data/ProcessedSample1.bmp";
	private String finger2 = "C:/workspace/matcher/src/test/resources/data/ProcessedSample2.bmp";

	private final String FOLDER = "C:/workspace/matcher/src/test/resources/data/";
	private String book_ref = FOLDER + "book_ref.jpg";
	private String book_clip = FOLDER + "book_clip.jpg";
	private String book_left = FOLDER + "book_left.jpeg";
	private String book_right = FOLDER + "book_right.jpeg";
	private String book_obscured = FOLDER + "book_obscured.jpeg";
	private String book_unrated = FOLDER + "book_unralated.jpeg";
	private int p = 2;

	public Complex[][] input;
	public Complex[][] inputBrightness;
	public Complex[][] inputOffset;
	public Complex[][] inputRotation;
	public Complex[][] inputWhiteNoise;
	public Complex[][] inputScaled;
	public Complex[][] inputBeforeScaled;

	final double theta = Math.PI * Math.random();// angle in radian
	final double brightnessScale = Math.random() * 0.5;

	final int N = 128;
	final int M = 128;

	final int xOffset = (int) ((Math.random()) * 10);
	final int yOffset = (int) ((Math.random()) * 10);
	final double scaleFactor = 2;

	@Before
	public void setUp() throws Exception {
		input = new Complex[N][M];
		inputOffset = new Complex[N][M];
		inputRotation = new Complex[N][M];
		inputBrightness = new Complex[N][M];
		inputWhiteNoise = new Complex[N][M];
		inputScaled = new Complex[N][M];
		inputBeforeScaled=new Complex[N][M];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				inputScaled[i][j] = new Complex(0,0); 				
			}
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				inputBeforeScaled[i][j] = new Complex(255, 0);							
				inputScaled[i][j] = new Complex(255,0);		
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {				
				input[i][j] = new Complex(Math.random() * 255, Math.random()
						* Math.PI * 2);
				inputOffset[(i + xOffset) % N][(j + yOffset) % M] = input[i][j];
				inputBrightness[i][j] = input[i][j].multiply(brightnessScale);
				inputWhiteNoise[i][j] = input[i][j].add(new Complex((Math
						.random() - 0.5) * 100, 0));				
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				double r2 = Math.pow((i - N / 2), 2) + Math.pow((j - M / 2), 2);
				double angle = Math.atan2(j - M / 2, i - N / 2);
				int x = (int) (N / 2 + Math.sqrt(r2) * Math.cos(angle - theta));
				int y = (int) (M / 2 + Math.sqrt(r2) * Math.sin(angle - theta));
				if (x < 0) {
					x = 0;
				}
				if (y < 0) {
					y = 0;
				}

				inputRotation[i][j] = input[x % N][y % M];
			}
		}

	}

	// private InputStream reference = loader.getResourceAsStream(filename);

	private Logger logger = Logger.getLogger(ImageMatcherTest.class.getName());

	public void testNearPowerOf2() {
		assertEquals(512, matcher.nearestSuperiorPow2(511));
		assertEquals(512, matcher.nearestSuperiorPow2(512));

	}

	/**
	 * test match same image
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMatchSameImage() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename));

		assertNotNull(reference);

		MatchingScore score = matcher.match(reference, candidate, p);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());

		assertTrue(score.getScore() > 0.9);

	}

	// @Test
	// public void testMatch2() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// refFilename));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// candFilename2));
	//
	// assertNotNull(reference);
	//
	// double score = matcher.match(reference, candidate,p);
	//
	// assertTrue(score > 0.9);
	//
	// }
	//
	// @Test
	// public void testMatch3() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// refFilename));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// candFilename3));
	//
	// assertNotNull(reference);
	//
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score > 0.9);
	//
	// }

	@Test
	public void testNoMatch() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename4));

		assertNotNull(reference);

		MatchingScore score = matcher.match(reference, candidate, p);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() < 0.9);

	}

	// @Test
	// public void testBookClip() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// this.book_ref));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// this.book_clip));
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score > 0.9);
	// }
	//
	// @Test
	// public void testBookLeft() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// this.book_ref));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// this.book_left));
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score > 0.9);
	// }
	//
	// @Test
	// public void testBookRight() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// this.book_ref));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// this.book_right));
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score > 0.9);
	// }
	//
	// @Test
	// public void testBookObscured() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// this.book_ref));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// this.book_obscured));
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score > 0.9);
	// }
	//
	// @Test
	// public void testBookUnrated() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// this.book_ref));
	//
	// InputStream candidate = new BufferedInputStream(new FileInputStream(
	// this.book_unrated));
	// double score = matcher.match(reference, candidate,p);
	// assertTrue(score < 0.9);
	// }

	/**
	 * test displayImage
	 * 
	 * @throws IOException
	 */
	// @Test
	// public void testDisplay() throws IOException {
	// InputStream reference = new BufferedInputStream(new FileInputStream(
	// refFilename));
	// matcher.displayImage(ImageIO.read(reference));
	// }

	/**
	 * test matching between input and input
	 */
	@Test
	public void testMatchSameInput() {
		MatchingScore score = matcher.match(input, input, p);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() > 0.9);
	}

	/**
	 * test matching between input and shift(input)
	 */
	@Test
	public void testMatchSameInputWithOffset() {
		MatchingScore score = matcher.match(input, inputOffset, p);
		logger.info("Rotation estimated=" + score.getRotation());
		assertTrue(score.getScore() > 0.9);
		assertEquals(xOffset, (int) (-score.getHorizontal_shift()));
		assertEquals(yOffset, (int) (-score.getVertical_shift()));

	}

	/**
	 * test matching between input and rotation(input)
	 */
	@Test
	public void testMatchSameInputWithRotation() {
		MatchingScore score = matcher.match(input, inputRotation, p);
		logger.info("Rotation angle=" + theta);

		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() > 0.9);
	}

	/**
	 * test matching between input and input+brightness
	 */
	@Test
	public void testMatchSameInputWithBrightness() {
		MatchingScore score = matcher.match(input, inputBrightness, p);
		logger.info("brightness scale=" + this.brightnessScale);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() > 0.9);
	}

	/**
	 * test matching between input and input+white noise
	 */
	@Test
	public void testMatchSameInputWithWhiteNoise() {
		MatchingScore score = matcher.match(input, inputWhiteNoise, p);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() > 0.7);
	}

	/**
	 * test matching between input and inputScaled
	 */
//	@Test
//	public void testMatchSameInputScaled() {
//		MatchingScore score = matcher.match(inputBeforeScaled, inputScaled, p);
//		logger.info("Rotation estimated=" + score.getRotation());
//		logger.info("horizontal shift=" + score.getHorizontal_shift());
//		logger.info("vertical shift=" + score.getVertical_shift());
//		assertTrue(score.getScore() > 0.7);
//	}

	/**
	 * match 2 similar but not same fingers
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMatchDistinctFingers() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				finger1));
		InputStream candidate = new BufferedInputStream(new FileInputStream(
				finger2));
		assertNotNull(reference);
		MatchingScore score = matcher.match(reference, candidate, p);
		logger.info("Rotation estimated=" + score.getRotation());
		logger.info("horizontal shift=" + score.getHorizontal_shift());
		logger.info("vertical shift=" + score.getVertical_shift());
		assertTrue(score.getScore() < 0.5);
	}
}
