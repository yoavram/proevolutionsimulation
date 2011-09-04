package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;
import il.ac.tau.yoavram.simarba.Environment.GeneType;

public class RandomAncestor extends IdealAncestor {
	private static final long serialVersionUID = -6459027848776836855L;

	public RandomAncestor() {
		super();
	}

	public void init() {
		super.init();
	}

	@Override
	public TransformableBacteria reproduce() {
		TransformableBacteria child = spawn();
		int n = (int) (alleles.length * 0.01);
		int g = 0;
		while (g < n) {
			int gene = RandomUtils.nextInt(0, alleles.length - 1);
			if (!getEnvironment().geneType(gene).equals(GeneType.MOD)) {
				child.alleles[gene] = (float) RandomUtils.nextDouble();
				g++;
			}
		}
		return child;
	};

	@Override
	public void die() {
	}
}
