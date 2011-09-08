package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.DoubleInvasion;

public class SimCmInvasion extends
		DoubleInvasion<SimpleBacteria, SimpleBacteria> {

	public SimCmInvasion() {
		invasion1 = new SimInvasion();
		invasion2 = new SimInvasion();
	}
	
	public void init() {
		((SimInvasion) invasion2).setFitnessThreshold(1.0);
	}

	public double getFitnessThreshold() {
		return ((SimInvasion) invasion1).getFitnessThreshold();
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		((SimInvasion) invasion1).setFitnessThreshold(fitnessThreshold);
	}

	public double getSimMutationRateModifier() {
		return ((SimInvasion) invasion1).getMutationRateModifier();
	}

	public void setSimMutationRateModifier(double mutationRateModifier) {
		((SimInvasion) invasion1).setMutationRateModifier(mutationRateModifier);
	}

	public double getCmMutationRateModifier() {
		return ((SimInvasion) invasion2).getMutationRateModifier();
	}

	public void setCmMutationRateModifier(double mutationRateModifier) {
		((SimInvasion) invasion2).setMutationRateModifier(mutationRateModifier);
	}
}
