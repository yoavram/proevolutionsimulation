package il.ac.tau.yoavram.simarba;

public class IdealAncestor extends TransformableBacteria {
	private static final long serialVersionUID = -6353798807269029745L;

	public IdealAncestor() {
		super();
	}

	public void init() {
		setAlleles(getEnvironment().getIdealAlleles());
	}

	@Override
	public void die() {
	}
}
