package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.filters.ClassFilter;

public class SimBacteria extends Bacteria {
	private static final long serialVersionUID = -5401626270460444969L;
	// private static final Logger logger = Logger.getLogger(SimBacteria.class);
	protected static ThreadLocal<SimBacteria> simTrash = new ThreadLocal<SimBacteria>();

	private double fitnessThreshold = 0;
	private double mutationRateModifier = 1;

	public SimBacteria() {
		super();
	}

	public SimBacteria(Bacteria other) {
		this();
		copy(other);
	}

	@Override
	protected void copy(Bacteria other) {
		super.copy(other);
		if (other instanceof SimBacteria) {
			SimBacteria otherSim = (SimBacteria) other;
			this.fitnessThreshold = otherSim.fitnessThreshold;
			this.mutationRateModifier = otherSim.mutationRateModifier;
		}
	}

	@Override
	protected Bacteria create() {
		return new SimBacteria();
	}

	public Bacteria getTrash() {
		return simTrash.get();
	}

	public void setTrash(Bacteria bacteria) {
		if (bacteria instanceof SimBacteria)
			simTrash.set((SimBacteria) bacteria);
		else
			super.setTrash(bacteria);
	}

	public void removeTrash() {
		simTrash.remove();
	}

	@Override
	public double getMutationRate() {
		if (getFitness() < getFitnessThreshold()) {
			return getMutationRateModifier() * super.getMutationRate();
		} else {
			return super.getMutationRate();
		}
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

	public static class Filter extends ClassFilter<Bacteria> {
		public Filter() {
			setClazz(SimBacteria.class);
		}
	}

}
