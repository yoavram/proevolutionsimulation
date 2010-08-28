package il.ac.tau.yoavram.simba;


public class SimBacteria extends Bacteria {
	private static final long serialVersionUID = -5401626270460444969L;
	//private static final Logger logger = Logger.getLogger(SimBacteria.class);

	private double fitnessThreshold = 0;
	private double mutationRateModifier = 1;

	public SimBacteria() {
		super();
	}

	public SimBacteria(SimBacteria other) {
		super(other);
		this.fitnessThreshold = other.fitnessThreshold;
		this.mutationRateModifier = other.mutationRateModifier;
	}

	public SimBacteria(Bacteria other) {
		super(other);
	}

	@Override
	public double getMutationRate() {
		if (getFitness() < getFitnessThreshold()) {
			return getMutationRateModifier() * super.getMutationRate();
		} else {
			return super.getMutationRate();
		}
	}

	@Override
	protected Bacteria spawn() {
		return new SimBacteria();
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
	}

	public double getFitnessThreshold() {
		return fitnessThreshold;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

}
