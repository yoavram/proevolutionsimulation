package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;

/**
 * http://junit.sourceforge.net/doc/cookbook/cookbook.htm
 * 
 * @author yoavram
 * 
 */
public class Test {
	@org.junit.Before
	public void setUp() {
	}

	@org.junit.Test
	public void simpleTest() {
		assertTrue(true);
	}
	
	@org.junit.After
	public void teadDown() {

	}

	public boolean isTest() {
		return true;
	}
}
