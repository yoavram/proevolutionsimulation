package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import il.ac.tau.yoavram.pes.utils.FixedSizedQueue;

import org.junit.Before;
import org.junit.Test;

public class TestFixedSizedQueue {

	FixedSizedQueue<Integer> queue;
	int limit = 3;

	@Before
	public void setUp() throws Exception {
		queue = new FixedSizedQueue<Integer>(limit);
	}

	@Test
	public void testAdd() {
		assertNull(queue.add(0));
		assertNull(queue.add(1));
		assertNull(queue.add(2));
		int ret = queue.add(3);
		assertEquals(0, ret);
	}

	@Test
	public void testRemove() {
		assertNull(queue.add(0));
		assertNull(queue.add(1));
		int ret = queue.remove();
		assertEquals(0, ret);
	}

	@Test
	public void testRandomRemove() {
		assertNull(queue.add(0));
		assertNull(queue.add(1));
		int ret = queue.randomRemove();
		if (ret == 0)
			ret = queue.randomRemove();
		assertEquals(1, ret);
	}

	@Test
	public void testRandom() {
		assertNull(queue.add(0));
		assertNull(queue.add(1));
		int count = 0;
		for (int i = 0; i < 1000; i++)
			count += queue.randomGet();
		assertTrue(count > 300 && count < 700);
		System.out.println(count);
	}

	@Test
	public void testSize() {
		assertEquals(0, queue.size());
		queue.add(0);
		assertEquals(1, queue.size());
		queue.add(1);
		assertEquals(2, queue.size());
		queue.add(2);
		assertEquals(3, queue.size());
		queue.add(3);
		assertEquals(3, queue.size());
	}

}
