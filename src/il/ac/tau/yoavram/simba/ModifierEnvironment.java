package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

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

	private Integer[] fitnessGenes;
	private int[] mutationRateModifiers;
	private int[] transformationRateModifiers;
	private int[] thresholdModifiers;

	public ModifierEnvironment() {
		INSTANCE = this;
	}

	public static ModifierEnvironment getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Environment#init()
	 */
	@Override
	public void init() {
		alleles = new int[getNumberOfGenes()];
		types = new GeneType[getNumberOfGenes()];
		for (int gene = 0; gene < alleles.length; gene++) {
			alleles[gene] = RandomUtils.nextInt(0, 1);
			types[gene] = GeneType.Fitness;
		}

		List<Integer> genes = Lists.newArrayList();
		for (int gene = 0; gene < getNumberOfGenes(); gene++)
			genes.add(gene);

		mutationRateModifiers = new int[getNumberOfMutationModifiers()];
		transformationRateModifiers = new int[getNumberOfTransformationModifiers()];
		thresholdModifiers = new int[getNumberOfThresholdModifiers()];

		for (int mods = 0; mods < getNumberOfMutationModifiers(); mods++) {
			int gene = genes.remove(RandomUtils.nextInt(0, genes.size()));
			types[gene] = GeneType.MutationRate;
			alleles[gene] = -1;
			mutationRateModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Mutation Rate Modifier"));
		}
		for (int mods = 0; mods < getNumberOfTransformationModifiers(); mods++) {
			int gene = genes.remove(RandomUtils.nextInt(0, genes.size()));
			types[gene] = GeneType.TransformationRate;
			alleles[gene] = -1;
			transformationRateModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Transformation Rate Modifier"));
		}
		for (int mods = 0; mods < getNumberOfThresholdModifiers(); mods++) {
			int gene = genes.remove(RandomUtils.nextInt(0, genes.size()));
			types[gene] = GeneType.Threshold;
			alleles[gene] = -1;
			thresholdModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Fitness Threshold Modifier"));
		}

		fitnessGenes = genes.toArray(new Integer[0]);
	}

	@Override
	protected int chooseRandomGeneToChange() {
		return getRandomFitnessGene();
	}

	@Override
	protected void geneChangedMsg(int gene, int currentAllele, int newAllele) {
		logger.info(String.format(
				"Tick %s: Changed the allele in gene %d from %d to %d",
				NumberUtils.formatNumber(getTick()), gene, currentAllele, newAllele));
	}

	public int getRandomFitnessGene() {
		return fitnessGenes[RandomUtils.nextInt(0, fitnessGenes.length - 1)];
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

	public int getNumberOfMutationModifiers() {
		return numOfMutationModifiers;
	}

	public int getNumberOfTransformationModifiers() {
		return numOfTransformationModifiers;
	}

	public void setNumberOfTransformationModifiers(
			int numOfTransformationModifiers) {
		this.numOfTransformationModifiers = numOfTransformationModifiers;
	}

	public int getNumberOfThresholdModifiers() {
		return numOfThresholdModifiers;
	}

	public void setNumberOfThresholdModifiers(int numOfThresholdModifiers) {
		this.numOfThresholdModifiers = numOfThresholdModifiers;
	}

	public void setNumberOfMutationModifiers(int numOfMutationModifiers) {
		this.numOfMutationModifiers = numOfMutationModifiers;
	}

}
