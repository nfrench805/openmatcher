/**
 * 
 */
package filters;

import java.util.logging.Logger;

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
	
	private static Logger logger= Logger.getLogger(Filter.class.getName());

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
		logger.info("Applying HighPass Filter on FFT...");
		int N = input.length;
		int M = input[0].length;
		Complex[][] output = new Complex[N][M];
		for (int j = 0; j < M; j++) {
			double Y = Math.cos(Math.PI*(j-M/2)/M);
			for (int i = 0; i < N; i++) {
				logger.fine("i="+i+" j="+j);
				//double r2 = Math.pow((i-N/2),2) + Math.pow(j-M/2, 2);
				double X = Math.cos(Math.PI*(i-N/2)/N)*Y;
				double H = (1-X)*(2-X);
				output[i][j] = input[i][j].multiply(H);				
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
