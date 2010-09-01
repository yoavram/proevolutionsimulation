package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

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

	private static final Logger logger = Logger
			.getLogger(SimulationConfigurer.class);

	public static final String JOB_NAME_KEY = "jobName";
	public static final String CONFIG_DIR_NAME = "config";
	public static final String XML_EXTENSION = ".xml";
	public static final String PROPERTIES_EXTENSION = ".properties";
	public static final String EMPTY_STRING = "";
	public static final String TIME = "time";

	private CommandLineParser parser;
	private Options options;
	private HelpFormatter helpFormatter;
	private Properties properties = null;
	private CommandLine cmdLine = null;
	private URL log4jUrl;
	private URL springXmlUrl;

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
		log4jUrl = createLog4jUrl();
		if (log4jUrl == null) {
			System.err.println("No Log4j properties file found");
		} else {
			PropertyConfigurator.configure(log4jUrl);
		}
		// TODO change log file name
		properties = createProperties();
		springXmlUrl = createSpringXmlUrl();

		if (!properties.containsKey(JOB_NAME_KEY)) {
			String fullpath = springXmlUrl.getFile();
			String[] parts = fullpath.split("/");
			String lastPart = parts[parts.length - 1];
			String filename = lastPart.replace(XML_EXTENSION, EMPTY_STRING);
			properties.put(JOB_NAME_KEY, filename);
		}
		if (!properties.containsKey(TIME)) {
			properties.put(TIME, TimeUtils.formatDate(date));
		}
	}

	private URL createSpringXmlUrl() {
		return this.getClass().getClassLoader()
				.getResource(cmdLine.getOptionValue(OptCode.Xml.toString()));

	}

	private URL createLog4jUrl() {
		String log4jFilename = null;
		if (cmdLine.hasOption(OptCode.Log.toString())) {
			log4jFilename = cmdLine.getOptionValue(OptCode.Log.toString());
		} else {
			log4jFilename = LOG4J_DEFAULT_CONFIG_FILE;
		}
		return this.getClass().getClassLoader().getResource(log4jFilename);
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
			InputStream propStream = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			if (propStream == null) {
				String msg = "Could not stream the properties file " + filename
						+ " in the classpath";
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			try {

				props.load(propStream);
			} catch (FileNotFoundException e) {
				logger.error("File " + filename + " not found: " + e);
			} catch (IOException e) {
				logger.error("Error loading " + filename + ": " + e);
			}
			logger.debug("Loaded properties from file " + filename + ": "
					+ props.toString());
		}
		if (cmdLine.hasOption(OptCode.Properties.toString())) {
			Properties cmdLineProps = cmdLine
					.getOptionProperties(OptCode.Properties.toString());
			props.putAll(cmdLineProps);
			logger.debug("Command line properties given: "
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

	public URL getLog4jUrl() {
		return log4jUrl;
	}

	public URL getSpringXmlUrl() {
		return springXmlUrl;
	}
}
