package il.ac.tau.yoavram.pes;

import java.util.List;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

public abstract class AbstractInvasion<T, E extends T> implements
		Invasion<T, E> {

	protected double invasionRate;

	@Override
	public List<List<T>> invade(List<List<T>> populations) {
		for (List<T> population : populations) {
			for (int i = 0; i < population.size(); i++) {
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					E e = transform(population.get(i));
					population.set(i, e);
				}
			}
		}
		return populations;
	}

	protected abstract E transform(T t);

	public double getInvasionRate() {
		return invasionRate;
	}

	public void setInvasionRate(double invasionRate) {
		if (invasionRate < 0 || invasionRate > 1) {
			throw new IllegalArgumentException(
					"Invasion rate value must be >= 0 and <= 1, it is "
							+ invasionRate);
		}
		this.invasionRate = invasionRate;
	}

}