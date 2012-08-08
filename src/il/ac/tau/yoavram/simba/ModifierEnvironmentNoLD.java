package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * This is the same class as {@link ModifierEnvironment} with the exception that
 * all modifier genes are spread over the genome with maximum distance between
 * them.
 * 
 * @author yoavram
 * @since 8/8/2012
 */
public class ModifierEnvironmentNoLD extends ModifierEnvironment implements
		Environment {

	private static final long serialVersionUID = -1265328720468462124L;
	protected static final Logger logger = Logger.getLogger(Environment.class);

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

		int distance = getNumberOfGenes()
				/ (getNumberOfMutationModifiers()
						+ getNumberOfTransformationModifiers() + getNumberOfThresholdModifiers());
		int pos = 0;
		for (int mods = 0; mods < getNumberOfMutationModifiers(); mods++) {
			int gene = genes.remove(pos);
			pos += distance;
			types[gene] = GeneType.MutationRate;
			alleles[gene] = -1;
			mutationRateModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Mutation Rate Modifier"));
		}
		for (int mods = 0; mods < getNumberOfTransformationModifiers(); mods++) {
			int gene = genes.remove(pos);
			pos += distance;
			types[gene] = GeneType.TransformationRate;
			alleles[gene] = -1;
			transformationRateModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Transformation Rate Modifier"));
		}
		for (int mods = 0; mods < getNumberOfThresholdModifiers(); mods++) {
			int gene = genes.remove(pos);
			pos += distance;
			types[gene] = GeneType.Threshold;
			alleles[gene] = -1;
			thresholdModifiers[mods] = gene;
			logger.info(String.format("Locus %d is designated as a %s", gene,
					"Fitness Threshold Modifier"));
		}

		fitnessGenes = genes.toArray(new Integer[0]);
	}
}
