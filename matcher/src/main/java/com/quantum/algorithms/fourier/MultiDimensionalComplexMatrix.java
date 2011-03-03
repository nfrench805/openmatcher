/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.lang.reflect.Array;
import java.util.Vector;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.complex.Complex;

/**
 * @author Pascal Dergane
 * 
 */
public class MultiDimensionalComplexMatrix implements Cloneable {

	/** Message for dimension mismatch. */
	private static final String DIMENSION_MISMATCH_MESSAGE = "some dimensions don't match: {0} != {1}";

	/**
	 * array of multiDimensions
	 */
	private Object multiDimensionalMatrixArray = null;
	/**
	 * size of each Dimension
	 */
	private Vector<Integer> dimensionSize = new Vector<Integer>();

	public MultiDimensionalComplexMatrix(final Object matrix) {
		this.multiDimensionalMatrixArray = matrix;

		// count dimensions
		int numOfDimensions = 0;
		for (Object lastDimension = multiDimensionalMatrixArray; lastDimension instanceof Object[];) {
			final Object[] array = (Object[]) lastDimension;
			numOfDimensions++;
			dimensionSize.add(array.length);
			lastDimension = array[0];
		}
	}

	/**
	 * Get the size in all dimensions.
	 * 
	 * @return size in all dimensions
	 */
	public Integer[] getDimensionSizes() {
		Object[] outputObject = dimensionSize.toArray();
		Integer[] output = new Integer[outputObject.length];
		for (int i=0;i<outputObject.length;i++){
			output[i] = (Integer) outputObject[i];			
		}
		return output;
	}

	/**
	 * Get the underlying storage array
	 * 
	 * @return underlying storage array
	 */
	public Object getArray() {
		return multiDimensionalMatrixArray;
	}

	/**
	 * return element (coordinates represented by Vector) of
	 * multiDimensionalMatrixArray
	 * 
	 * for example multiDimensionalMatrixArray = Complex[][] getElement(1,3)
	 * return Complex[1][3]
	 * 
	 * @param Vector
	 * @return Complex value
	 */
	public Complex getElement(int... coordinates) {
		if (null == coordinates) {
			return null;
		}
		if (coordinates.length != dimensionSize.size()) {
			throw MathRuntimeException.createIllegalArgumentException(
					DIMENSION_MISMATCH_MESSAGE, coordinates.length,
					dimensionSize.size());
		}

		Object lastDimension = multiDimensionalMatrixArray;

		for (int i = 0; i < dimensionSize.size(); i++) {
			lastDimension = ((Object[]) lastDimension)[coordinates[i]];
		}

		return (Complex) lastDimension;
	}

	/** {@inheritDoc} */
	@Override
	public Object clone() {
		MultiDimensionalComplexMatrix mdcm = new MultiDimensionalComplexMatrix(
				Array.newInstance(Complex.class, this.getDimensionSizes().length));
		clone(mdcm);
		return mdcm;
	}

	/**
	 * Copy contents of current array into mdcm.
	 * 
	 * @param mdcm
	 *            array where to copy data
	 */
	private void clone(MultiDimensionalComplexMatrix mdcm) {
		int dimensionSizes = dimensionSize.size();
		int[] vector = new int[dimensionSizes];
		int size = 1;
		for (int i = 0; i < dimensionSizes; i++) {
			size *= dimensionSize.elementAt(i);
		}
		int[][] vectorList = new int[size][dimensionSizes];
		for (int[] nextVector : vectorList) {
			System.arraycopy(vector, 0, nextVector, 0, dimensionSizes);
			for (int i = 0; i < dimensionSizes; i++) {
				vector[i]++;
				if (vector[i] < dimensionSize.elementAt(i)) {
					break;
				} else {
					vector[i] = 0;
				}
			}
		}

		for (int[] nextVector : vectorList) {
			mdcm.set(getElement(nextVector), nextVector);
		}
	}

	/**
	 * Set a matrix element.
	 * 
	 * @param magnitude
	 *            magnitude of the element
	 * @param vector
	 *            indices of the element
	 * @return the previous value
	 * @exception IllegalArgumentException
	 *                if dimensions do not match
	 */
	public Complex set(Complex magnitude, int... vector)
			throws IllegalArgumentException {
		int dimensionSizes = this.getDimensionSizes().length;
		if (vector == null) {
			if (dimensionSizes > 0) {
				throw MathRuntimeException.createIllegalArgumentException(
						DIMENSION_MISMATCH_MESSAGE, 0, dimensionSizes);
			}
			return null;
		}
		if (vector.length != dimensionSizes) {
			throw MathRuntimeException.createIllegalArgumentException(
					DIMENSION_MISMATCH_MESSAGE, vector.length, dimensionSizes);
		}

		Object[] lastDimension = (Object[]) multiDimensionalMatrixArray;
		for (int i = 0; i < dimensionSizes - 1; i++) {
			lastDimension = (Object[]) lastDimension[vector[i]];
		}

		Complex lastValue = (Complex) lastDimension[vector[dimensionSizes - 1]];
		lastDimension[vector[dimensionSizes - 1]] = magnitude;

		return lastValue;
	}

}
