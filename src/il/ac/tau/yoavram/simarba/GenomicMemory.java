package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;

import com.google.common.collect.Lists;

/**
 * 
 * @author yoavram
 * @version Charles
 */
public class GenomicMemory implements Serializable {

	private static final long serialVersionUID = 3082455981812409957L;

	private static GenomicMemory INSTANCE = null;

	private int capacity = 0;
	private LinkedList<int[]> memory;
	private transient int[] recycle;

	public GenomicMemory() {
		INSTANCE = this;
	}

	public void init() {
		memory = Lists.newLinkedList();
		recycle = null;
	}

	public static GenomicMemory getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public void addGenome(int[] genome) {
		int len = genome.length;
		if (recycle == null || recycle.length != len) {
			recycle = new int[len];
		}
		System.arraycopy(genome, 0, recycle, 0, len);
		memory.add(recycle);
		recycle = null;
		while (memory.size() > getCapacity()) {
			recycle = memory.removeFirst();
		}
	}

	/**
	 * returns a random genome from the memory.
	 * 
	 * @return a genome. please do not use this array - only copy from it, as it
	 *         will be reused in the memory.
	 */
	public int[] getRandomGenome() {
		if (memory.size() < 1) {
			return null;
		}
		int rand = RandomUtils.nextInt(0, memory.size() - 1);
		recycle = memory.remove(rand);
		return recycle;
	}

	public void clear() {
		memory.clear();
		recycle = null;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public int size() {
		return memory.size();
	}
}
