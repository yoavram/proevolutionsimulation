package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimarInvasion extends
		AbstractInvasion<ModifierBacteria, ModifierBacteria> {
	private double mutationRateFitnessThreshold = 0;
	private double transformationRateFitnessThreshold = 0;
	private double mutationRateModifier = 1;
	private double transformationRateModifier = 1;

	@Override
	protected ModifierBacteria transform(ModifierBacteria bacteria) {
		bacteria.setMutationRateFitnessThreshold(getMutationRateFitnessThreshold());
		bacteria.setTransformationRateFitnessThreshold(getTransformationRateFitnessThreshold());
		bacteria.setMutationRateModifier(getMutationRateModifier());
		bacteria.setTransformationRateModifier(getTransformationRateModifier());
		return bacteria;
	}

	@Override
	public String getInvaderName() {
		return String
				.format("%s mutationRateModifier %f transformationRateModifier %f mutationRateFitnessThreshold %f transformationRateFitnessThreshold %f",
						ModifierBacteria.class.getSimpleName(),
						getMutationRateModifier(),
						getTransformationRateModifier(),
						getMutationRateFitnessThreshold(),
						getTransformationRateFitnessThreshold());
	}

	public double getMutationRateFitnessThreshold() {
		return mutationRateFitnessThreshold;
	}

	public void setMutationRateFitnessThreshold(double fitnessThreshold) {
		this.mutationRateFitnessThreshold = fitnessThreshold;
	}

	public double getTransformationRateFitnessThreshold() {
		return transformationRateFitnessThreshold;
	}

	public void setTransformationRateFitnessThreshold(double fitnessThreshold) {
		this.transformationRateFitnessThreshold = fitnessThreshold;
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
