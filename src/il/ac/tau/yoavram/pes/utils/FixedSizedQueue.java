package il.ac.tau.yoavram.pes.utils;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class FixedSizedQueue<B> implements Serializable, RandomQueue<B> {
	private static final long serialVersionUID = -6436324249586452569L;

	private List<B> list;
	private int limit;

	public FixedSizedQueue(int limit) {
		super();
		this.limit = limit;
		list = Lists.newArrayListWithCapacity(limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#add(B)
	 */
	@Override
	public B add(B b) {
		B ret = null;
		if (list.size() >= limit) {
			ret = list.remove(0);
		}
		list.add(b);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#remove()
	 */
	@Override
	public B remove() {
		if (list.size() > 0)
			return list.remove(0);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#randomRemove()
	 */
	@Override
	public B randomRemove() {
		int rand = RandomUtils.nextInt(0, list.size() - 1);
		return list.remove(rand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#random()
	 */
	@Override
	public B randomGet() {
		int rand = RandomUtils.nextInt(0, list.size() - 1);
		return list.get(rand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.pes.utils.RandomQueue#get()
	 */
	@Override
	public B get() {
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		while (size() > limit && size() > 0) {
			remove();
		}
	}
}
