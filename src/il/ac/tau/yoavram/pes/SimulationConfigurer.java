package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.PropertyConfigurator;

public class SimulationConfigurer {
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

	public SimulationConfigurer(String[] args) {
		this(args, new Date());
	}

	public SimulationConfigurer(String[] args, Date date) {
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
			System.out.println("Command line properties given: "
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

		logFilename = properties.getProperty(JOB_NAME_KEY) + '.'
				+ properties.getProperty(TIME) + LOG_EXTENTION;
		log4jProps.setProperty("log4j.appender.FILE.File", logFilename);
		System.out.println("Logging to file " + logFilename);
		PropertyConfigurator.configure(log4jProps);
	}

	private Properties loadPropertiesFromClasspath(String filename) {
		return loadPropertiesFromClasspath(new Properties(), filename);
	}

	private Properties loadPropertiesFromClasspath(Properties props,
			String filename) {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		try {
			props.load(stream);
		} catch (FileNotFoundException e) {
			System.err.println("File " + filename + " not found: " + e);
		} catch (IOException e) {
			System.err.println("Error loading " + filename + ": " + e);
		}
		System.out.println("Loaded properties from file " + filename + ": "
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
