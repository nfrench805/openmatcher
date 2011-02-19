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
	private String candFilename2 = "C:/workspace/matcher/src/test/resources/data/1/001_1_2.bmp";
	private String candFilename3 = "C:/workspace/matcher/src/test/resources/data/1/001_1_3.bmp";
	private String candFilename4 = "C:/workspace/matcher/src/test/resources/data/F_24_11.gif";
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
	
	@Test
	public void testMatch2() throws IOException {
		InputStream reference = new BufferedInputStream(
				new FileInputStream(refFilename));
		
		InputStream candidate = new BufferedInputStream(
				new FileInputStream(candFilename2));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score>0L);
	}
	
	@Test
	public void testMatch3() throws IOException {
		InputStream reference = new BufferedInputStream(
				new FileInputStream(refFilename));
		
		InputStream candidate = new BufferedInputStream(
				new FileInputStream(candFilename3));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score>0L);
	}
	
	@Test
	public void testNoMatch() throws IOException {
		InputStream reference = new BufferedInputStream(
				new FileInputStream(refFilename));
		
		InputStream candidate = new BufferedInputStream(
				new FileInputStream(candFilename4));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score<0.99);
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
