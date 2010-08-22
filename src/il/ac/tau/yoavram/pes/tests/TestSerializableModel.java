package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;
import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.SerilizableModel;
import il.ac.tau.yoavram.pes.io.Serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class TestSerializableModel {
	private static SerilizableModel<Integer> model;
	private static String filename;

	@org.junit.BeforeClass
	public static void setUpBeforeClass() {
		filename = "tests/test_model_serialization";
		model = new MockModel();
		model.setTime(new Date());
		List<Integer> pop = Ints.asList(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,
				9, 10 });
		List<List<Integer>> popList = Lists.newArrayList();
		popList.add(pop);
		model.setPopulations(popList);
	}

	@org.junit.Test
	public void serializationTest() throws IOException {
		File file = new File(filename);
		assertTrue(!file.exists() || file.delete());

		Serialization.writeToFile(model, filename);

		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertTrue(file.getTotalSpace() > 0);
	}

	@org.junit.Test
	public void deserializationTest() throws IOException,
			ClassNotFoundException {
		Model<Integer> deModel = Serialization.readFromFile(filename);

		assertTrue(deModel != null);
		assertTrue(deModel.getTime() == model.getTime());
		assertTrue(deModel.getPopulations().equals(model.getPopulations()));
	}

	public static class MockModel extends SerilizableModel<Integer> {
		private static final long serialVersionUID = 1L;

		private Date time;

		private List<List<Integer>> popList;

		@Override
		public void step() {
		}

		@Override
		public void setTime(Date time) {
			// this.time = time;
		}

		@Override
		public void setPopulations(List<List<Integer>> populations) {
			this.popList = populations;
		}

		@Override
		public void init() {
		}

		@Override
		public Date getTime() {
			return time;
		}

		@Override
		public List<List<Integer>> getPopulations() {
			return popList;
		}
	};
}
