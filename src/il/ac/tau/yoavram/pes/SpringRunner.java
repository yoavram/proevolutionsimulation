package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.common.io.Files;

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
			logger.error(e);
			System.exit(1);
		}
		System.exit(0);
	}

	public static void run(String[] args) throws IOException {
		System.out.println("Starting " + SpringRunner.class.getSimpleName());
		SimulationConfigurer configurer = new SimulationConfigurer();
		Date date = new Date();
		String dateString = TimeUtils.formatDate(date);
		configurer.configure(args, date);
		Properties properties = configurer.getProperties();
		String jobName = properties
				.getProperty(SimulationConfigurer.JOB_NAME_KEY);

		File jobConfigDir = new File(SimulationConfigurer.CONFIG_DIR_NAME
				+ File.separator + jobName);
		if (!jobConfigDir.exists()) {
			jobConfigDir.mkdir();
		}

		AbstractXmlApplicationContext context = new FileSystemXmlApplicationContext();

		logger.info("Adding properties to context: " + properties.toString());
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(properties);
		context.addBeanFactoryPostProcessor(propertyPlaceholderConfigurer);

		File propertiesFile = new File(jobConfigDir.getCanonicalPath()
				+ File.separator + jobName + '.' + dateString
				+ SimulationConfigurer.PROPERTIES_EXTENSION);
		logger.debug("Saving properties to file "
				+ propertiesFile.getCanonicalPath());
		properties.store(new FileOutputStream(propertiesFile),
				propertiesFile.getName());

		File contextFile = new File(jobConfigDir.getCanonicalFile()
				+ File.separator + jobName + '.' + dateString
				+ SimulationConfigurer.XML_EXTENSION);

		logger.info("Copying context file from "
				+ configurer.getSpringXmlFile().getCanonicalPath() + " to "
				+ contextFile.getCanonicalPath());
		Files.copy(configurer.getSpringXmlFile(), contextFile);

		logger.info("Loading context from file "
				+ contextFile.getCanonicalPath());
		context.setConfigLocation(contextFile.getPath());

		// make sure destroy methods will be called and refresh the context
		context.registerShutdownHook();
		context.refresh();

		Simulation simulation = context.getBean("simulation", Simulation.class);

		logger.debug("Starting simulation " + jobName);
		simulation.start();
		if (Boolean.parseBoolean(properties.getProperty("simulation.block"))) {
			System.out.println("Press any key to exit...");
			System.in.read();
		}
	}
}