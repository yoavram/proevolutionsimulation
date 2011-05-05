package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.simarba.Environment.GeneType;
import il.ac.tau.yoavram.simba.Bacteria;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class RateStrategy {
	enum ModifierType {
		BasalLevel, Threshold, InducedLevel
	}

	private Environment environment;
	private boolean stressInducedMutation;
	private boolean stressInducedTransformation;
	private double minMutationRate;
	private double maxMutationRate;
	private double minTransformationRate;
	private double maxTransformationnRate;
	private double mutationStep;
	private double transformationStep;
	private double mutatioRateResolution;
	private double transformationRateResolution;
	private double mutationThresholdResolution;
	private double transformationThresholdResolution;

	private Multimap<ModifierType, Integer> mutationIndice;
	private Multimap<ModifierType, Integer> transformationIndice;
	private double mutationThreshold = 0;
	private double mutationInducedRate = 0;
	private double transformationBasalRate = 0;
	private double transformationThreshold = 0;
	private double transformationInducedRate = 0;
	private double mutationBasalRate = 0;

	public RateStrategy() {
		mutationIndice = HashMultimap.create(3, 10);
		transformationIndice = HashMultimap.create(3, 10);

	}

	public void init() {
		int mutTypes = isStressInducedMutation() ? ModifierType.values().length
				: 1;
		int traTypes = isStressInducedTransformation() ? ModifierType.values().length
				: 1;
		int mutMods = 0;
		int traMods = 0;

		for (int gene = 0; gene < getEnvironment().getNumberOfGenes(); gene++) {
			if (getEnvironment().getGeneType(gene).equals(GeneType.MOD)) {
				if (mutMods <= traMods) {
					ModifierType type = ModifierType.values()[mutMods
							% mutTypes];
					mutationIndice.put(type, gene);
					mutMods++;
				} else {
					ModifierType type = ModifierType.values()[traMods
							% traTypes];
					transformationIndice.put(type, gene);
					traMods++;
				}
			}
		}
		mutationStep = (maxMutationRate - minMutationRate)
				/ (double) mutatioRateResolution;
		transformationStep = (maxTransformationnRate - minTransformationRate)
				/ (double) transformationRateResolution;

	}

	public void setParameters(Bacteria bacteria) {
		for (Integer i : mutationIndice.get(ModifierType.BasalLevel)) {
			mutationBasalRate += alleles[i];
		}
		mutationBasalRate = minMutationRate + mutationStep * mutationBasalRate;
		for (Integer i : mutationIndice.get(ModifierType.Threshold)) {
			mutationThreshold += alleles[i];
		}
		mutationThreshold=mutationThreshold/mutationThresholdResolution;
		for (Integer i : mutationIndice.get(ModifierType.InducedLevel)) {
			mutationInducedRate += alleles[i];
		}
		mutationInducedRate = minMutationRate + mutationStep
				* mutationInducedRate;
		for (Integer i : transformationIndice.get(ModifierType.BasalLevel)) {
			transformationBasalRate += alleles[i];
		}
		transformationBasalRate = minTransformationRate + transformationStep
				* transformationBasalRate;
		for (Integer i : transformationIndice.get(ModifierType.Threshold)) {
			transformationThreshold += alleles[i];
		}
		for (Integer i : transformationIndice.get(ModifierType.InducedLevel)) {
			transformationInducedRate += alleles[i];
		}
		transformationInducedRate = minTransformationRate + transformationStep
				* transformationInducedRate;
	}

	public void clear() {
		mutationThreshold = 0;
		mutationInducedRate = 0;
		transformationBasalRate = 0;
		transformationThreshold = 0;
		transformationInducedRate = 0;
		mutationBasalRate = 0;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setStressInducedMutation(boolean stressInducedMutation) {
		this.stressInducedMutation = stressInducedMutation;
	}

	public boolean isStressInducedMutation() {
		return stressInducedMutation;
	}

	public void setStressInducedTransformation(
			boolean stressInducedTransformation) {
		this.stressInducedTransformation = stressInducedTransformation;
	}

	public boolean isStressInducedTransformation() {
		return stressInducedTransformation;
	}

	public double getMinMutationRate() {
		return minMutationRate;
	}

	public void setMinMutationRate(double minMutationRate) {
		this.minMutationRate = minMutationRate;
	}

	public double getMaxMutationRate() {
		return maxMutationRate;
	}

	public void setMaxMutationRate(double maxMutationRate) {
		this.maxMutationRate = maxMutationRate;
	}

	public double getMinTransformationRate() {
		return minTransformationRate;
	}

	public void setMinTransformationRate(double minTransformationRate) {
		this.minTransformationRate = minTransformationRate;
	}

	public double getMaxTransformationnRate() {
		return maxTransformationnRate;
	}

	public void setMaxTransformationnRate(double maxTransformationnRate) {
		this.maxTransformationnRate = maxTransformationnRate;
	}

	
}
