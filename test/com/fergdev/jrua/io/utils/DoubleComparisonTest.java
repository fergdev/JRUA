package nz.ac.massey.buto.io.utils;

import static org.junit.Assert.*;

import nz.ac.massey.buto.utils.Maths;

import org.junit.Test;

/**
 * Tests for the double comparison method.
 * 
 * @author fahewson
 *
 */
public class DoubleComparisonTest {

	@Test
	public void equalTest(){
		
		assertTrue(Maths.compare(1d, 1d) == 0);
		
		assertTrue(Maths.compare(Double.MAX_VALUE, Double.MAX_VALUE) == 0);
		
		assertTrue(Maths.compare(Double.MIN_VALUE, Double.MIN_VALUE) == 0);
		
		assertTrue(Maths.compare(0.00001, 0.00001) == 0);
		
		assertTrue(Maths.compare(0.00000000001, 0.00000000001) == 0);
		
		assertTrue(Maths.compare(0.00000000001, 0.000000000009) == 0);
	}
	
	@Test
	public void notEqualTest(){
		
		assertTrue(Maths.compare(1d, 2d) != 0);
		
		assertTrue(Maths.compare(Double.MAX_VALUE, Double.MIN_VALUE) != 0);
		
		assertTrue(Maths.compare(Double.MIN_VALUE, Double.MAX_VALUE) != 0);
		
		assertTrue(Maths.compare(0.00001, 0.00002) != 0);
		
		//assertTrue(DoubleComparison.compare(0.00000001, 0.00000000002) != 0);
		//assertTrue(DoubleComparison.compare(0.00000000001, 0.000000000009) != 0);
	}
	
	@Test
	public void lessThanTest(){
		assertTrue(Maths.compare(0.00000001, 0.00001) < 0);
		
		assertTrue(Maths.compare(Double.MIN_VALUE, Double.MAX_VALUE) < 0);
	
		assertTrue(Maths.compare(0.00001, 0.00002) < 0);
	}
	
	@Test
	public void greaterThanTest(){
		assertTrue(Maths.compare(0.00001, 0.000000001) > 0);
		
		assertTrue(Maths.compare(Double.MAX_VALUE, Double.MIN_VALUE) > 0);
		
		assertTrue(Maths.compare(0.00002, 0.00001) > 0);
	}
}
