package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimarInvasion extends AbstractInvasion<SexyBacteria, SexyBacteria> {
	private double fitnessThreshold;
	private double mutationRateModifier;
	private double transformationRateModifier;

	@Override
	protected SexyBacteria transform(SexyBacteria bacteria) {
		bacteria.setFitnessThreshold(getFitnessThreshold());
		bacteria.setMutationRateModifier(getMutationRateModifier());
		bacteria.setTransformationRateModifier(getMutationRateModifier());
		return bacteria;
	}

	@Override
	public String getInvaderName() {
		return String
				.format("%s mutationRateModifier %f transformationRateModifier %f fitnessThreshold %f",
						SexyBacteria.class.getSimpleName(),
						getMutationRateModifier(),
						getTransformationRateModifier(), getFitnessThreshold());
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

	public double getTransformationRateModifier() {
		return transformationRateModifier;
	}

	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = transformationRateModifier;
	}

}
