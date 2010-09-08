package il.ac.tau.yoavram.pes;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PesCommandLineParser {

	private PesCommandLineParser() {
	}

	public enum OptCode {
		Help, Xml, Properties, FileProperties, Log;
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1).toLowerCase();
		};
	};

	public static CommandLine parse(String[] args) {
		CommandLineParser parser = new GnuParser();
		Options options = createOptions();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			printHelp(options);
		}
		return cmd;
	}

	private static void printHelp(Options options) {
		new HelpFormatter().printHelp(
				"java " + SpringRunner.class.getSimpleName(), options, true);

	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();
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
		return options;
	}
}
