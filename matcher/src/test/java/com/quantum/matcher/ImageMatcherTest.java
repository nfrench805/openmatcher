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
public class ImageMatcherTest extends TestCase {

	private ImageMatcher matcher = new ImageMatcher();
	//private ClassLoader loader = getClass().getClassLoader();
	private String refFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	private String candFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	// private InputStream reference = loader.getResourceAsStream(filename);
	

	/**
	 * test match 2 images
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMatch() throws IOException {
		InputStream reference = new BufferedInputStream(
				new FileInputStream(refFilename));
		
		InputStream candidate = new BufferedInputStream(
				new FileInputStream(candFilename));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score>0L);
	}

	/**
	 * test displayImage
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDisplay() throws IOException {
		InputStream reference = new BufferedInputStream(
				new FileInputStream(refFilename));
		matcher.displayImage(ImageIO.read(reference));
	}
}
