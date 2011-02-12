/**
 * 
 */
package com.quantum.algorithms.fourier;

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
public class MatcherTest  extends TestCase{
	
	private Matcher matcher= new Matcher();

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
	public void testPowerOf2(){
		assertFalse(matcher.isPowerOf2(0));
		assertFalse(matcher.isPowerOf2(10));
		assertTrue(matcher.isPowerOf2(8));
		assertFalse(matcher.isPowerOf2(922337203685477580L));
	}
	
	
	/**
	 * test match method
	 */
	@Test
	public void testMatch(){
		Complex[][] image1 = new Complex[8][8];
		Complex[][] image2 = new Complex[8][8];
		
		for (int i = 0;i<8;i++){
			for (int j=0;j<8;j++){
				image1[i][j] = new Complex (i,j);
				image2[i][j] = new Complex (i,j);
			}			
		}
				
		matcher.match(image1, image2);
	}

}
