package il.ac.tau.yoavram.pes.utils;

public interface RandomQueue<B> {

	/**
	 * add b to the queue. if queue is full, remove the oldest item in the queue
	 * and return it (FIFO). otherwise return null.
	 * 
	 * @param b
	 *            item to add
	 * @return oldest item in the queue that was removed to allow space for
	 *         input, or null if the queue was not full.
	 */
	public abstract B add(B b);

	/**
	 * remove the oldest item in the queue and return it (FIFO).
	 * 
	 * @return the oldest item in the queue that is now removed or null if the queue is empty.
	 */
	public abstract B remove();

	/**
	 * randomly choose an item from the queue, remove it and return it.
	 * 
	 * @return the randomly removed item
	 */
	public abstract B randomRemove();

	/**
	 * randomly choose an item from the queue and return it, leaving it in the
	 * queue.
	 * 
	 * @return randomly chosen item or null if the queue is empty
	 */
	public abstract B randomGet();
	
	/**
	 * return the oldest item in the queue .
	 * 
	 * @return the oldest item in the queue or null if the queue is empty.
	 */
	public abstract B get();

	/**
	 * returns the number of items in the queue
	 */
	public abstract int size();

}