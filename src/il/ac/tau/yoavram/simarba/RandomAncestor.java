package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

public class RandomAncestor extends IdealAncestor {
	private static final long serialVersionUID = -1820986529454870872L;

	private double randomness = 0.01;

	public RandomAncestor() {
		super();
	}

	public void init() {
		super.init();
		for (int gene = 0; gene < alleles.length; gene++) {
			if (alleles[gene] != -1
					&& RandomUtils.nextDouble() < getRandomness()) {
				alleles[gene] = (float) RandomUtils.nextDouble();
			}
		}
	}

	@Override
	public void die() {
	}

	/**
	 * The probability that an allele will be random instead of ideal
	 * 
	 * @return the probability. default is 0.01.
	 */
	public double getRandomness() {
		return randomness;
	}

	public void setRandomness(double randomness) {
		this.randomness = randomness;
	}

}
