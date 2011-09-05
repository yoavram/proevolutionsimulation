package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

import javax.lang.model.element.Modifier;

import org.apache.log4j.Logger;

public class SexyBacteria implements Bacteria {

	private static final long serialVersionUID = 641538952239456807L;
	private static final Logger logger = Logger.getLogger(SexyBacteria.class);

	private static final double DEFAULT_FITNESS = -1;
	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY = new int[0];

	protected static SexyBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	protected int[] alleles;
	protected double mutationRate;
	protected double transformationRate;
	protected double selectionCoefficient;
	private double fitnessThreshold = 0;
	private double mutationRateModifier = 1;
	private double transformationRateModifier = 1;
	private boolean sim = false;
	private boolean sir = false;
	private boolean cr = false;
	private boolean cm = false;

	protected transient double fitness = DEFAULT_FITNESS;
	protected transient long update = DEFAULT_UPDATE;

	public SexyBacteria() {
		alleles = EMPTY_INT_ARRAY;
	}

	public SexyBacteria(SexyBacteria other) {
		this();
		copy(other);
	}

	/**
	 * should be overridden if new members are given
	 * 
	 * @param other
	 */
	protected void copy(SexyBacteria other) {
		alleles = EMPTY_INT_ARRAY;
		if (other.alleles.length > 0)
			alleles = Arrays.copyOf(other.alleles, other.alleles.length);

		mutationRate = other.mutationRate;
		transformationRate = other.transformationRate;
		selectionCoefficient = other.selectionCoefficient;
		fitnessThreshold = other.fitnessThreshold;
		mutationRateModifier = other.mutationRateModifier;
		transformationRateModifier = other.transformationRateModifier;

		fitness = DEFAULT_FITNESS;
		update = DEFAULT_UPDATE;
	}

	/**
	 * should be overridden by new types
	 * 
	 * @param other
	 */
	protected SexyBacteria create() {
		logger.debug("creating new " + SexyBacteria.class.getName());
		return new SexyBacteria();
	}

	protected SexyBacteria getTrash() {
		return trash;
	}

	protected void setTrash() {
		trash = this;
	}

	protected void removeTrash() {
		trash = null;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		fitness = DEFAULT_FITNESS;
		update = DEFAULT_UPDATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#die()
	 */
	@Override
	public void die() {
		setTrash();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#spawn()
	 */
	@Override
	public SexyBacteria spawn() {
		SexyBacteria recycled = null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#reproduce()
	 */
	@Override
	public SexyBacteria reproduce() {
		// logger.debug(getID() + " reproducing");
		SexyBacteria child = spawn();
		int numOfMutations = RandomUtils.nextPoisson(getMutationRate());
		if (numOfMutations > 0) {
			logger.debug("new organism " + getID() + " has " + numOfMutations
					+ " mutations");
		}
		for (int i = 0; i < numOfMutations; i++) {
			child.mutate();
		}
		return child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#mutate()
	 */
	@Override
	public void mutate() {
		int gene = RandomUtils.nextInt(0, alleles.length);
		double rand = RandomUtils.nextDouble();

		switch (getEnvironment().getGeneType(gene)){
			case Fitness:
				int allele = alleles[gene];
				if (rand < 0.5)
					allele = (allele + 1) % 3;
				else
					allele = (allele + 2) % 3;
				alleles[gene] = allele;
			default:
				allele = alleles[gene];
				if (rand<0.5)
					allele = Math.min(allele+1, alleles.length);
				else
					allele = Math.max(allele-1,0);
				alleles[gene]=allele;				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#getFitness()
	 */
	@Override
	public int transform(int[] genome) {
		int length = RandomUtils.nextInt(1, getAlleles().length);
		int count = 0;
		int pos = RandomUtils.nextInt(0, getAlleles().length - 1);

		while (count < length) {
			if (pos >= alleles.length) {
				pos -= alleles.length;
			}
			alleles[pos] = genome[pos];
			pos++;
			count++;
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#getFitness()
	 */
	@Override
	public double getFitness() {
		if (fitness == -1
				|| getEnvironment().getLastEnvironmentalChange() > update) {
			int deleteriousMutations = 0;
			for (int gene = 0; gene < alleles.length; gene++) {
				if (getEnvironment().getIdealAllele(gene) != alleles[gene]) {
					deleteriousMutations++;
				}
			}
			fitness = Math.pow((1 - getSelectionCoefficient()),
					deleteriousMutations);
			update = Simulation.getInstance().getTick();
		}
		return fitness;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#getID()
	 */
	@Override
	public int getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SexyBacteria
				&& this.getID() == ((Bacteria) obj).getID();
	}

	protected ModifierEnvironment getEnvironment() {
		return ModifierEnvironment.getInstance();
	}

	public void setSelectionCoefficient(double selectionCoefficient) {
		this.selectionCoefficient = selectionCoefficient;
	}

	public double getSelectionCoefficient() {
		return selectionCoefficient;
	}

	public int[] getAlleles() {
		return alleles;
	}

	public void setAlleles(int[] alleles) {
		this.alleles = alleles;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setTransformationRate(double transformationRate) {
		this.transformationRate = transformationRate;
	}

	@Override
	public double getMutationRate() {
		if (isMutator()) {
			return getMutationRateModifier() * mutationRate;
		} else {
			return mutationRate;
		}
	}

	@Override
	public double getTransformationRate() {
		if (isRecombinator()) {
			return getTransformationRateModifier() * transformationRate;
		} else {
			return transformationRate;
		}
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
	}

	@Override
	public double getFitnessThreshold() {
		return fitnessThreshold;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = transformationRateModifier;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

	public double getTransformationRateModifier() {
		return transformationRateModifier;
	}

	public boolean isMutator() {
		return ((isSim() && getFitness() < getFitnessThreshold()) || isCm());
	}

	public boolean isRecombinator() {
		return ((isSir() && getFitness() < getFitnessThreshold()) || isCr());
	}

	public void setSim(boolean sim) {
		this.sim = sim;
	}

	public void setCm(boolean cm) {
		this.cm = cm;
	}

	public void setCr(boolean cr) {
		this.cr = cr;
	}

	public boolean isSim() {
		return sim;
	}

	public boolean isCm() {
		return cm;
	}

	public boolean isCr() {
		return cr;
	}

	public boolean isSir() {
		return sir;
	}

	public void setSir(boolean sir) {
		this.sir = sir;
	}

}
