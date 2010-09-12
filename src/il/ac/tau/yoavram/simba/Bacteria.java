package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.filters.ClassFilter;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class Bacteria implements Serializable {
	private static final long serialVersionUID = -490159741474792583L;
	private static final Logger logger = Logger.getLogger(Bacteria.class);

	private static final double DEFAULT_FITNESS = -1;
	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY=new int[0];

	protected static Bacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	protected int[] environmentalAlleles;
	protected int[] housekeepingAlleles;
	protected double mutationRate;
	protected double selectionCoefficient;

	protected transient double fitness = DEFAULT_FITNESS;
	protected transient long update = DEFAULT_UPDATE;

	public Bacteria() {
		environmentalAlleles = EMPTY_INT_ARRAY;
		housekeepingAlleles = EMPTY_INT_ARRAY;
	}

	public Bacteria(Bacteria other) {
		this();
		copy(other);
	}

	/**
	 * should be overridden if new members are given
	 * 
	 * @param other
	 */
	protected void copy(Bacteria other) {
		environmentalAlleles = Arrays.copyOf(other.environmentalAlleles,
				other.environmentalAlleles.length);
		housekeepingAlleles = Arrays.copyOf(other.housekeepingAlleles,
				other.housekeepingAlleles.length);
		mutationRate = other.mutationRate;
		selectionCoefficient = other.selectionCoefficient;
		fitness = DEFAULT_FITNESS;
		update = DEFAULT_UPDATE;
	}

	/**
	 * should be overridden by new types
	 * 
	 * @param other
	 */
	protected Bacteria create() {
		return new Bacteria();
	}

	public Bacteria getTrash() {
		return trash;
	}

	public void setTrash(Bacteria bacteria) {
		trash = bacteria;
	}

	public void removeTrash() {
		trash = null;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		fitness = DEFAULT_FITNESS;
		update = DEFAULT_UPDATE;
	}

	public void die() {
		setTrash(this);
	}

	public Bacteria spawn() {
		Bacteria recycled = null;
		if (getTrash() == null)
			recycled = create();
		else {
			recycled = getTrash();
			removeTrash();
			recycled.id = nextID++;
		}
		recycled.copy(this);
		return recycled;
	}

	public Bacteria reproduce() {
		// logger.debug(getID() + " reproducing");
		Bacteria child = spawn();
		int numOfMutations = RandomUtils.nextPoisson(child.getMutationRate());
		if (numOfMutations > 0) {
			logger.debug("new organism " + getID() + " has " + numOfMutations
					+ " mutations");
		}
		for (int i = 0; i < numOfMutations; i++) {
			child.mutate();
		}
		return child;
	}

	public void mutate() {
		int gene = RandomUtils.nextInt(0, housekeepingAlleles.length
				+ environmentalAlleles.length - 1);

		int currentAllele = -1;
		int newAllele = -1;
		if (gene < housekeepingAlleles.length) {
			currentAllele = housekeepingAlleles[gene];
			newAllele = (currentAllele + 1) % 2;
			housekeepingAlleles[gene] = newAllele;
		} else {
			gene -= housekeepingAlleles.length;
			currentAllele = environmentalAlleles[gene];
			newAllele = (currentAllele + RandomUtils.nextInt(1, 2)) % 3;
			environmentalAlleles[gene] = newAllele;
		}
		logger.debug("Mutation at bacteria " + getID() + " gene " + gene
				+ " mutated from " + currentAllele + " to " + newAllele);
	}

	public double getFitness() {
		if (fitness == -1
				|| getEnvironment().getLastEnvironmentalChange() > update) {
			fitness = 1;
			double s = getSelectionCoefficient();
			for (int gene = 0; gene < housekeepingAlleles.length; gene++) {
				if (housekeepingAlleles[gene] != 0) {
					fitness *= (1 - s);
				}
			}
			for (int gene = 0; gene < environmentalAlleles.length; gene++) {
				if (getEnvironment().getIdealAllele(gene) != environmentalAlleles[gene]) {
					fitness *= (1 - s);
				}
			}
			update = Simulation.getInstance().getTick();
		}
		return fitness;
	}

	public void randomize() {
		for (int gene = 0; gene < housekeepingAlleles.length; gene++) {
			housekeepingAlleles[gene] = RandomUtils.nextInt(0, 1);
		}
		for (int gene = 0; gene < environmentalAlleles.length; gene++) {
			environmentalAlleles[gene] = RandomUtils.nextInt(0, 2);
		}
	}

	public int getID() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Bacteria
				&& this.getID() == ((Bacteria) obj).getID();
	}

	private Environment getEnvironment() {
		return ((SimbaModel) Simulation.getInstance().getModel())
				.getEnvironment();
	}

	public int[] getEnvironmentalAlleles() {
		return environmentalAlleles;
	}

	public void setEnvironmentalAlleles(int[] environmentalAlleles) {
		this.environmentalAlleles = environmentalAlleles;
	}

	public int[] getHousekeepingAlleles() {
		return housekeepingAlleles;
	}

	public void setHousekeepingAlleles(int[] housekeepingAlleles) {
		this.housekeepingAlleles = housekeepingAlleles;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setSelectionCoefficient(double selectionCoefficient) {
		this.selectionCoefficient = selectionCoefficient;
	}

	public double getSelectionCoefficient() {
		return selectionCoefficient;
	}

	public static class Filter extends ClassFilter<Bacteria> {
		public Filter() {
			setClazz(Bacteria.class);
		}
	}

}
