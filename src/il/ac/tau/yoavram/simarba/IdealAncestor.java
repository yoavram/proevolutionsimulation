package il.ac.tau.yoavram.simarba;

import java.util.Arrays;

import com.google.common.collect.HashMultimap;

public class IdealAncestor extends TransformableBacteria {
	private static final long serialVersionUID = -6353798807269029745L;

	private int numberOfLowMutationRateLoci = 0;
	private int numberOfHighMutationRateLoci = 0;
	private int numberOfMutationThresholdLoci = 0;
	private int numberOfLowTransformationRateLoci = 0;
	private int numberOfHighTransformationRateLoci = 0;
	private int numberOfTransformationThresholdLoci = 0;

	public IdealAncestor() {
		super();
	}

	public void init() {
		alleles = Arrays.copyOf(getEnvironment().idealAlleles(),
				getEnvironment().idealAlleles().length);

		int mods = 0;
		modifierPositions = HashMultimap.create();
		for (int gene = 0; gene < alleles.length; gene++) {
			if (alleles[gene] == -1) {
				// modifier gene
				if (mods < numberOfLowMutationRateLoci)
					modifierPositions.get(0).add(gene);
				else if (mods < numberOfHighMutationRateLoci)
					modifierPositions.get(1).add(gene);
				else if (mods < numberOfMutationThresholdLoci)
					modifierPositions.get(2).add(gene);
				else if (mods < numberOfLowTransformationRateLoci)
					modifierPositions.get(3).add(gene);
				else if (mods < numberOfHighTransformationRateLoci)
					modifierPositions.get(4).add(gene);
				else if (mods < numberOfTransformationThresholdLoci)
					modifierPositions.get(5).add(gene);
				mods++;
			}
		}
		// mutation rate
		if (mutationRate == -1)
			mutationRate = 0.001;

		float allele = (float) (mutationRate / modifierPositions.get(0).size());
		for (int gene : modifierPositions.get(0)) {
			alleles[gene] = allele;
		}

		allele = (float) (mutationRate / modifierPositions.get(1).size());
		for (int gene : modifierPositions.get(1)) {
			alleles[gene] = allele;
		}

		// transformation rate
		if (transformationRate == -1)
			transformationRate = 0.0001;

		allele = (float) (transformationRate / modifierPositions.get(3).size());
		for (int gene : modifierPositions.get(3)) {
			alleles[gene] = allele;
		}

		allele = (float) (transformationRate / modifierPositions.get(4).size());
		for (int gene : modifierPositions.get(4)) {
			alleles[gene] = allele;
		}

		// mutation threshold
		if (mutationThreshold == -1)
			mutationThreshold = 0;

		for (int gene : modifierPositions.get(2)) {
			alleles[gene] = 0;
		}

		// transformation threshold
		if (transformationThreshold == -1)
			transformationThreshold = 0;

		for (int gene : modifierPositions.get(5)) {
			alleles[gene] = 0;
		}
		update(0);
	}

	@Override
	public void die() {
	}

}
