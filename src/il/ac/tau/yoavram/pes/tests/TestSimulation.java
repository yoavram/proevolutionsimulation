package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimulation {
	ApplicationContext context;
	Test test;

	@org.junit.Before
	public void setUp() throws IOException {
		context = new ClassPathXmlApplicationContext("test_simulation.xml");
	}

	@org.junit.Test
	public void simpleTest() {
		assertTrue(test.isTest());
	}
}
