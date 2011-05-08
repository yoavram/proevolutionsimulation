package il.ac.tau.yoavram.simarba;

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
		for (int gene = 0; gene < alleles.length; gene++)
			child.mutate(gene);
		return child;
	};

	@Override
	public void die() {
	}
}
