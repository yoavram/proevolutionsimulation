package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import junit.framework.Assert;
import il.ac.tau.yoavram.pes.statistics.listeners.MongoListener;
import il.ac.tau.yoavram.pes.utils.SpringUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMongoListener {
	private static final String configFilename = "test_mongo_listener.xml";

	private static MongoListener mongo;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mongo = SpringUtils.getContext(configFilename).getBean(
				MongoListener.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mongo.close();
	}

	@Test
	public void testInit() {
		Assert.assertNotNull(mongo);

	}

	@Test
	public void testClose() {
		try {
			mongo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testListen() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDataFieldNames() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDb() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDb() {
		fail("Not yet implemented");
	}

}
