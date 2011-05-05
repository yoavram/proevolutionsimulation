package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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
	private List<float[]> memory;

	// private transient int[] recycle;

	public GenomicMemory() {
		INSTANCE = this;
	}

	public void init() {
		memory = Lists.newArrayListWithCapacity(getCapacity() + 1);
		// recycle = null;
	}

	public static GenomicMemory getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public void addGenome(float[] genome) {
		// int len = genome.length;
		/*
		 * if (recycle == null || recycle.length != len) { recycle = new
		 * int[len]; }
		 */
		// System.arraycopy(genome, 0, recycle, 0, len);
		memory.add(Arrays.copyOf(genome, genome.length));
		// recycle = null;
		while (memory.size() > getCapacity()) {
			// recycle =
			memory.remove(0);
		}
	}

	/**
	 * returns a random genome from the memory.
	 * 
	 * @return a genome.
	 */
	public float[] getRandomGenome() {
		if (memory.size() < 1) {
			return null;
		}
		int rand = RandomUtils.nextInt(0, memory.size() - 1);
		// recycle =
		return memory.remove(rand);
		// return recycle;
	}

	public void clear() {
		memory.clear();
		// recycle = null;
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
