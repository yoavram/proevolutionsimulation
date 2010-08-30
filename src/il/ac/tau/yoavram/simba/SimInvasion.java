package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

public class SimInvasion implements Invasion<Bacteria, SimBacteria> {
	// private static final Logger logger = Logger.getLogger(SimInvasion.class);

	private double invasionRate;
	private double fitnessThreshold;
	private double mutationRateModifier;

	@Override
	public List<List<Bacteria>> invade(List<List<Bacteria>> populations) {
		for (List<Bacteria> population : populations) {
			for (Bacteria b : population) {
				population.remove(b);
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					SimBacteria sim = new SimBacteria(b);
					sim.setFitnessThreshold(getFitnessThreshold());
					sim.setMutationRateModifier(getMutationRateModifier());
					b = sim;
				}
				population.add(b);
			}
		}

		return populations;
	}

	@Override
	public String getInvaderName() {
		return SimBacteria.class.getSimpleName();
	}

	public double getInvasionRate() {
		return invasionRate;
	}

	public void setInvasionRate(double invasionRate) {
		if (invasionRate <= 0 || invasionRate > 1) {
			throw new IllegalArgumentException(
					"Invasion rate value must be >0 and <=1, it is "
							+ invasionRate);
		}
		this.invasionRate = invasionRate;
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

}
