/**
 * 
 */
package com.quantum.matcher;

/**
 * @author Pascal
 *
 */
public class MatchingScore {
	/**
	 * similarity estimation (value between 0 and 1.0)
	 * 0.0 is not similar
	 * 1.0 it's same image
	 * 
	 */
	private double score=0.0;
	/**
	 * rotation angle in radian between both images
	 */
	private double rotation=0.0;
	
	/**
	 * horizontal shift between both images
	 */
	private double horizontal_shift =0.0;
	
	/**
	 * vertical shift between both images
	 */
	private double vertical_shift =0.0;

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param horizontal_shift the horizontal_shift to set
	 */
	public void setHorizontal_shift(double horizontal_shift) {
		this.horizontal_shift = horizontal_shift;
	}

	/**
	 * @return the horizontal_shift
	 */
	public double getHorizontal_shift() {
		return horizontal_shift;
	}

	/**
	 * @param vertical_shift the vertical_shift to set
	 */
	public void setVertical_shift(double vertical_shift) {
		this.vertical_shift = vertical_shift;
	}

	/**
	 * @return the vertical_shift
	 */
	public double getVertical_shift() {
		return vertical_shift;
	}
	
}
