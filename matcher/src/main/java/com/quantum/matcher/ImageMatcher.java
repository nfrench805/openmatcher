/**
 * 
 */
package com.quantum.matcher;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math.complex.Complex;

import com.quantum.algorithms.fourier.IMatcher;
import com.quantum.algorithms.fourier.FFTMatcher;
import com.quantum.algorithms.fourier.MatcherUnit;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcher {
	
	//private IMatcher matcher=new FFTMatcher();
	
	private MatcherUnit<FFTMatcher> matcher= new MatcherUnit<FFTMatcher>();
	
	public ImageMatcher(){
		matcher.set(new FFTMatcher());
	}

	public double match(final InputStream reference, final InputStream candidate)
			throws IOException {

		BufferedImage  imageReference = ImageIO.read(reference);
		BufferedImage  imageCandidate = ImageIO.read(candidate);
		
		int width = imageReference.getWidth();
		int height = imageReference.getHeight();
		
		double[][] imgRef = new double[width][height];
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				int pix=(int) imageCandidate.getRGB(x, y);
				int alpha=(pix >> 24) & 0x0ff;
				int r=(pix>>16)& 0x0ff;
				int g=(pix>>8)& 0x0ff;
				int b=pix & 0x0ff;
				imgRef[x][y] =  (r+g+b)/3.0;
			}
		}
		
		width = imageCandidate.getWidth();
		height = imageCandidate.getHeight();
		double[][] imgCand = new double[width][height];
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				int pix=(int) imageCandidate.getRGB(x, y);
				int alpha=(pix >> 24) & 0x0ff;
				int r=(pix>>16)& 0x0ff;
				int g=(pix>>8)& 0x0ff;
				int b=pix & 0x0ff;
				imgCand[x][y] =  (r+g+b)/3.0;
			}
		}		
		return matcher.match(imgRef, imgCand);		
	}
	
	/**
	 * create an image from a double array
	 * @param img
	 * @param width
	 * @return
	 */
	public static BufferedImage CreateImageFromMatrix(double[] img, int
			width) {
			       int[] grayImage = new int[img.length];
			       double[] scales = (double[])img.clone();
			       Arrays.sort(scales);
			       double min = scales[0];
			       double max = scales[scales.length - 1];
			        for(int i = 0; i < grayImage.length; i++) {
			           double v = img[i];
			           v -= min;
			           v /= (max - min);
			           short val = (short)(v * 255);
			            grayImage[i] = (val << 16) | (val << 8) | (val);
			        }
			        BufferedImage bi = new BufferedImage(width, img.length / width,
			BufferedImage.TYPE_INT_RGB);
			        bi.setRGB(0, 0, width, img.length / width, grayImage, 0, width);
			        return bi;
			    }
	
	/**
	 * display an image
	 * @param image
	 */
	public void displayImage(final Image image){
		JFrame frame = new JFrame();
	    JLabel label = new JLabel(new ImageIcon(image));
	    frame.getContentPane().add(label, BorderLayout.CENTER);
	    frame.pack();
	    frame.setVisible(true);
	}

}
