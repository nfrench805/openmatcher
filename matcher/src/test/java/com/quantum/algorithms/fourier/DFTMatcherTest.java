package com.quantum.algorithms.fourier;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DFTMatcherTest extends TestCase {

	private MatcherUnit<DFTMatcher> matcher = new MatcherUnit<DFTMatcher>();
	private Logger logger = Logger.getLogger(FFTMatcherTest.class.getName());
	private final int size = 32;

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
		matcher.set(new DFTMatcher());
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

	@Test
	public void testMatchRotation() {

		double[][] f = new double[size][size];
		double[][] g = new double[size][size];

		// get a random angle between 0 and PI/2
		double theta0 = Math.random() * Math.PI/30;//maximum 6°
		

		// define center of rotation
		int x0 = size / 2;
		int y0 = size / 2;

		// create a random image
		// f and g are same
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				f[i][j] = (Math.random() * 255);
			}
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int deltaX = i - x0;
				int deltaY = j - y0;
				double ro = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				double theta = Math.atan2(deltaY, deltaX);

				int x = (int) ( x0+ ro * Math.cos(theta - theta0));
				int y = (int) ( y0 + ro * Math.sin(theta - theta0));
				
				logger.info("x="+x+" y="+y+ " ("+x0+","+y0+")");
				if (x<0) x=0;
				if (y<0) y=0;
				if (y>size-1) y= size-1;
				if (x>size-1) x= size-1;
				
				g[i][j] = f[x][y ];
			}
		}

		double score = matcher.match(f, g);
		logger.info("score=" + score);
		logger.info("rotation defined for test is:" + theta0 / Math.PI * 180
				+ "°");
		assertTrue(score>0.1);

	}

}
