package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.LinkedList;

import com.google.common.collect.Lists;

public class GenomicMemory {

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

	public void addGenome(int[] genome) {
		memory.push(genome);
		while (memory.size() > getCapacity()) {
			memory.removeLast();
		}
	}

	public int[] getRandomGenome() {
		return memory.remove(RandomUtils.nextInt(0, memory.size() - 1));
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
