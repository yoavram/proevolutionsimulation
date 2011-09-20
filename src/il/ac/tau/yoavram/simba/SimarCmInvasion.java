package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.DoubleInvasion;

public class SimarCmInvasion extends DoubleInvasion<SexyBacteria, SexyBacteria> {

	public SimarCmInvasion() {
		invasion1 = new SimarInvasion();
		invasion2 = new SimarInvasion();
	}

	public void init() {
		((SimarInvasion) invasion2).setFitnessThreshold(0.0);
	}

	public double getFitnessThreshold() {
		return ((SimarInvasion) invasion1).getFitnessThreshold();
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		((SimarInvasion) invasion1).setFitnessThreshold(fitnessThreshold);
	}

	public double getSimMutationRateModifier() {
		return ((SimarInvasion) invasion1).getMutationRateModifier();
	}

	public void setSimMutationRateModifier(double mutationRateModifier) {
		((SimarInvasion) invasion1)
				.setMutationRateModifier(mutationRateModifier);
	}

	public double getCmMutationRateModifier() {
		return ((SimarInvasion) invasion2).getMutationRateModifier();
	}

	public void setCmMutationRateModifier(double mutationRateModifier) {
		((SimarInvasion) invasion2)
				.setMutationRateModifier(mutationRateModifier);
	}
}
