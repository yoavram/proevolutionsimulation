package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.SimBacteria;

import org.junit.Test;

public class TestTls {
	private static ThreadLocal<Object> tls=new ThreadLocal<Object>();

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
	
	@Test 
	public void testSimBacteriaOverride() {
		Bacteria b = new Bacteria();
		b.die();
		SimBacteria s = new SimBacteria();
		s.die();
		assertEquals(Bacteria.class, b.spawn().getClass());
		assertEquals(SimBacteria.class, s.spawn().getClass());
		
	}
}
