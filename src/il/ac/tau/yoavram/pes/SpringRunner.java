package il.ac.tau.yoavram.pes;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * TODO bacth runner with args changing properties
 * @author Yoav
 *
 */
public abstract class SpringRunner {
	private static final String USAGE = "Usage: java "
			+ SpringRunner.class.getSimpleName()
			+ " [spring xml configuration file path]";
	private static final Logger logger = Logger.getLogger(SpringRunner.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			logger.error(USAGE);
			throw new IllegalArgumentException(USAGE);
		}
		run(args[0]);
	}

	public static void run(String springConfigurationFilename) {
		logger.info("Loading simulation from file "
				+ springConfigurationFilename);
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				springConfigurationFilename);
		context.registerShutdownHook();
		Simulation simulation = context.getBean("simulation", Simulation.class);

		logger.debug("Loaded simulation from file "
				+ springConfigurationFilename + ", starting now");
		simulation.start();
	}

}