package il.ac.tau.yoavram.pes.tests;

import il.ac.tau.yoavram.pes.statistics.listeners.SqlListener;
import il.ac.tau.yoavram.pes.utils.SpringUtils;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestSqlListener {

	private SqlListener l;

	@Before
	public void setUp() throws Exception {
		l = SpringUtils.getContext("test_sql_listener.xml").getBean(
				SqlListener.class);
		l.setDataFieldNames(Lists.newArrayList("ticks", "fitness"));
	}

	@Test
	public void testListen() {
		l.listen(new Number[] { 1, 0.7 });

		l.listen(new Number[] { 2, 0.7558 });
		l.listen(new Number[] { 180000000, 0.0122510022058975 });

	}

}
