/**
 * 
 */
package com.quantum.algorithms.fourier;

import java.util.Vector;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.complex.Complex;

/**
 * @author Pascal Dergane
 * 
 */
public class MultiDimensionalComplexMatrix {

	 /** Message for dimension mismatch. */
    private static final String DIMENSION_MISMATCH_MESSAGE =
        "some dimensions don't match: {0} != {1}";
    
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
			dimensionSize.add(numOfDimensions, array.length);
			lastDimension = array[0];
		}
	}
	
	/**
     * Get the size in all dimensions.
     * @return size in all dimensions
     */
    public Integer[] getDimensionSizes() {    	
        return (Integer[]) dimensionSize.toArray();
    }

    /**
     * Get the underlying storage array
     * @return underlying storage array
     */
    public Object getArray() {
        return multiDimensionalMatrixArray;
    }
    
    /**
     * return element (coordinates represented by Vector)
     * of multiDimensionalMatrixArray
     * 
     * for example
     * multiDimensionalMatrixArray = Complex[][]
     * getElement(1,3) return Complex[1][3]
     * @param Vector
     * @return Complex value 
     */
    public Complex getElement(int... coordinates){
    	if (null == coordinates){
    		return null;
    	}
    	if (coordinates.length != dimensionSize.size()) {
            throw MathRuntimeException.createIllegalArgumentException(
                    DIMENSION_MISMATCH_MESSAGE, coordinates.length, dimensionSize.size());
        }
    	
    	Object lastDimension = multiDimensionalMatrixArray;

        for (int i = 0; i < dimensionSize.size(); i++) {
            lastDimension = ((Object[]) lastDimension)[coordinates[i]];
        }
        
        return (Complex) lastDimension;
    }
}
