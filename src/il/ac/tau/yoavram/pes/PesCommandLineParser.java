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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This command line parser is used by {@link SimulationConfigurer} to parse the <code>String[] args</code> input.</br>
 * 
 * @author yoavram
 * @see SimulationConfigurer
 *
 */
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
