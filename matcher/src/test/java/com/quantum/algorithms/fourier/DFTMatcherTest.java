package com.quantum.algorithms.fourier;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DFTMatcherTest extends TestCase {

	private IMatcher matcher = new DFTMatcher();
	private Logger logger = Logger.getLogger(FFTMatcherTest.class.getName());
	private final int size = 512;

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

	@Test
	public void testMatch() {
		double[][] f = new double[size][size];
		double[][] g = new double[size][size];

		// create a random image
		// f and g are same
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				f[i][j] = (Math.random() * 255);
				g[i][j] = f[i][j];
			}
		}

		double score = matcher.match(f, g);

		logger.info("score=" + score);
		assertTrue(score > 0.9);

	}

	@Test
	public void testNoMatch() {
		double[][] f = new double[size][size];
		double[][] g = new double[size][size];

		// create a random image
		// f and g are different
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				f[i][j] = (Math.random() * 255);
				g[i][j] = (Math.random() * 255);
			}
		}

		double score = matcher.match(f, g);
		logger.info("score=" + score);
		assertTrue(0.3 > score);

	}

	@Test
	public void testMatchTranslation() {
		double[][] f = new double[size][size];
		double[][] g = new double[size][size];

		// create a random image
		// f and g are same
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				f[i][j] = (Math.random() * 255);
			}
		}

		int deltaX = size / 4;
		int deltaY = size / 4;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				g[i][j] = f[(i + deltaX) % size][(j + deltaY) % size];
			}
		}

		double score = matcher.match(f, g);
		logger.info("score=" + score);
		assertTrue(score > 0.9);

	}

}
