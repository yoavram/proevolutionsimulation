package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;
import il.ac.tau.yoavram.pes.params.FileParamEngine;

import org.apache.commons.configuration.ConfigurationException;

/*TODO: document, add failure tests (null returns etc.)*/
public class TestFileParamsEngine {

	private static final String TEST_FILENAME = "tests/test.properties";
	private FileParamEngine paramEngine = null;

	@org.junit.Before
	public void setUp() throws ConfigurationException {
		paramEngine = new FileParamEngine();
		paramEngine.setFilename(TEST_FILENAME);
		paramEngine.init();
	}

	@org.junit.Test
	public void simpleTest() {
		String test = paramEngine.getParam("test");
		assertTrue(test.equals("test"));

		int howMany = paramEngine.getParam("howMany");
		assertTrue(howMany == 20);

		double whatFraction = paramEngine.getParam("whatFraction");
		assertTrue(whatFraction == 0.5);

		boolean isTest = paramEngine.getParam("isTest");
		assertTrue(isTest);
	}

	@org.junit.After
	public void teadDown() {
	}
}
