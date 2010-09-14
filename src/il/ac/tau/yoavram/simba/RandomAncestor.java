package il.ac.tau.yoavram.simba;

import java.util.Arrays;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

public class RandomAncestor extends Bacteria {
	private static final long serialVersionUID = 2055347433087760819L;

	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;

	public RandomAncestor() {
		super();
	}

	public void init() {
		int[] env = new int[getNumberOfEnvironmentalGenes()];
		for (int i = 0; i < env.length; i++) {
			if (RandomUtils.nextDouble() < 0.01)
				env[i] = RandomUtils.nextInt(0, 1);
			else
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
}