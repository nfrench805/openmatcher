package com.quantum.matcher;

import java.awt.geom.Point2D;

public class Peak implements Comparable<Peak> {

	/**
	 * original coordinates (from topleft)
	 */
	private Point2D pixel;
	/**
	 * relative coordinates (from middle)
	 */
	private Point2D relativePixel;
	/**
	 * amplitude/magnitude at point (pixel)
	 */
	private double amplitude;
	/**
	 * phase at point (pixel)
	 */
	private double phase;

	public Peak() {
		// default constructor;
		pixel = new Point2D.Double(0, 0);
		amplitude = 0;
		phase = 0;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(double phase) {
		this.phase = phase;
	}

	/**
	 * @return the phase
	 */
	public double getPhase() {
		return phase;
	}

	/**
	 * @param amplitude
	 *            the amplitude to set
	 */
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	/**
	 * @return the amplitude
	 */
	public double getAmplitude() {
		return amplitude;
	}

	/**
	 * @param pixel
	 *            the pixel to set
	 */
	public void setPoint(Point2D point) {
		this.pixel = point;
	}

	/**
	 * @return the pixel
	 */
	public Point2D getPoint() {
		return pixel;
	}

	/**
	 * if this.amplitude is greater than amplitude of other then this object is
	 * greater than other.
	 */
	@Override
	public int compareTo(Peak o) {
		int ret=(this.amplitude>o.getAmplitude())?0:1;
		/*
		if (this.amplitude == o.getAmplitude()){
			ret =0;
		}
		*/
		return ret;
	}

	/**
	 * @param relativePixel the relativePixel to set
	 */
	public void setRelativePixel(Point2D relativePixel) {
		this.relativePixel = relativePixel;
	}

	/**
	 * @return the relativePixel
	 */
	public Point2D getRelativePixel() {
		return relativePixel;
	}

}
