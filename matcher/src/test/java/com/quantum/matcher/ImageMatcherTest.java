/**
 * 
 */
package com.quantum.matcher;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Pascal Dergane
 * 
 */
public class ImageMatcherTest extends TestCase {

	private ImageMatcher matcher = new ImageMatcher();
	// private ClassLoader loader = getClass().getClassLoader();
	private String refFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	private String candFilename = "C:/workspace/matcher/src/test/resources/data/1/001_1_1.bmp";
	private String candFilename2 = "C:/workspace/matcher/src/test/resources/data/1/001_1_2.bmp";
	private String candFilename3 = "C:/workspace/matcher/src/test/resources/data/1/001_1_3.bmp";
	private String candFilename4 = "C:/workspace/matcher/src/test/resources/data/F_24_11.gif";
	
	private final String FOLDER = "C:/workspace/matcher/src/test/resources/data/";
	private String book_ref = FOLDER + "book_ref.jpg";
	private String book_clip = FOLDER + "book_clip.jpg";
	private String book_left = FOLDER + "book_left.jpeg";
	private String book_right = FOLDER + "book_right.jpeg";
	private String book_rotation5 = FOLDER + "book_rotation5degree.jpeg";
	private String book_rotation15 = FOLDER + "book_rotation15degree.jpeg";
	private String book_obscured = FOLDER + "book_obscured.jpeg";
	private String book_unrated = FOLDER + "book_unralated.jpeg";
	
	
	
	// private InputStream reference = loader.getResourceAsStream(filename);

	private Logger logger = Logger.getLogger(ImageMatcherTest.class.getName());

	public void testNearPowerOf2() {
		assertEquals(512, matcher.nearestSuperiorPow2(511));
		assertEquals(512, matcher.nearestSuperiorPow2(512));

	}

	/**
	 * test match same image
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMatchSameImage() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score > 0.9);

	}

	@Test
	public void testMatch2() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename2));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);

		assertTrue(score > 0.9);

	}

	@Test
	public void testMatch3() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename3));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);
		assertTrue(score > 0.9);

	}

	@Test
	public void testNoMatch() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				candFilename4));

		assertNotNull(reference);

		double score = matcher.match(reference, candidate);
		assertTrue(score < 0.9);

	}
	
	@Test
	public void testBookClip() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				this.book_ref));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				this.book_clip));
		double score = matcher.match(reference, candidate);
		assertTrue(score > 0.9);
	}
	
	@Test
	public void testBookLeft() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				this.book_ref));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				this.book_left));
		double score = matcher.match(reference, candidate);
		assertTrue(score > 0.9);
	}
	
	@Test
	public void testBookRight() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				this.book_ref));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				this.book_right));
		double score = matcher.match(reference, candidate);
		assertTrue(score > 0.9);
	}
	
	@Test
	public void testBookObscured() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				this.book_ref));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				this.book_obscured));
		double score = matcher.match(reference, candidate);
		assertTrue(score > 0.9);
	}
	
	@Test
	public void testBookUnrated() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				this.book_ref));

		InputStream candidate = new BufferedInputStream(new FileInputStream(
				this.book_unrated));
		double score = matcher.match(reference, candidate);
		assertTrue(score < 0.9);
	}

	/**
	 * test displayImage
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDisplay() throws IOException {
		InputStream reference = new BufferedInputStream(new FileInputStream(
				refFilename));
		matcher.displayImage(ImageIO.read(reference));
	}
}

