/**
 * 
 */
package com.quantum.matcher;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * @author Pascal Dergane
 *
 */
public class ImageMatcherTest extends TestCase{

	private ImageMatcher matcher= new ImageMatcher();
	private ClassLoader loader = getClass().getClassLoader();
	private String filename ="/001_1_1.bmp";
	
	/**
	 * test match 2 images
	 * @throws IOException 
	 */
	@Test
	public void testMatch() throws IOException{
		
		InputStream reference = loader.getResourceAsStream(filename);
		
		assertNotNull(reference);
		
		double score= matcher.match(reference, reference);
		
		assertNotSame(0L, score);
	}
	
	/**
	 * test displayImage
	 * @throws IOException
	 */
	@Test
	public void testDisplay() throws IOException{
		
		InputStream reference = new BufferedInputStream(
	            new FileInputStream(filename));
		
		matcher.displayImage(ImageIO.read(reference));
				
	}
}
