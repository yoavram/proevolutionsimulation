package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

public class SimCmInvasion implements Invasion<Bacteria, Bacteria> {
	// private static final Logger logger =
	// Logger.getLogger(SimCmInvasion.class);
	private double invasionRate;
	protected SimInvasion simInvasion = new SimInvasion();
	protected SimInvasion cmInvasion = new SimInvasion();

	public void init() {
		cmInvasion.setFitnessThreshold(1.0);
	}

	@Override
	public List<List<Bacteria>> invade(List<List<Bacteria>> populations) {
		for (List<Bacteria> population : populations) {
			for (int i = 0; i < population.size(); i++) {
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					Bacteria e = simInvasion.transform(population.get(i));
					population.set(i, e);
				} else {
					Bacteria e = simInvasion.transform(population.get(i));
					population.set(i, e);
				}
			}
		}
		return populations;
	}

	public double getFitnessThreshold() {
		return simInvasion.getFitnessThreshold();
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		simInvasion.setFitnessThreshold(fitnessThreshold);
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
		return Bacteria.class.getSimpleName() + " SIM mutationRateModifier "
				+ getSimMutationRateModifier() + " fitnessThreshold "
				+ getFitnessThreshold() + " CM mutationRateModifier "
				+ getCmMutationRateModifier();
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
