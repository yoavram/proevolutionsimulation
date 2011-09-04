package il.ac.tau.yoavram.simba.difeq;

import java.math.BigDecimal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DifeqCommandLineParser {
	CommandLine cmd = null;

	public static void main(String[] args) {
		printHelp(createOptions());

	}

	public DifeqCommandLineParser(String[] args) {
		CommandLineParser parser = new GnuParser();
		Options options = createOptions();

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			printHelp(options);
		}
		if (cmd.hasOption('h'))
			printHelp(options);
	}

	private BigDecimal getOptionValue(char option) {
		return new BigDecimal(cmd.getOptionValue(option));
	}

	private static void printHelp(Options options) {
		new HelpFormatter().printHelp("java " + Difeq.class.getSimpleName(),
				"Use to solve SIM difference equations system", options,
				"Written by Yoav Ram (yoavra@post.tau.ac.il)", true);
	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();

		Option h = OptionBuilder.withLongOpt("help")
				.withDescription("print this message").create('h');
		Option n = OptionBuilder.hasArg().withLongOpt("n")
				.withDescription("Number of states > 0").create('n');
		Option s = OptionBuilder.hasArg().withLongOpt("s")
				.withDescription("Selection coefficient, 0<s<1").create('s');
		Option tau = OptionBuilder.hasArg().withLongOpt("tau")
				.withDescription("Mutation rate modifier 0<=tau").create('t');
		Option pi = OptionBuilder.hasArg().withLongOpt("pi")
				.withDescription("Fitness threshold, 0<=pi<=n+1").create('p');
		Option gamma = OptionBuilder.hasArg().withLongOpt("gamma")
				.withDescription("Deleterious mutation probability, 0<gamma<1")
				.create('g');
		Option phi = OptionBuilder.hasArg().withLongOpt("phi")
				.withDescription("Beneficial mutation probability, 0<phi<1")
				.create('f');
	/*	Option err = OptionBuilder.hasArg().withLongOpt("err")
				.withDescription("error threshold < 1").create('e');*/
		Option iter = OptionBuilder.hasArg().withLongOpt("iter")
				.withDescription("Maximum number of iterations > 1")
				.create('i');
		Option precision = OptionBuilder.hasArg().withLongOpt("precision")
		.withDescription("Number of digits after the dot > 1")
		.create('r');

		options.addOption(h).addOption(n).addOption(s).addOption(tau)
				.addOption(pi).addOption(gamma).addOption(phi)//.addOption(err)
				.addOption(iter).addOption(precision);
		return options;
	}

	public CommandLine getCmd() {
		return cmd;
	}

	public void setCmd(CommandLine cmd) {
		this.cmd = cmd;
	}

	public int getN() {
		return Integer.valueOf(cmd.getOptionValue('n'));
	}

	public BigDecimal getS() {
		return getOptionValue('s');
	}

	public BigDecimal getTau() {
		return getOptionValue('t');
	}

	public BigDecimal getMu() {
		return getOptionValue('m');
	}

	public BigDecimal getGamma() {
		return getOptionValue('g');
	}

	public BigDecimal getPhi() {
		return getOptionValue('f');
	}
	
	public BigDecimal getPi() {
		return getOptionValue('p');
	}

/*	public BigDecimal getErr() {
		return getOptionValue('e');
	}*/

	public int getIter() {
		return Integer.valueOf(cmd.getOptionValue('i'));
	}

	public int getPrecision() {
		return Integer.valueOf(cmd.getOptionValue('r'));

	}
}
