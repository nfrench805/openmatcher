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

	private IMatcher matcher = new FFTMatcher();
	private Logger logger = Logger.getLogger(MatcherTest.class.getName());
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

	/**
	 * check method isPowerOf2
	 */
	@Test
	public void testPowerOf2() {
		assertFalse(((GenericMatcher)matcher).isPowerOf2(0));
		assertFalse(((GenericMatcher)matcher).isPowerOf2(10));
		assertTrue(((GenericMatcher)matcher).isPowerOf2(8));
		assertFalse(((GenericMatcher)matcher).isPowerOf2(922337203685477580L));
	}

	@Test
	public void testNearestPowerOf2() {
		assertEquals(2, ((GenericMatcher)matcher).nearestSuperiorPow2(0));
		assertEquals(2, ((GenericMatcher)matcher).nearestSuperiorPow2(2));
		assertEquals(8, ((GenericMatcher)matcher).nearestSuperiorPow2(7));
		assertEquals(1024, ((GenericMatcher)matcher).nearestSuperiorPow2(999));

	}
	
	
	@Test 
	public void testMatch(){
		double[][] f= new double[size][size];
		double[][] g= new double[size][size];
		
		//create a random image
		//f and g are same
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				f[i][j]=(Math.random()*255);
				g[i][j] = f[i][j];
			}
		}
		
		double score = matcher.match(f, g);
		logger.info("score=" + score);
		assertTrue(score>0.3);
	}
	
	@Test 
	public void testNoMatch(){
		double[][] f= new double[size][size];
		double[][] g= new double[size][size];
		
		//create a random image
		//f and g are different
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				f[i][j]=(Math.random()*255);
				g[i][j] = (Math.random()*255);
			}
		}
		
		double score = matcher.match(f, g);
		logger.info("score=" + score);
		assertTrue(0.3>score);
	}
	
	@Test 
	public void testMatchTranslation(){
		double[][] f= new double[size][size];
		double[][] g= new double[size][size];
		
		//create a random image
		//f and g are same
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				f[i][j]=(Math.random()*255);				
			}
		}
		
		int deltaX=size/4;
		int deltaY=size/4;
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				g[i][j]=f[(i+deltaX)%size][(j+deltaY)%size];				
			}
		}
		
		double score = matcher.match(f, g);
		logger.info("score=" + score);
		assertTrue(score>0.3);
	}
}
