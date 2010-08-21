package il.ac.tau.yoavram.pes;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class FileRunner {
	private static final String USAGE = "Usage: java "
			+ FileRunner.class.getSimpleName()
			+ " [spring xml configuration file path]";
	private static final Logger logger = Logger.getLogger(FileRunner.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("resources\\log4j.properties");
		if (args.length != 1) {
			logger.error(USAGE);
			throw new IllegalArgumentException(USAGE);
		}

		logger.info("Loading simulation from file " + args[0]);
		ApplicationContext context = new FileSystemXmlApplicationContext(
				args[0]);
		Simulation simulation = context.getBean("simulation", Simulation.class);

		logger.debug("Loaded simulation from file " + args[0]
				+ ", starting now");
		simulation.start();
	}
}