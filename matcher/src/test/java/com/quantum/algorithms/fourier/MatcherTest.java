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

	@Test
	public void testNearestPowerOf2() {
		assertEquals(2, matcher.nearestSuperiorPow2(0));
		assertEquals(2, matcher.nearestSuperiorPow2(2));
		assertEquals(8, matcher.nearestSuperiorPow2(7));
		assertEquals(1024, matcher.nearestSuperiorPow2(999));

	}
	
	
	@Test 
	public void testMatch(){
		double[][] f= new double[8][8];
		double[][] g= new double[8][8];
		
		//create a random image
		//f and g are same
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				f[i][j]=(Math.random()*255);
				g[i][j] = f[i][j];
			}
		}
		
		double score = matcher.match(f, g);
		
		assertNotSame(0L,score);
	}
	
	@Test 
	public void testNoMatch(){
		double[][] f= new double[8][8];
		double[][] g= new double[8][8];
		
		//create a random image
		//f and g are different
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				f[i][j]=(Math.random()*255);
				g[i][j] = 0;
			}
		}
		
		double score = matcher.match(f, g);
		
		assertEquals(0L,score);
	}
}
