package il.ac.tau.yoavram.pes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Yoav
 * 
 */
public abstract class SpringRunner {
	private static final Logger logger = Logger.getLogger(SpringRunner.class);

	/**
	 * @param args
	 *            see {@link SimulationConfigurer}
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		try {
			run(args);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		System.exit(0);
	}

	public static void run(String[] args) throws IOException {
		System.out.println("Starting " + SpringRunner.class.getSimpleName());
		SimulationConfigurer configurer = new SimulationConfigurer();
		configurer.configure(args, new Date());
		if (configurer.getSpringXmlUrl() == null) {
			System.err.println("Spring XML file not defined");
			System.err.println();
			System.exit(1);
		}
		if (configurer.getProperties() == null) {
			System.err.println("Properties not defined");
			System.exit(1);
		}

		Properties properties = configurer.getProperties();
		String jobName = properties
				.getProperty(SimulationConfigurer.JOB_NAME_KEY);

		AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext();

		logger.info("Adding properties to context: " + properties.toString());
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(properties);
		context.addBeanFactoryPostProcessor(propertyPlaceholderConfigurer);

		logger.info("Loading context from file "
				+ configurer.getSpringXmlUrl().toString());

		context.setConfigLocation(configurer.getSpringXmlUrl().toString());

		// make sure destroy methods will be called and refresh the context
		context.registerShutdownHook();
		context.refresh();

		Simulation simulation = context.getBean("simulation", Simulation.class);

		logger.debug("Starting simulation " + jobName);
		simulation.start();
		if (Boolean.parseBoolean(properties.getProperty("simulation.block",
				"false"))) {
			System.out.println("Press any key to exit...");
			System.in.read();
		}
	}
}