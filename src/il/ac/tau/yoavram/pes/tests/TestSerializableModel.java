package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;
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
	private static String filenameWithDate;

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

		assertTrue(deModel != null);
		assertTrue(deModel.equals(model));
		assertTrue(deModel.getPopulations().equals(model.getPopulations()));
	}

	@org.junit.Test
	public void serializeTest() {
		// since filename already has extension:
		model.setExtension(null);
		filenameWithDate = model.serialize();

		File file = new File(filenameWithDate);
		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertTrue(file.getTotalSpace() > 0);
	}

	@org.junit.Test
	public void deserializeTest() {
		SerializableModel<Integer> deModel = SerializableModel
				.deserialize(filenameWithDate);

		assertTrue(deModel != null);
		assertTrue(deModel.equals(model));
		assertTrue(deModel.getPopulations().equals(model.getPopulations()));
		assertTrue(new File(filenameWithDate).delete());
	}

	@org.junit.Test
	public void springDeserializeTest() {
		File file = new File(filename);
		assertTrue(file.exists());

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"test_deserialization.xml");
		MockModel deModel = context.getBean("model", MockModel.class);

		assertTrue(deModel != null);
		assertTrue(deModel.equals(model));
		assertTrue(deModel.getPopulations().equals(model.getPopulations()));

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
