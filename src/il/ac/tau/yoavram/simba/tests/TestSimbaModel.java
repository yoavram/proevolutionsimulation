package il.ac.tau.yoavram.simba.tests;

import static org.junit.Assert.*;
import il.ac.tau.yoavram.pes.io.Serialization;
import il.ac.tau.yoavram.simba.SimbaModel;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestSimbaModel {
	private static SimbaModel model;
	private static final String contextXmlFilename = "resources/simba.xml";
	private static final String serializationFilename = "tests/simba_serialization.xml";
	private static File file;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		file = new File(serializationFilename);
		ApplicationContext context = new FileSystemXmlApplicationContext(
				contextXmlFilename);
		model = context.getBean(SimbaModel.class);
	}

	@Test
	public void serializationTest() throws IOException {
		file.delete();
		assertFalse(file.exists());

		Serialization.writeToFile(model, serializationFilename);

		assertTrue(file.exists());
		assertTrue(file.getTotalSpace() > 0);
	}

	@Test
	public void deserializationTest() throws Exception {
		assertTrue(file.exists());
		assertTrue(file.canRead());

		SimbaModel deModel = Serialization.readFromFile(serializationFilename);

		assertNotNull(deModel);
		assertEquals(deModel.getTime(), model.getTime());
		assertEquals(deModel.getAncestor(), model.getAncestor());
		assertEquals(deModel.getEnvironment(), model.getEnvironment());
		assertEquals(deModel.getPopulations(), model.getPopulations());
	}
}
