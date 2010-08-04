package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * http://static.springsource.org/spring/docs/3.0.x/spring-framework-
 * reference/html/>reference
 * 
 * @author yoavram
 * 
 */
public class TestSpring {
	ApplicationContext context;
	Test test;

	@org.junit.Before
	public void setUp() throws IOException {
		context = new ClassPathXmlApplicationContext("test_spring.xml");
		test = context.getBean("test", Test.class);
	}

	@org.junit.Test
	public void simpleTest() {
		assertTrue(test.isTest());
	}
}
