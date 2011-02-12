/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.math.complex.Complex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Pascal Dergane
 * 
 */
public class MatcherTest extends TestCase {

	private Matcher matcher = new Matcher();
	private Logger logger = Logger.getLogger(MatcherTest.class.getName());

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * check method isPowerOf2
	 */
	@Test
	public void testPowerOf2() {
		assertFalse(matcher.isPowerOf2(0));
		assertFalse(matcher.isPowerOf2(10));
		assertTrue(matcher.isPowerOf2(8));
		assertFalse(matcher.isPowerOf2(922337203685477580L));
	}

	/**
	 * test match method 2 images (same values) expect a high Score
	 */
	@Test
	public void testMatch() {
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image1[i][j] = new Complex(i % 2, 0);
				image2[i][j] = new Complex(i % 2, 0);
			}
		}

		int score = matcher.match(image1, image2);
		logger.info("Score computed = " + score);
		assertNotSame(0, score);
	}

	/**
	 * test match method 2 images (different values) expect a lower Score
	 */
	@Test
	public void testMatchWithDifferentImages() {
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image1[i][j] = new Complex(i % 2, 0);
				image2[i][j] = new Complex(0, 0);
			}
		}

		int score = matcher.match(image1, image2);
		logger.info("Score computed = " + score);
		assertEquals(0, score);
	}

	/**
	 * test match method 2 images (same values with horizontal translation)
	 * expect a high Score
	 */
	@Test
	public void testMatchWithSameImagesHTranslated() {
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image1[i][j] = new Complex(i % 2, 0);
			}
		}

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image2[i][j] = image1[(i + 1) % 8][j];
			}
		}

		int score = matcher.match(image1, image2);
		logger.info("Score computed = " + score);
		assertNotSame(0, score);
	}

	/**
	 * test match method 2 images (same values with vertical translation) expect
	 * a high Score
	 */
	@Test
	public void testMatchWithSameImagesVTranslated() {
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image1[i][j] = new Complex(i % 2, 0);
			}
		}

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				image2[i][j] = image1[i][(j + 1) % 8];
			}
		}

		int score = matcher.match(image1, image2);
		logger.info("Score computed = " + score);
		assertNotSame(0, score);
	}

	/**
	 * test match method 2 images (same values with 90� rotation) expect a high
	 * Score
	 */
	@Test
	public void testMatchWithSameImagesRotation() {
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (i == 3 || i == 4) {
					image1[i][j] = new Complex(1, 0);
				} else {
					image1[i][j] = new Complex(0, 0);
				}
			}
		}

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (j == 3 || j == 4) {
					image2[i][j] = new Complex(1, 0);
				} else {
					image2[i][j] = new Complex(0, 0);
				}
			}
		}

		int score = matcher.match(image1, image2);
		logger.info("Score computed = " + score);
		assertNotSame(0, score);
	}

}
