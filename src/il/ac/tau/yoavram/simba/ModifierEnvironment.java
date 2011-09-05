package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import org.apache.log4j.Logger;

public class ModifierEnvironment extends SimpleEnvironment implements
		Environment {

	public enum GeneType {
		Fitness, MutationRate, TransformationRate, Threshold
	}

	private static final long serialVersionUID = -1046262126111003968L;

	private static final Logger logger = Logger.getLogger(Environment.class);

	private static ModifierEnvironment INSTANCE = null;

	private GeneType[] types;
	private int numOfMutationModifiers = 0;
	private int numOfTransformationModifiers = 0;
	private int numOfThresholdModifiers = 0;

	private int[] fitnessGenes;
	private int[] mutationRateModifiers;
	private int[] transformationRateModifiers;
	private int[] thresholdModifiers;

	public ModifierEnvironment() {
		INSTANCE = this;
	}

	public static ModifierEnvironment getInstance() {
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#init()
	 */
	@Override
	public void init() {
		alleles = new int[getNumberOfGenes()];
		types = new GeneType[getNumberOfGenes()];
		for (int gene = 0; gene < alleles.length; gene++) {
			alleles[gene] = RandomUtils.nextInt(0, 1);
			types[gene] = GeneType.Fitness;
		}
		mutationRateModifiers = new int[getNumOfMutationModifiers()];
		transformationRateModifiers = new int[getNumOfTransformationModifiers()];
		thresholdModifiers = new int[getNumOfThresholdModifiers()];

		for (int mods = 0; mods < getNumOfMutationModifiers(); mods++) {
			int gene = randomFitnessGene();
			types[gene] = GeneType.MutationRate;
			alleles[gene] = -1;
			mutationRateModifiers[mods] = gene;
		}
		for (int mods = 0; mods < getNumOfTransformationModifiers(); mods++) {
			int gene = randomFitnessGene();
			types[gene] = GeneType.TransformationRate;
			alleles[gene] = -1;
			transformationRateModifiers[mods] = gene;
		}
		for (int mods = 0; mods < getNumOfThresholdModifiers(); mods++) {
			int gene = randomFitnessGene();
			types[gene] = GeneType.Threshold;
			alleles[gene] = -1;
			thresholdModifiers[mods] = gene;
		}

		fitnessGenes = new int[alleles.length - getNumOfMutationModifiers()
				- getNumOfTransformationModifiers()
				- getNumOfThresholdModifiers()];
		int index = 0;
		for (int gene = 0; gene < alleles.length; gene++) {
			if (types[gene] == GeneType.Fitness) {
				fitnessGenes[index++] = gene;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#change(double)
	 */
	@Override
	public void change(double fractionOfGenesToChange) {
		double toChange = Math.ceil(fractionOfGenesToChange * alleles.length);
		for (int i = 0; i < toChange; i++) {
			int gene = randomFitnessGene();
			int currentAllele = alleles[gene];
			int newAllele = (currentAllele + 1) % 2;
			alleles[gene] = newAllele;
			logger.debug("Changed the  gene " + gene + " from " + currentAllele
					+ " to " + newAllele);
		}
		if (toChange > 0) {
			lastEnvironmentalChange = getTick();
		}
	}

	private int randomFitnessGene() {
		int gene = RandomUtils.nextInt(0, alleles.length - 1);
		while (types[gene] != GeneType.Fitness) {
			gene = RandomUtils.nextInt(0, alleles.length - 1);
		}
		return gene;
	}

	public int getRandomFitnessGene() {
		return RandomUtils.nextInt(0, fitnessGenes.length - 1);
	}

	public int[] getMutationRateModifiers() {
		return mutationRateModifiers;
	}

	public int[] getTransformationRateModifiers() {
		return transformationRateModifiers;
	}

	public int[] getThresholdModifiers() {
		return thresholdModifiers;
	}

	public GeneType getGeneType(int gene) {
		return types[gene];
	}

	public void setNumberOfGenes(int numberOfGenes) {
		setNumberOfEnvironmentalGenes(numberOfGenes);
	}

	public int getNumberOfGenes() {
		return getNumberOfEnvironmentalGenes();
	}

	public int getNumOfMutationModifiers() {
		return numOfMutationModifiers;
	}

	public void setNumOfMutationModifiers(int numOfMutationModifiers) {
		this.numOfMutationModifiers = numOfMutationModifiers;
	}

	public int getNumOfTransformationModifiers() {
		return numOfTransformationModifiers;
	}

	public void setNumOfTransformationModifiers(int numOfTransformationModifiers) {
		this.numOfTransformationModifiers = numOfTransformationModifiers;
	}

	public int getNumOfThresholdModifiers() {
		return numOfThresholdModifiers;
	}

	public void setNumOfThresholdModifiers(int numOfThresholdModifiers) {
		this.numOfThresholdModifiers = numOfThresholdModifiers;
	}

	public void setNumberOfMutationModifiers(int numOfMutationModifiers) {
		this.numOfMutationModifiers = numOfMutationModifiers;
	}

	public void setNumberOfTransformationModifiers(
			int numOfTransformationModifiers) {
		this.numOfTransformationModifiers = numOfTransformationModifiers;
	}

	public void setNumberOfThresholdModifiers(int numOfThresholdModifiers) {
		this.numOfThresholdModifiers = numOfThresholdModifiers;
	}

}
