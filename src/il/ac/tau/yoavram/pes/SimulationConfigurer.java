package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SimulationConfigurer {
	public static final String LOG4J_DEFAULT_CONFIG_FILE = "log4j.properties";

	// private static final Logger logger = Logger
	// .getLogger(SimulationConfigurer.class);

	public static final String JOB_NAME_KEY = "jobName";
	public static final String CONFIG_DIR_NAME = "config";
	public static final String XML_EXTENSION = ".xml";
	public static final String LOG_EXTENTION = ".log";
	public static final String PROPERTIES_EXTENSION = ".properties";
	public static final String EMPTY_STRING = "";
	public static final String TIME = "time";

	private CommandLineParser parser;
	private Options options;
	private HelpFormatter helpFormatter;
	private Properties properties = null;
	private CommandLine cmdLine = null;
	private URL springXmlUrl = null;
	private String logFilename = null;

	public enum OptCode {
		Help, Xml, Properties, FileProperties, Log;
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1).toLowerCase();
		};
	};

	@SuppressWarnings("static-access")
	public SimulationConfigurer() {
		parser = new GnuParser();
		options = new Options();
		Option xml = OptionBuilder.withArgName("file").hasArg()
				.isRequired(true).withLongOpt("xml")
				.withDescription("Spring XML configuration file")
				.create(OptCode.Xml.toString());
		Option log = OptionBuilder.withArgName("file").hasArg()
				.withLongOpt("log").withDescription("log4j config filename")
				.create(OptCode.Log.toString());
		Option pfile = OptionBuilder.withArgName("file").hasArg()
				.withLongOpt("pfile").withDescription("properties filename")
				.create(OptCode.FileProperties.toString());
		Option properties = OptionBuilder.withArgName("property=value")
				.hasArgs(2).withValueSeparator()
				.withDescription("properties (override properties file)")
				.withLongOpt("properties")
				.create(OptCode.Properties.toString());
		Option help = new Option(OptCode.Help.toString(), "help", false,
				"print this message");

		options.addOption(xml).addOption(pfile).addOption(log)
				.addOption(properties).addOption(help);
	}

	public void configure(String[] args, Date date) {
		cmdLine = parseArgs(args);
		if (cmdLine == null)
			return;
		properties = createProperties();
		springXmlUrl = createSpringXmlUrl();
		Properties log4jProps = createLog4jProperties();

		if (!properties.containsKey(JOB_NAME_KEY)) {
			String fullpath = springXmlUrl.getFile();
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

	private URL createSpringXmlUrl() {
		return this.getClass().getClassLoader()
				.getResource(cmdLine.getOptionValue(OptCode.Xml.toString()));

	}

	private Properties createLog4jProperties() {
		String filename = null;
		if (cmdLine.hasOption(OptCode.Log.toString())) {
			filename = cmdLine.getOptionValue(OptCode.Log.toString());
		} else {
			filename = LOG4J_DEFAULT_CONFIG_FILE;
		}
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		if (stream == null) {
			String msg = "Could not stream the properties file " + filename
					+ " in the classpath";
			System.err.println(msg);
			throw new NullPointerException(msg);
		}
		Properties props = new Properties();
		try {
			props.load(stream);
		} catch (FileNotFoundException e) {
			System.err.println("File " + filename + " not found: " + e);
		} catch (IOException e) {
			System.err.println("Error loading " + filename + ": " + e);
		}
		System.out.println("Loaded Log4j properties from file " + filename
				+ ": " + props.toString());
		return props;
	}

	private CommandLine parseArgs(String[] args) {
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			printHelp();
		}
		return cmd;
	}

	private Properties createProperties() {
		Properties props = new Properties();
		if (cmdLine.hasOption(OptCode.FileProperties.toString())) {
			String filename = cmdLine.getOptionValue(OptCode.FileProperties
					.toString());
			InputStream stream = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			if (stream == null) {
				String msg = "Could not stream the properties file " + filename
						+ " in the classpath";
				System.err.println(msg);
				throw new NullPointerException(msg);
			}
			try {

				props.load(stream);
			} catch (FileNotFoundException e) {
				System.err.println("File " + filename + " not found: " + e);
			} catch (IOException e) {
				System.err.println("Error loading " + filename + ": " + e);
			}
			System.out.println("Loaded properties from file " + filename + ": "
					+ props.toString());
		}
		if (cmdLine.hasOption(OptCode.Properties.toString())) {
			Properties cmdLineProps = cmdLine
					.getOptionProperties(OptCode.Properties.toString());
			props.putAll(cmdLineProps);
			System.out.println("Command line properties given: "
					+ cmdLineProps.toString());
		}
		return props;
	}

	public void printHelp() {
		getHelpFormatter().printHelp(
				"java " + SpringRunner.class.getSimpleName(), options, true);

	}

	private HelpFormatter getHelpFormatter() {
		if (helpFormatter == null) {
			helpFormatter = new HelpFormatter();
		}
		return helpFormatter;
	}

	public Properties getProperties() {
		return properties;
	}

	public String getLogFilename() {
		return logFilename;
	}

	public URL getSpringXmlUrl() {
		return springXmlUrl;
	}
}
