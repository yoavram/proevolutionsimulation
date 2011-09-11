package il.ac.tau.yoavram.simba;

public class IdealSexyAncestor extends SexyBacteria {
	private static final long serialVersionUID = 867656401286313696L;

	protected int numberOfGenes;

	public IdealSexyAncestor() {
		super();
	}

	public void init() {
		int[] alleles = new int[getNumberOfGenes()];
		for (int i = 0; i < alleles.length; i++) {
			alleles[i] = getEnvironment().getIdealAllele(i);
		}
		
		setAlleles(alleles);
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
