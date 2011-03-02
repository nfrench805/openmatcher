/**
 * 
 */
package com.quantum.algorithms.fourier;

import junit.framework.TestCase;

import org.apache.commons.math.complex.Complex;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Pascal Dergane
 * 
 */
public class FourierTransformTest extends TestCase {
	private int size = 1024;
	/**
	 * define a complex serie
	 */
	private Complex[] f;
	/**
	 * define a real serie
	 */
	private Complex[] r;

	@Before
	public void setUp() throws Exception {
		f = new Complex[size];
		r = new Complex[size];

		for (int i = 0; i < size; i++) {
			f[i] = new Complex(Math.round(Math.random() * 100), Math.round(Math
					.random() * 100));

			r[i] = new Complex(Math.round(Math.random() * 100), 0);
		}

	}

	/**
	 * test that IDFT(DFT) = original signal
	 */
	@Test
	public void testReversibility() {
		Complex[] dft = FourierTransform.dft(f);
		Complex[] idft = FourierTransform.idft(dft);
		for (int i = 0; i < size; i++) {
			// System.out.println("" + i + " [" + Math.round(idft[i].getReal())
			// + ";" + Math.round(idft[i].getImaginary()) + "]" + " ["
			// + Math.round(f[i].getReal()) + ";"
			// + Math.round(f[i].getImaginary()) + "]");
			assertEquals(Math.round(Math.round(f[i].getReal())),
					Math.round(idft[i].getReal()));
			assertEquals(Math.round(f[i].getImaginary()),
					Math.round(idft[i].getImaginary()));
		}
	}

	/**
	 * test TFD of real signal X[n]=X[N-n].conjugate
	 */
	@Test
	public void testTFDofRealSignal() {
		Complex[] dft = FourierTransform.dft(r);

		for (int i = 1; i < size; i++) {
			// System.out.println("" + i + " [" + Math.round(dft[i].getReal())
			// + ";" + Math.round(dft[i].getImaginary()) + "]" + " ["
			// + Math.round(dft[size - i].getReal()) + ";"
			// + Math.round(dft[size - i].getImaginary()) + "]");

			assertEquals(Math.round(Math.round(dft[i].getReal())),
					Math.round(dft[size - i].getReal()));
			assertEquals(Math.round(dft[i].getImaginary()),
					-Math.round(dft[size - i].getImaginary()));
		}
	}

	/**
	 * check Parseval theorem sum (x*x) = 1/N * sum (X*X)
	 */
	public void testParseval() {
		Complex[] dft = FourierTransform.dft(f);

		double expected = 0;
		double phase = 0;
		for (int k = 0; k < size; k++) {
			final Complex x = f[k];
			final Complex X = dft[k];
			expected += x.abs() * x.abs();
			phase += X.abs() * X.abs();
		}		
		//System.out.println("expected=" + expected + " phase=" + phase/size);
		assertEquals(Math.round(expected),Math.round(phase/size));
	}
	
	/**
	 * check Plancherel theorem sum (x*x.conjugate) = 1/N * sum (X*X.conjugate)
	 */
	public void testPlancherel() {
		Complex[] dft = FourierTransform.dft(f);
		Complex[] dft2 = FourierTransform.dft(r);

		Complex expected = new Complex(0,0);
		Complex phase = new Complex(0,0);
		for (int k = 0; k < size; k++) {
			final Complex x = f[k];
			final Complex y = r[k];
			final Complex X = dft[k];
			final Complex Y = dft2[k];
			expected = expected.add(x.multiply(y.conjugate()));
			phase = phase.add(X.multiply(Y.conjugate()));
		}		
		//System.out.println("expected=" + expected + " phase=" + phase);
		assertEquals(Math.round(expected.getReal()),Math.round(phase.getReal()/size));
		assertEquals(Math.round(expected.getImaginary()),Math.round(phase.getImaginary()/size));
	}


}
