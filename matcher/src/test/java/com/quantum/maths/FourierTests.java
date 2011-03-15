/**
 * 
 */
package com.quantum.maths;

import junit.framework.TestCase;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




public class FourierTests extends TestCase {

	private FastFourierTransformer FFT = new FastFourierTransformer();
	private int N = 8192;
	private Complex[] input = new Complex[N];

	@Before
	public void setUp() throws Exception {
		for (int k = 0; k < N; k++) {
			input[k] = new Complex(Math.rint(Math.random() * 255),
					Math.rint(Math.random() * 255));
		}

	}

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
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransform() {
		long start = System.currentTimeMillis();
		Complex[] DFT = Fourier.transform(input, true);
		Complex[] IDFT = Fourier.transform(DFT, false);
		long end = System.currentTimeMillis();
		long elapsedTime = end-start;
		System.out.println("elapsed time(ms) ="+elapsedTime);
		
		long start1 = System.currentTimeMillis();
		Complex[] DFT1 = FFT.transform(input);
		Complex[] IDFT1 = FFT.inversetransform(DFT1);
		long end1 = System.currentTimeMillis();
		System.out.println("Apache FFT elapsed time(ms) ="+(end1 - start1));
		
		for (int k = 0; k < N; k++) {
			
			//System.out.println(k+" >"+input[k].getReal()+","+input[k].getImaginary());
			//System.out.println(k+" >"+IDFT[k].getReal()+","+IDFT[k].getImaginary());
			//System.out.println("------------------------------------------------------");
			assertEquals(Math.round(input[k].getReal()),
					Math.round(IDFT[k].getReal()));
			assertEquals(Math.round(input[k].getImaginary()),
					Math.round(IDFT[k].getImaginary()));

		}
	}

}