package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

public abstract class DoubleInvasion<T, E extends T> implements Invasion<T, E> {

	protected AbstractInvasion<T, E> invasion1;
	protected AbstractInvasion<T, E> invasion2;
	private double invasionRate;

	@Override
	public List<List<T>> invade(List<List<T>> populations) {
		for (List<T> population : populations) {
			for (int i = 0; i < population.size(); i++) {
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					E e = invasion1.transform(population.get(i));
					population.set(i, e);
				} else {
					E e = invasion2.transform(population.get(i));
					population.set(i, e);
				}
			}
		}
		return populations;
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

	@Override
	public String getInvaderName() {
		return invasion1.getInvaderName() + ", " + invasion2.getInvaderName();
	}
}
