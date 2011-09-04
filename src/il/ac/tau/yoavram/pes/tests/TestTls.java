package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestTls {
	private static ThreadLocal<Object> tls = new ThreadLocal<Object>();

	@Test
	public void testNull() {
		assertNull(tls.get());
	}

	@Test
	public void testSetGet() {
		tls.set(new Object());
		assertNotNull(tls.get());
	}

	@Test
	public void testRemove() {
		assertNotNull(tls.get());
		tls.remove();
		assertNull(tls.get());
	}

}
