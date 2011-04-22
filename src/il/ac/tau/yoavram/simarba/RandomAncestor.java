package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

public class RandomAncestor extends TransformableBacteria {
	private static final long serialVersionUID = -1820986529454870872L;

	private double randomness = 0.01;

	public RandomAncestor() {
		super();
	}

	public void init() {
		int[] alleles = getEnvironment().getIdealAlleles();
		for (int i = 0; i < alleles.length; i++) {
			if (RandomUtils.nextDouble() < getRandomness())
				alleles[i] = RandomUtils.nextInt(0, 2);
		}
		setAlleles(alleles);
	}

	@Override
	public void die() {
	}

	public double getRandomness() {
		return randomness;
	}

	public void setRandomness(double randomness) {
		this.randomness = randomness;
	}

}
