package com.quantum.algorithms.fourier;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.apache.commons.math.complex.Complex;



public class MatcherUnit<Template> {
		
	
	public void set(Template matcherInstance){
		matcher.setMatcherUnit(matcherInstance);
	}
	
	/**
	 * instance of template IMatcher
	 */
	public GenericMatcher<Template> matcher =new GenericMatcher<Template>();
	/**
	 * logger
	 */
	protected Logger logger = Logger.getLogger(MatcherUnit.class.getName());
	
	/**
	 * main method computing a score (probability that candidate image matches
	 * against reference image
	 * 
	 * @param reference
	 * @param candidate
	 * @return
	 */
	public double match(double[][] reference, double[][] candidate) {
		int N1 = reference.length;
		int N2 = reference[0].length;

		logger.info("width=" + N1 + " height=" + N2);
		// we suppose images with same size
		logger.info("computing DFT of reference...");
		Complex[][] F = matcher.get2D_DFT(reference);
		logger.info("computing DFT of candidate...");
		Complex[][] G = matcher.get2D_DFT(candidate);
		logger.info("computing cross phase spectrum...");
		Complex[][] R = matcher.getCrossPhaseSpectrum(F, G);
		logger.info("computing POC...");
		Complex[][] POC = matcher.get2D_IDFT(R, N1, N2);
		logger.info("looking for a peak in POC...");
		Point2D peak = matcher.getPeak(POC);
		Complex score = POC[(int) peak.getX()][(int) peak.getY()];
		return score.getReal();
	}
	
	
	/**
	 * 
	 * @param reference
	 * @param candidate
	 * @return
	 */
	public double matchLogPolar(final double[][] reference, final double[][] candidate){
		int N1 = reference.length;
		int N2 = reference[0].length;

		logger.info("width=" + N1 + " height=" + N2);
		// we suppose images with same size
		logger.info("computing DFT of reference...");
		Complex[][] F = matcher.get2D_DFT(reference);
		logger.info("computing DFT of candidate...");
		Complex[][] G = matcher.get2D_DFT(candidate);
		logger.info("computing cross phase spectrum...");
		Complex[][] R = matcher.getCrossPhaseSpectrum(CoordinateConverter.toLogPolar(F), CoordinateConverter.toLogPolar(G));
		logger.info("computing POC...");
		Complex[][] POC = matcher.get2D_IDFT(R, N1, N2);
		logger.info("looking for a peak in POC...");
		Point2D peak = matcher.getPeak(POC);
		Complex score = POC[(int) peak.getX()][(int) peak.getY()];
		return score.getReal();
	}
	

}
