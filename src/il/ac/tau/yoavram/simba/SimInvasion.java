package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimInvasion extends AbstractInvasion<Bacteria, SimBacteria> {
	// private static final Logger logger = Logger.getLogger(SimInvasion.class);

	private double fitnessThreshold;
	private double mutationRateModifier;

	@Override
	protected SimBacteria transform(Bacteria bacteria) {
		SimBacteria sim = new SimBacteria(bacteria);
		sim.setFitnessThreshold(getFitnessThreshold());
		sim.setMutationRateModifier(getMutationRateModifier());
		return sim;
	}

	public double getFitnessThreshold() {
		return fitnessThreshold;
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	@Override
	public String getInvaderName() {
		return SimBacteria.class.getSimpleName();
	}

}
