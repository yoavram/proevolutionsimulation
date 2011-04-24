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

	public GenomicMemory() {
		INSTANCE = this;
		memory = Lists.newLinkedList();
	}

	public static GenomicMemory getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public void addGenome(int[] genome) {
		memory.push(genome);
		while (memory.size() > getCapacity()) {
			memory.removeLast();
		}
	}

	public int[] getRandomGenome() {
		if (memory.size() > 0)
			return memory.remove(RandomUtils.nextInt(0, memory.size() - 1));
		else
			return null;
	}

	public void clear() {
		memory.clear();
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}
}
