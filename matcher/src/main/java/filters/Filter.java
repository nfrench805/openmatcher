/**
 * 
 */
package filters;

import org.apache.commons.math.complex.Complex;

/**
 * @author Pascal Dergane define a high pass filter algorithm used 
 * 			for(y=0;y<fftH;y++) 
 * 				for (x=0;x<fftW;x++) { 
 * 					fcpx a=imgA.at(x,y); 
 * 					fcpx b=imgB.at(x,y); 
 * 					fcpx c=(a)*std::conj(b); 
 * 					/* complex multiply a by the complex conjugate of b
 * 
 *         			// Apply high-pass edge detecting filter 
 *         			int fx=x; 
 *         			if (fx>(fftW/2))fx-=fftW; 
 *         			int fy=y; 
 *         			if (fy>(fftH/2)) fy-=fftH; 
 *         			float r2=(fx*fx+fy*fy); // square of radius: discrete frequency bins if
 *         				(r2<128) c=0; // zero out low frequencies
 * 
 *         			imgC.at(x,y)=c; }
 * 
 */
public class Filter {

	/**
	 * apply a highPass filter on input
	 * 
	 * @param input
	 *            complex[][] array representing an image source
	 * @param threshold, double value representing limit of frequencies 
	 * we keep. If x^2+y^2 < threshold, then output[x][y] = 0	            
	 * @return Complex[][] image filtered
	 */
	public static Complex[][] applyHighPass(final Complex[][] input,final double threshold) {
		int N = input.length;
		int M = input[0].length;
		Complex[][] output = new Complex[N][M];
		for (int j = 0; j < M; j++) {
			for (int i = 0; i < N; i++) {
				double r2 = Math.pow((i-N/2),2) + Math.pow(j-M/2, 2);
				output[i][j] = (r2<threshold)? new Complex(0,0):input[i][j];
			}
		}
		return output;
	}
	
	/**
	 * apply a highPass filter on input
	 * 
	 * @param input
	 *            complex[][] array representing an image source
	 * @param threshold, double value representing limit of frequencies 
	 * we keep. If x^2+y^2 > threshold, then output[x][y] = 0	            
	 * @return Complex[][] image filtered
	 */
	public static Complex[][] applyLowPass(final Complex[][] input,final double threshold) {
		int N = input.length;
		int M = input[0].length;
		Complex[][] output = new Complex[N][M];
		for (int j = 0; j < M; j++) {
			for (int i = 0; i < N; i++) {
				double r2 = Math.pow((i-N/2),2) + Math.pow(j-M/2, 2);
				output[i][j] = (r2>threshold)? new Complex(0,0):input[i][j];
			}
		}
		return output;
	}

}
