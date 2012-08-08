package il.ac.tau.yoavram.simba;

public class IdealModifierAncestor extends ModifierBacteria {
	private static final long serialVersionUID = -3388750169448804095L;
	protected int numberOfGenes;

	public IdealModifierAncestor() {
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
}
