/*
 *  proevolutionsimulation: an agent-based simulation framework for evolutionary biology
 *  Copyright 2010 Yoav Ram <yoavram@post.tau.ac.il>
 *
 *  This file is part of proevolutionsimulation.
 *
 *  proevolutionsimulation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License (GNU GPL v3) as published by
 *  the Free Software Foundation.
 *   
 *  proevolutionsimulation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. You are allowed to modify this code, link it with other code 
 *  and release it, as long as you keep the same license. 
 *  
 *  The content license is Creative Commons 3.0 BY-SA. 
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with proevolutionsimulation.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package il.ac.tau.yoavram.pes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class runs a {@link Simulation} that is configured using a Spring XML file from the classpath (usually the config folder).
 * @author yoavram
 * @version Alfred
 * 
 */
public abstract class SpringRunner {
	private static final Logger logger = Logger.getLogger(SpringRunner.class);

	/**
	 * @param args
	 *            see {@link PesCommandLineParser}
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
		SimulationConfigurer configurer = new SimulationConfigurer(args);
		if (configurer.getSpringXmlConfig() == null) {
			System.err.println("Spring XML config file not defined");
			System.err.println();
			System.exit(1);
		}
		if (configurer.getProperties() == null) {
			System.err.println("Properties not defined");
			System.err.println();
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
				+ configurer.getSpringXmlConfig().toString());

		context.setConfigLocation(configurer.getSpringXmlConfig().toString());

		// make sure destroy methods will be called and refresh the context
		context.registerShutdownHook();
		context.refresh();

		Simulation simulation = context.getBean("simulation", Simulation.class);

		logger.debug("Starting simulation " + jobName);
		simulation.start();
	}
}