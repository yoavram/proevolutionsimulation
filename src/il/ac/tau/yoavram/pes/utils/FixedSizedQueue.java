package il.ac.tau.yoavram.pes.utils;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class FixedSizedQueue<B> implements Serializable {
	private static final long serialVersionUID = -6436324249586452569L;

	private List<B> list;
	private int limit;

	public FixedSizedQueue(int limit) {
		super();
		this.limit = limit;
		list = Lists.newArrayListWithCapacity(limit);
	}

	/**
	 * add b to the queue. if queue is full, remove the oldest item in the
	 * queue and return it. otherwise return null.
	 * 
	 * @param b
	 *            item to add
	 * @return oldest item in the queue that was removed to allow space for
	 *         input, or null if the queue was not full.
	 */
	public B add(B b) {
		B ret = null;
		if (list.size() >= limit) {
			ret = list.remove(0);
		}
		list.add(b);
		return ret;
	}

	/**
	 * randomly choose an item from the queue, remove it and return it.
	 * 
	 * @return the randomly removed item
	 */
	public B randomRemove() {
		int rand = RandomUtils.nextInt(0, list.size() - 1);
		return list.remove(rand);
	}

	/**
	 * randomly choose an item from the queue and return it, leaving it in
	 * the queue.
	 * 
	 * @return randomly chosen item
	 */
	public B random() {
		int rand = RandomUtils.nextInt(0, list.size() - 1);
		return list.get(rand);
	}

	/**
	 * returns the number of items in the queue
	 */
	public int size() {
		return list.size();
	}
}
