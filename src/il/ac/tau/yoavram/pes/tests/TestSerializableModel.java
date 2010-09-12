package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.*;
import il.ac.tau.yoavram.pes.SerializableModel;
import il.ac.tau.yoavram.pes.io.Serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class TestSerializableModel {
	private static SerializableModel<Integer> model;
	private static String filename;

	@org.junit.BeforeClass
	public static void setUpBeforeClass() {
		filename = "tests/test_model_serialization.ser";
		model = new MockModel();
		model.setID(new Date());
		List<Integer> pop = Ints.asList(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,
				9, 10 });
		List<List<Integer>> popList = Lists.newArrayList();
		popList.add(pop);
		model.setPopulations(popList);
		model.setFilename(filename);
	}

	@org.junit.Test
	public void writeToFileTest() throws IOException {
		File file = new File(filename);
		assertTrue(!file.exists() || file.delete());

		Serialization.writeToFile(model, filename);

		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertTrue(file.getTotalSpace() > 0);
	}

	@org.junit.Test
	public void readFromFileTest() throws IOException, ClassNotFoundException {
		SerializableModel<Integer> deModel = Serialization
				.readFromFile(filename);

		assertNotNull(deModel);
		assertEquals(deModel, model);
		assertEquals(deModel.getPopulations(), model.getPopulations());
	}

	@org.junit.Test
	public void serializeTest() {
		filename = model.serialize();
		assertNotNull(filename);

		File file = new File(filename);
		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertTrue(file.getTotalSpace() > 0);
	}

	@org.junit.Test
	public void deserializeTest() {
		SerializableModel<Integer> deModel = SerializableModel
				.deserialize(filename);

		assertNotNull(deModel);
		assertEquals(deModel, model);
		assertEquals(deModel.getPopulations(), model.getPopulations());
	}

	@org.junit.Test
	public void springDeserializeTest() {
		File file = new File(filename);
		assertTrue(file.exists());

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"test_deserialization.xml");
		MockModel deModel = context.getBean("model", MockModel.class);

		assertNotNull(deModel);
		assertEquals(deModel, model);
		assertEquals(deModel.getPopulations(), model.getPopulations());
	}
	
	@org.junit.Test
	public void springDeserializeAndSetParameterTest() {
		File file = new File(filename);
		assertTrue(file.exists());

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"test_deserialization.xml");
		MockModel deModel = context.getBean("model2", MockModel.class);

		assertNotNull(deModel);
		assertFalse(deModel.equals(model));
		assertEquals(deModel.getPopulations(), model.getPopulations());
	}

	public static class MockModel extends SerializableModel<Integer> {
		private static final long serialVersionUID = 1L;

		private List<List<Integer>> popList;

		@Override
		public void step() {
		}

		@Override
		public void setPopulations(List<List<Integer>> populations) {
			this.popList = populations;
		}

		@Override
		public void init() {
		}

		@Override
		public List<List<Integer>> getPopulations() {
			return popList;
		}
	};
}
