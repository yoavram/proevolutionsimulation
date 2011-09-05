package il.ac.tau.yoavram.simba;

public class IdealSexyAncestor extends SexyBacteria {

	private static final long serialVersionUID = 867656401286313696L;
	private int numberOfGenes;

	public IdealSexyAncestor() {
		super();
	}

	public void init() {
		int[] genome = new int[getNumberOfGenes()];
		for (int i = 0; i < genome.length; i++) {
			genome[i] = getEnvironment().getIdealAllele(i);
		}
		for (int modifier : getEnvironment().getMutationRateModifiers()) {
			genome[modifier] = 0;
		}
		for (int modifier : getEnvironment().getTransformationRateModifiers()) {
			genome[modifier] = 0;
		}
		for (int modifier : getEnvironment().getThresholdModifiers()) {
			genome[modifier] = Integer.MAX_VALUE;
		}
		setAlleles(genome);
		super.setMutationRateModifier(mutationRateModifier);
		super.setTransformationRateModifier(transformationRateModifier);
		super.setFitnessThreshold(fitnessThreshold);
	}

	@Override
	public void die() {
		// do not go to recycling
	}

	public int getNumberOfGenes() {
		return numberOfGenes;
	}

	public void setNumberOfGenes(int numberOfGenes) {
		this.numberOfGenes = numberOfGenes;
	}

	@Override
	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
	}

	@Override
	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	@Override
	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = transformationRateModifier;
	}

}
