/**
 * 
 */
package com.quantum.maths;

import org.apache.commons.math.complex.Complex;

/**
 * @author Pascal Dergane
 * 
 */
public class Polar {

	/**
	 * transform Rectangular 2D coordinates to Polar Coordinates
	 * 
	 * @param input
	 * @return
	 */
	public static Complex[][] transformTo(Complex[][] input) {
		int N = input.length;
		int M = input[0].length;

		Complex[][] output = new Complex[N][M];

		int n0 = (N % 2 == 0) ? N / 2 : (N - 1) / 2;
		int m0 = (M % 2 == 0) ? M / 2 : (M - 1) / 2;

		//double rMax = Math.sqrt(n0 * n0 + m0 * m0);
		double deltaTheta = Math.PI * 2 / N;
		double base = Math.pow(10, Math.log10(N) / N);

		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				double r = Math.pow(base, i)-1;
				double theta = j * deltaTheta;

				//get largest closest int coordinates
				int x = (int) Math.round(r * Math.cos(theta) + m0);
				int y = (int) Math.round(r * Math.sin(theta) + n0);
				if (x >= 0 && y >= 0 && x < N && y < M) {
					output[j][i] = input[x][y];
				} else {
					output[j][i] = new Complex(0, 0);
				}
			}
		}

		return output;
	}
}
