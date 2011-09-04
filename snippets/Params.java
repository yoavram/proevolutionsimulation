package il.ac.tau.yoavram.pes.params;

import java.io.File;

//TODO i don't like this file, see if i can remove it from use
public class Params {
	protected ParamEngine paramEngine = null;

	public ParamEngine getParamEngine() {
		return paramEngine;
	}

	public void setParamEngine(ParamEngine paramEngine) {
		this.paramEngine = paramEngine;
	}

	public double getRunLength() {
		return paramEngine.getParam("runlength");
	}

	public String getOutputFile() {
		return paramEngine.getParam("outputfile");
	}

	public String getFreezedryFolder() {
		String folder = paramEngine.getParam("freezedryfolder");
		if (!folder.endsWith(File.separator)) {
			folder += File.separator;
		}
		return folder;
	}

	public String getInputFile() {
		return paramEngine.getParam("inputfile");
	}

	public int getMaxPopulationSize() {
		return paramEngine.getParam("maxpopulationsize");
	}

	public int getNumberOfGenes() {
		return paramEngine.getParam("numberofgenes");
	}

	public double getStandardSelectionModifier() {
		return paramEngine.getParam("standardselectionmodifier");
	}

	public double getEnvironmentalGeneFrequency() {
		return paramEngine.getParam("environmentalgenefrequency");
	}

	public double getOutXingFitnessCost() {
		return paramEngine.getParam("outxingfitnesscost");
	}

	public double getGlobalMutationRate() {
		return paramEngine.getParam("globalmutationrate");
	}

	public boolean isChangeEnvironmentOnStartup() {
		return paramEngine.getParam("changeenvironmentonstartup");
	}

	public double getFractionOfEnvironmentalGenesToChange() {
		return paramEngine.getParam("fractionofenvironmentalgenestochange");
	}

	public int getNumberOfAlleles() {
		return 3;
	}

	public double getFAMOrganismsStartingFrequency() {
		return paramEngine.getParam("famorganismsstartingfrequency");
	}

	public double getFAMFunctionThreshold() {
		return paramEngine.getParam("famfunctionthreshold");
	}

	public double getFAMFunctionMutationRateModifier() {
		return paramEngine.getParam("famfunctionmutationratemodifier");
	}

	public double getHeterozygotCoefficient() {
		return 0.5;
	}

	public double getRecombinationFrequency() {
		return 0.5;
	}

	public double getEnvironmentalChangeFrequency() {
		return paramEngine.getParam("environmentalchangefrequency");
	}
}
