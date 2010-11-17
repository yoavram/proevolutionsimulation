package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.ClassFilter;
import il.ac.tau.yoavram.pes.filters.ClassNameFilter;
import il.ac.tau.yoavram.pes.terminators.ExtinctionTerminator;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestExtinctionTerminator {

	ExtinctionTerminator<Number> terminator;
	Model<Number> model;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		terminator = new ExtinctionTerminator<Number>();
		List<Number> numbers = Lists.newArrayList();
		numbers.add(Integer.MAX_VALUE);
		numbers.add(Double.MAX_EXPONENT);
		numbers.add(Long.MAX_VALUE);
		List<List<Number>> list = Lists.newArrayList();
		list.add(numbers);
		model = mock(Model.class);
		when(model.getPopulations()).thenReturn(list);

		terminator.setModel(model);
	}

	@Test
	public void testClazz() {
		terminator.setFilter(new ClassFilter<Number>(Integer.class));
		assertFalse(terminator.terminate());

		terminator.setFilter(new ClassFilter<Number>(Byte.class));
		assertTrue(terminator.terminate());
	}

	@Test
	public void testClazzName() {
		terminator.setFilter(new ClassNameFilter<Number>("Integer"));
		assertFalse(terminator.terminate());

		terminator.setFilter(new ClassNameFilter<Number>("Byte"));
		assertTrue(terminator.terminate());

	}
}
