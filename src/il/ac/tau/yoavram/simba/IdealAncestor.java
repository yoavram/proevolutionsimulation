package il.ac.tau.yoavram.simba;

import java.util.Arrays;

public class IdealAncestor extends Bacteria {

	private static final long serialVersionUID = 2429038920415328352L;

	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;
	private Environment environment;

	public IdealAncestor() {
		super();
	}

	public void init() {
		int[] env = new int[getNumberOfEnvironmentalGenes()];
		for (int i = 0; i < env.length; i++) {
			env[i] = getEnvironment().getIdealAllele(i);
		}
		int[] hk = new int[getNumberOfHousekeepingGenes()];
		Arrays.fill(hk, 0);

		setEnvironmentalAlleles(env);
		setHousekeepingAlleles(hk);
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

	public int getNumberOfHousekeepingGenes() {
		return numberOfHousekeepingGenes;
	}

	public void setNumberOfHousekeepingGenes(int numberOfHousekeepingGenes) {
		this.numberOfHousekeepingGenes = numberOfHousekeepingGenes;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}
}
