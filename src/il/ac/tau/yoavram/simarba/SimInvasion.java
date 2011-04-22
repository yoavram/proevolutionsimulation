package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimInvasion extends
		AbstractInvasion<TransformableBacteria, TransformableBacteria> {

	private double fitnessThreshold;
	private double mutationRateModifier;

	@Override
	public TransformableBacteria transform(TransformableBacteria bacteria) {
		bacteria.setMutationFitnessThreshold(getMutationFitnessThreshold());
		bacteria.setMutationRateModifier(getMutationRateModifier());
		return bacteria;
	}

	public double getMutationFitnessThreshold() {
		return fitnessThreshold;
	}

	public void setMutationFitnessThreshold(double fitnessThreshold) {
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
		return TransformableBacteria.class.getSimpleName()
				+ " mutationRateModifier " + getMutationRateModifier()
				+ " mutationFitnessThreshold " + getMutationFitnessThreshold();
	}

}
