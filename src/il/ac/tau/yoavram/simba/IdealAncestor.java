package il.ac.tau.yoavram.simba;

import java.util.Arrays;

public class IdealAncestor extends Bacteria {

	private static final long serialVersionUID = 2429038920415328352L;

	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;

	public IdealAncestor() {
		super();
	}

	public void init() {
		int[] env = new int[getNumberOfEnvironmentalGenes()];
		for (int i = 0; i < env.length; i++) {
			env[i] = Environment.getInstace().getIdealAllele(i);
		}
		int[] hk = new int[getNumberOfHousekeepingGenes()];
		Arrays.fill(hk, HousekeepingAllele.Beneficial.ordinal());

		setEnvironmentalAlleles(env);
		setHousekeepingAlleles(hk);
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
}
