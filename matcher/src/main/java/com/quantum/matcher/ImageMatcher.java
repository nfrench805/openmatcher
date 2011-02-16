/**
 * 
 */
package com.quantum.matcher;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math.complex.Complex;

import com.quantum.algorithms.fourier.Matcher;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcher {
	
	private Matcher matcher=new Matcher();

	public double match(final InputStream reference, final InputStream candidate)
			throws IOException {

		BufferedImage  imageReference = ImageIO.read(reference);
		BufferedImage  imageCandidate = ImageIO.read(candidate);
		
		int width = imageReference.getWidth();
		int height = imageReference.getHeight();
		
		Complex[][] imgRef = new Complex[width][height];
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				imgRef[x][y] = new Complex(imageReference.getRGB(x, y) & 0xff,0);
			}
		}
		
		width = imageCandidate.getWidth();
		height = imageCandidate.getHeight();
		Complex[][] imgCand = new Complex[width][height];
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				imgRef[x][y] = new Complex(imageCandidate.getRGB(x, y) & 0xff,0);
			}
		}
		
		return matcher.match(imgRef, imgCand);		
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