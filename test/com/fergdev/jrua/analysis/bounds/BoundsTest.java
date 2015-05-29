package nz.ac.massey.buto.analysis.bounds;

import static org.junit.Assert.*;
import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.bounds.BoundsCreationException;
import nz.ac.massey.buto.domain.ButoProperty;

import org.junit.Test;

/**
 * Unit tests for the bounds object.
 * 
 * @author Fergus Hewson
 *
 */
public class BoundsTest {

	/**
	 * Test data type object.
	 */
	private ButoProperty integerTestDataType = new ButoProperty("key","name","units","java.lang.Integer",0, 100);
	private ButoProperty longTestDataType = new ButoProperty("key","name","units","java.lang.Long",0l, 100l);
	private ButoProperty doubleTestDataType = new ButoProperty("key","name","units","java.lang.Double",10.5, 100.5);
	
	@Test
	public void simpleTest() throws BoundsCreationException{
		
		// Create bound
		Bounds bound = new Bounds(1000, 50, 75, integerTestDataType);
		
		// Test values
		assertEquals(1000, bound.timeStamp);
		assertEquals(50, bound.lowerBound);
		assertEquals(75, bound.upperBound);
	}
	
	
	@Test(expected=BoundsCreationException.class)
	public void testConstructorError() throws BoundsCreationException{
		
		// Should error, lower bound greater than upper bound
		new Bounds(1000, 100, 50, integerTestDataType);
	}
	
	@Test
	public void roundingTest() throws BoundsCreationException{
		
		// Integer rounding
		Bounds integerBound = new Bounds(1000, 50.8, 75.1, integerTestDataType);
		
		assertEquals(50, integerBound.lowerBound);
		assertEquals(76, integerBound.upperBound);
		
		// Long rounding
		Bounds longBound = new Bounds(1000, 50.8, 75.1, longTestDataType);
		assertEquals(50l, longBound.lowerBound);
		assertEquals(76l, longBound.upperBound);
		
		// Double rounding (no rounding)
		Bounds doubleBound = new Bounds(1000, 50.8, 75.1, doubleTestDataType);
		assertEquals(50.8, doubleBound.lowerBound);
		assertEquals(75.1, doubleBound.upperBound);
	}
	
	@Test
	public void dataTypeBoundsTest() throws BoundsCreationException{
		
		// Integer bounds
		Bounds integerBound = new Bounds(1000, -100, 108, integerTestDataType);
		
		assertEquals(0, integerBound.lowerBound);
		assertEquals(100, integerBound.upperBound);
		
		// Long bounds
		Bounds longBound = new Bounds(1000, -1000l, 9000l, longTestDataType);
		assertEquals(0l, longBound.lowerBound);
		assertEquals(100l, longBound.upperBound);
		
		// Double bounds
		Bounds doubleBound = new Bounds(1000, -80.546, 405.123, doubleTestDataType);
		assertEquals(10.5, doubleBound.lowerBound);
		assertEquals(100.5, doubleBound.upperBound);
		
	}
	
	@Test
	public void equalityTest() throws BoundsCreationException{
		
		Bounds bound1 = new Bounds(1000, 0, 50, integerTestDataType);
		Bounds bound2 = new Bounds(2000, 0, 50, integerTestDataType);
		Bounds bound3 = new Bounds(1000, 1, 50, integerTestDataType);
		Bounds bound4 = new Bounds(1000, 0, 60, integerTestDataType);
		
		assertTrue(bound1.equals(bound1));
		assertTrue(!bound1.equals(bound2));
		assertTrue(!bound1.equals(bound3));
		assertTrue(!bound1.equals(bound4));
	}
}
