package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

public class SimCmInvasion implements
		Invasion<TransformableBacteria, TransformableBacteria> {
	private double invasionRate;
	protected SimInvasion simInvasion = new SimInvasion();
	protected SimInvasion cmInvasion = new SimInvasion();

	public void init() {
		cmInvasion.setMutationFitnessThreshold(1.0);
	}

	@Override
	public List<List<TransformableBacteria>> invade(
			List<List<TransformableBacteria>> populations) {
		for (List<TransformableBacteria> population : populations) {
			for (int i = 0; i < population.size(); i++) {
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					TransformableBacteria e = simInvasion.transform(population
							.get(i));
					population.set(i, e);
				} else {
					TransformableBacteria e = cmInvasion.transform(population
							.get(i));
					population.set(i, e);
				}
			}
		}
		return populations;
	}

	public double getMutationFitnessThreshold() {
		return simInvasion.getMutationFitnessThreshold();
	}

	public void setMutationFitnessThreshold(double fitnessThreshold) {
		simInvasion.setMutationFitnessThreshold(fitnessThreshold);
	}

	public double getSimMutationRateModifier() {
		return simInvasion.getMutationRateModifier();
	}

	public void setSimMutationRateModifier(double mutationRateModifier) {
		simInvasion.setMutationRateModifier(mutationRateModifier);
	}

	public double getCmMutationRateModifier() {
		return cmInvasion.getMutationRateModifier();
	}

	public void setCmMutationRateModifier(double mutationRateModifier) {
		cmInvasion.setMutationRateModifier(mutationRateModifier);
	}

	@Override
	public String getInvaderName() {
		return TransformableBacteria.class.getSimpleName()
				+ " SIM mutationRateModifier " + getSimMutationRateModifier()
				+ " mutationFitnessThreshold " + getMutationFitnessThreshold()
				+ " CM mutationRateModifier " + getCmMutationRateModifier();
	}

	@Override
	public double getInvasionRate() {
		return invasionRate;
	}

	@Override
	public void setInvasionRate(double invasionRate) {
		if (invasionRate < 0 || invasionRate > 1) {
			throw new IllegalArgumentException(
					"Invasion rate value must be >= 0 and <= 1, it is "
							+ invasionRate);
		}
		this.invasionRate = invasionRate;
	}
}
