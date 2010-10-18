package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimInvasion extends AbstractInvasion<Bacteria, Bacteria> {
	// private static final Logger logger = Logger.getLogger(SimInvasion.class);

	private double fitnessThreshold;
	private double mutationRateModifier;

	@Override
	public Bacteria transform(Bacteria bacteria) {
		bacteria.setFitnessThreshold(getFitnessThreshold());
		bacteria.setMutationRateModifier(getMutationRateModifier());
		return bacteria;
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
		return Bacteria.class.getSimpleName() + " mutationRateModifier "
				+ getMutationRateModifier() + " fitnessThreshold "
				+ getFitnessThreshold();
	}

}
