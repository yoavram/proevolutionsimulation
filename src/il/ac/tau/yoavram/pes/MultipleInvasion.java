package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

public class MultipleInvasion<T, E extends T> extends AbstractInvasion<T, E> {
	private final static Logger logger = Logger
			.getLogger(MultipleInvasion.class);

	private List<AbstractInvasion<T, E>> invasions;

	public MultipleInvasion() {
		invasions = Lists.newArrayList();
	}

	public void init() {
		double rate = 0;
		for (Invasion<T, E> inv : getInvasions()) {
			rate += inv.getInvasionRate();
		}
		setInvasionRate(rate);
	}

	@Override
	protected E transform(T t) {
		double rand = RandomUtils.nextDouble();
		double sumProb = 0;
		for (AbstractInvasion<T, E> inv : getInvasions()) {
			sumProb += inv.getInvasionRate() / getInvasionRate();
			if (rand <= sumProb) {
				return inv.transform(t);
			}
		}
		String msg = String.format(
				"Operation failed, probabilities did not sum to 1: sumProb=%f",
				sumProb);
		logger.error(msg);
		throw new RuntimeException(msg);
	}

	@Override
	public String getInvaderName() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		for (int i = 0; i < getInvasions().size() - 1; i++) {
			sb.append(getInvasions().get(i).getInvaderName());
			sb.append(", ");
		}
		if (getInvasions().size() > 0) {
			sb.append(getInvasions().get(getInvasions().size() - 1)
					.getInvaderName());
		}
		return sb.toString();
	}

	public void setInvasions(List<AbstractInvasion<T, E>> invasions) {
		this.invasions = invasions;
	}

	public List<AbstractInvasion<T, E>> getInvasions() {
		return invasions;
	}
}
