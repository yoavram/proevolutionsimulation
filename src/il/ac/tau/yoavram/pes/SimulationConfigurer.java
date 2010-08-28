package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	public static final String LOG4J_DEFAULT_CONFIG_FILE = "config/log4j.properties";

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
	private File log4jFile;
	private File springXmlFile;

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
		log4jFile = createLog4jFile();
		PropertyConfigurator.configure(log4jFile.getAbsolutePath());
		// TODO change log file name
		properties = createProperties();
		springXmlFile = createSpringXmlFile();

		if (!properties.containsKey(JOB_NAME_KEY)) {
			properties.put(JOB_NAME_KEY,
					springXmlFile.getName()
							.replace(XML_EXTENSION, EMPTY_STRING));
		}
		if (!properties.containsKey(TIME)) {
			properties.put(TIME, TimeUtils.formatDate(date));
		}
	}

	private File createSpringXmlFile() {
		return new File(cmdLine.getOptionValue(OptCode.Xml.toString()));

	}

	private File createLog4jFile() {
		String log4jFilename = null;
		if (cmdLine.hasOption(OptCode.Log.toString())) {
			log4jFilename = cmdLine.getOptionValue(OptCode.Log.toString());
		} else {
			log4jFilename = LOG4J_DEFAULT_CONFIG_FILE;
		}
		return new File(log4jFilename);
	}

	private CommandLine parseArgs(String[] args) {
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error(e);
			printHelp();
		}
		return cmd;
	}

	private Properties createProperties() {
		Properties props = new Properties();
		if (cmdLine.hasOption(OptCode.FileProperties.toString())) {
			String filename = cmdLine.getOptionValue(OptCode.FileProperties
					.toString());
			try {
				props.load(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
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

	public File getLog4jFile() {
		return log4jFile;
	}

	public File getSpringXmlFile() {
		return springXmlFile;
	}
}
