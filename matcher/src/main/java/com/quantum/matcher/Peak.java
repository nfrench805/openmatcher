package com.quantum.matcher;

import java.awt.geom.Point2D;

public class Peak implements Comparable<Peak> {

	private Point2D point;
	private double amplitude;
	private double phase;

	public Peak() {
		// default constructor;
		point = new Point2D.Double(0, 0);
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
	 * @param point
	 *            the point to set
	 */
	public void setPoint(Point2D point) {
		this.point = point;
	}

	/**
	 * @return the point
	 */
	public Point2D getPoint() {
		return point;
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

}
