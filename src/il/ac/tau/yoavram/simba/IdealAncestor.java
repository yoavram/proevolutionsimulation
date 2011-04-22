package il.ac.tau.yoavram.simba;

public class IdealAncestor extends SimpleBacteria {
	private static final long serialVersionUID = 8192172491310782942L;
	private int numberOfEnvironmentalGenes;

	public IdealAncestor() {
		super();
	}

	public void init() {
		int[] env = new int[getNumberOfEnvironmentalGenes()];
		for (int i = 0; i < env.length; i++) {
			env[i] = getEnvironment().getIdealAllele(i);
		}
		setEnvironmentalAlleles(env);
		setNumberOfDeleteriousHousekeepingGenes(0);
	}

	@Override
	public void die() {
		// do not go to recycling
	}

	public int getNumberOfEnvironmentalGenes() {
		return numberOfEnvironmentalGenes;
	}

	public void setNumberOfEnvironmentalGenes(int numberOfEnvironmentalGenes) {
		this.numberOfEnvironmentalGenes = numberOfEnvironmentalGenes;
	}

}
