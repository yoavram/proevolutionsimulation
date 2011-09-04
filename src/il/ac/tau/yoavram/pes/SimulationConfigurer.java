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

import il.ac.tau.yoavram.pes.utils.DelayedLogger;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is used to configure a {@link Simulation}, using user files and
 * parameters.</br> Files and parameters are given as <code>String[] args</code>
 * , typically at commandline.</br> Then the configurer uses a command line
 * parser to parse the args, read parameters from properties file, initiate
 * loggers, etc.</br>
 * <p>
 * The complete syntax can be evaluated by running SpringRunner without any
 * arguments or calling this constructor with and empty string array.</br> As of
 * 31/03/2011, the syntax is:</br>
 * 
 * <pre>
 * usage: java SpringRunner [-f {file}] [-h] [-l {file}] [-p {property=value}] -x {file}
 *  -f,--pfile <file>                  properties filename
 *  -h,--help                          print this message
 *  -l,--log <file>                    log4j config filename
 *  -p,--properties <property=value>   properties (override properties file)
 *  -x,--xml <file>                    Spring XML configuration file
 * </pre>
 * 
 * 
 * @author yoavram
 * 
 */
public class SimulationConfigurer {
	private static final DelayedLogger logger = new DelayedLogger(
			Logger.getLogger(SimulationConfigurer.class));

	public static final String LOG4J_DEFAULT_CONFIG_FILE = "log4j.properties";
	public static final String JOB_NAME_KEY = "jobName";
	public static final String CONFIG_DIR_NAME = "config";
	public static final String XML_EXTENSION = ".xml";
	public static final String LOG_EXTENTION = ".log";
	public static final String PROPERTIES_EXTENSION = ".properties";
	public static final String EMPTY_STRING = "";
	public static final String TIME = "time";

	private URL springXmlConfig;
	private Properties properties;
	private String logFilename;

	public SimulationConfigurer(String[] args) throws IOException {
		this(args, new Date());
	}

	public SimulationConfigurer(String[] args, Date date) throws IOException {
		CommandLine cmdLine = PesCommandLineParser.parse(args);
		// TODO check stuff
		springXmlConfig = this
				.getClass()
				.getClassLoader()
				.getResource(
						cmdLine.getOptionValue(PesCommandLineParser.OptCode.Xml
								.toString()));
		properties = new Properties();
		if (cmdLine.hasOption(PesCommandLineParser.OptCode.FileProperties
				.toString())) {
			String filename = cmdLine
					.getOptionValue(PesCommandLineParser.OptCode.FileProperties
							.toString());
			properties = loadPropertiesFromClasspath(properties, filename);
		}
		if (cmdLine.hasOption(PesCommandLineParser.OptCode.Properties
				.toString())) {
			Properties cmdLineProps = cmdLine
					.getOptionProperties(PesCommandLineParser.OptCode.Properties
							.toString());
			properties.putAll(cmdLineProps);
			logger.info("Command line properties given: "
					+ cmdLineProps.toString());
		}

		String log4jConfigFilename = null;
		if (cmdLine.hasOption(PesCommandLineParser.OptCode.Log.toString())) {
			log4jConfigFilename = cmdLine
					.getOptionValue(PesCommandLineParser.OptCode.Log.toString());
		} else {
			log4jConfigFilename = LOG4J_DEFAULT_CONFIG_FILE;
		}

		Properties log4jProps = loadPropertiesFromClasspath(log4jConfigFilename);

		if (!properties.containsKey(JOB_NAME_KEY)) {
			String fullpath = springXmlConfig.getFile();
			String[] parts = fullpath.split("/");
			String lastPart = parts[parts.length - 1];
			String filename = lastPart.replace(XML_EXTENSION, EMPTY_STRING);
			properties.setProperty(JOB_NAME_KEY, filename);
		}
		if (!properties.containsKey(TIME)) {
			properties.setProperty(TIME, TimeUtils.formatDate(date));
		}

		logFilename = properties.getProperty("log.dir") + File.separator
				+ properties.getProperty(JOB_NAME_KEY) + File.separator
				+ properties.getProperty(JOB_NAME_KEY) + '.'
				+ properties.getProperty(TIME) + LOG_EXTENTION;
		log4jProps.setProperty("log4j.appender.FILE.File", logFilename);
		logger.info("Logging to file " + logFilename);
		PropertyConfigurator.configure(log4jProps);
		logger.info("Finished configuring simulation");
	}

	private Properties loadPropertiesFromClasspath(String filename)
			throws IOException {
		return loadPropertiesFromClasspath(new Properties(), filename);
	}

	private Properties loadPropertiesFromClasspath(Properties props,
			String filename) throws IOException {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		props.load(stream);
		logger.info("Loaded properties from file " + filename + ": "
				+ props.toString());
		return props;
	}

	public Properties getProperties() {
		return properties;
	}

	public String getLogFilename() {
		return logFilename;
	}

	public URL getSpringXmlConfig() {
		return springXmlConfig;
	}
}
