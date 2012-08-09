package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.AbstractInvasion;

public class SimarInvasion extends
		AbstractInvasion<ModifierBacteria, ModifierBacteria> {
	private double fitnessThreshold = 0;
	private double mutationRateModifier = 1;
	private double transformationRateModifier = 1;

	@Override
	protected ModifierBacteria transform(ModifierBacteria bacteria) {
		bacteria.setFitnessThreshold(getFitnessThreshold());
		bacteria.setMutationRateModifier(getMutationRateModifier());
		bacteria.setTransformationRateModifier(getTransformationRateModifier());
		return bacteria;
	}

	@Override
	public String getInvaderName() {
		return String
				.format("%s mutationRateModifier %f transformationRateModifier %f fitnessThreshold %f",
						ModifierBacteria.class.getSimpleName(),
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
