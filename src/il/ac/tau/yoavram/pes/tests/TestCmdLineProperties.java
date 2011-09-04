package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCmdLineProperties {

	@Test
	public void simpleTest() throws FileNotFoundException, IOException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"cmdline_properties_test.xml");

		PropertyPlaceholderConfigurer p = new PropertyPlaceholderConfigurer();
		Properties props = new Properties();
		props.load(new FileInputStream("tests/test.properties"));
		assertTrue(props.get("test").equals("test"));

		p.setProperties(props);
		ctx.addBeanFactoryPostProcessor(p);
		ctx.refresh();

		String testBean = ctx.getBean("test", String.class);
		assertTrue(testBean.equals("test"));
	}
}
