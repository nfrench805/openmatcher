/**
 * 
 */
package com.quantum.matcher;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcher {

	public double match(final InputStream reference, final InputStream candidate)
			throws IOException {

		Image imageReference = ImageIO.read(reference);
		Image imageCandidate = ImageIO.read(candidate);

		displayImage(imageReference);
		displayImage(imageCandidate);
		
		return 0;
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
