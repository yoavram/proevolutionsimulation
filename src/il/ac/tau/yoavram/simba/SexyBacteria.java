package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class SexyBacteria implements Bacteria {

	private static final long serialVersionUID = 641538952239456807L;
	private static final Logger logger = Logger.getLogger(SexyBacteria.class);

	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY = new int[0];
	private static final int DEFAULT_INT = -1;
	private static final double RATE_CONSTANT = 0.1;

	protected static SexyBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	protected int[] alleles;
	protected double mutationRate;
	protected double transformationRate;
	protected double selectionCoefficient;

	protected transient double fitnessThreshold = Double.NaN;
	protected transient double mutationRateModifier = Double.NaN;
	protected transient double transformationRateModifier = Double.NaN;
	protected transient int harmfulAlleles = DEFAULT_INT;
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

		harmfulAlleles = DEFAULT_INT;
		update = DEFAULT_UPDATE;
		fitnessThreshold = Double.NaN;
		mutationRateModifier = Double.NaN;
		transformationRateModifier = Double.NaN;
	}

	/**
	 * should be overridden by new types
	 * 
	 * @param other
	 */
	protected SexyBacteria create() {
		// logger.debug("creating new " + SexyBacteria.class.getSimpleName());
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
		harmfulAlleles = DEFAULT_INT;
		update = DEFAULT_UPDATE;
		fitnessThreshold = Double.NaN;
		mutationRateModifier = Double.NaN;
		transformationRateModifier = Double.NaN;
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
		SexyBacteria child = spawn();
		int numOfMutations = RandomUtils.nextPoisson(getMutationRate());
		for (int i = 0; i < numOfMutations; i++) {
			child.mutate();
		}
		logger.debug(String.format("Tick %d: %s reproduced, child is %s",
				Simulation.getInstance().getTick(), toString(),
				child.toString()));
		return child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#mutate()
	 */
	@Override
	public void mutate() {
		int gene = getEnvironment().getRandomFitnessGene();
		double rand = RandomUtils.nextDouble();
		// TODO mutate modifiers??
		int currentAllele = alleles[gene];
		int newAllele = -1;
		if (rand < 0.5)
			newAllele = (currentAllele + 1) % 3;
		else
			newAllele = (currentAllele + 2) % 3;
		alleles[gene] = newAllele;
		logger.debug(String.format(
				"Tick %d: %s mutated in locus %d from %d to %d", Simulation
						.getInstance().getTick(), toString(), gene,
				currentAllele, newAllele));
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
		int pos = RandomUtils.nextInt(0, getAlleles().length / 2);

		logger.debug(String.format(
				"Tick %d: %s transforming %d loci from locus %d", Simulation
						.getInstance().getTick(), toString(), length, pos));
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
		double fitness = Math.pow((1 - getSelectionCoefficient()),
				numberOfHarmfulAlleles());
		return fitness;
	}

	protected int numberOfHarmfulAlleles() {
		if (harmfulAlleles == DEFAULT_INT
				|| getEnvironment().getLastEnvironmentalChange() > update) {

			harmfulAlleles = 0;
			for (int gene = 0; gene < alleles.length; gene++) {
				int idealAllele = getEnvironment().getIdealAllele(gene);
				if (idealAllele != -1 && idealAllele != alleles[gene]) {
					harmfulAlleles++;
				}
			}
			update = Simulation.getInstance().getTick();
		}
		return harmfulAlleles;
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

	private void setModifier(double modifier, int[] modifierIndex) {
		if (modifierIndex.length > 0) {
			int allele = (int) (modifier / modifierIndex.length);
			for (int gene : modifierIndex) {
				alleles[gene] = allele;
			}
			int remainder = (int) (modifier % modifierIndex.length);
			alleles[modifierIndex[0]] += remainder;
		}

	}

	private double clacModifier(int[] modifierIndex) {
		int modifierSum = 0;
		for (int modifier : modifierIndex) {
			modifierSum += alleles[modifier];
		}
		return modifierSum;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = Double.NaN;
		setModifier(mutationRateModifier / RATE_CONSTANT, getEnvironment()
				.getMutationRateModifiers());
	}

	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = Double.NaN;
		setModifier(transformationRateModifier / RATE_CONSTANT,
				getEnvironment().getTransformationRateModifiers());
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = Double.NaN;
		setModifier(fitnessThreshold, getEnvironment().getThresholdModifiers());
	}

	public double getMutationRateModifier() {
		if (Double.isNaN(mutationRateModifier)) {
			mutationRateModifier = clacModifier(getEnvironment()
					.getMutationRateModifiers()) * RATE_CONSTANT;
			if (mutationRateModifier < 0)
				mutationRateModifier = 1 / (-mutationRateModifier);
		}
		return mutationRateModifier;
	}

	public double getTransformationRateModifier() {
		if (Double.isNaN(transformationRateModifier)) {
			transformationRateModifier = clacModifier(getEnvironment()
					.getTransformationRateModifiers()) * RATE_CONSTANT;
			if (transformationRateModifier < 0)
				transformationRateModifier = 1 / (-transformationRateModifier);
		}
		return transformationRateModifier;

	}

	@Override
	public double getFitnessThreshold() {
		if (Double.isNaN(fitnessThreshold)) {
			fitnessThreshold = clacModifier(getEnvironment()
					.getThresholdModifiers());
		}
		return fitnessThreshold;
	}

	public boolean isMutator() {
		return ((isSim() && numberOfHarmfulAlleles() >= getFitnessThreshold()) || isCm());
	}

	public boolean isRecombinator() {
		return ((isSir() && numberOfHarmfulAlleles() >= getFitnessThreshold()) || isCr());
	}

	public boolean isSim() {
		return getMutationRateModifier() != 1 && getFitnessThreshold() > 0
				&& getFitnessThreshold() <= alleles.length;
	}

	public boolean isSir() {
		return getTransformationRateModifier() != 1
				&& getFitnessThreshold() > 0
				&& getFitnessThreshold() <= alleles.length;
	}

	public boolean isCm() {
		return getFitnessThreshold() == 0 && getMutationRateModifier() != 1;
	}

	public boolean isCr() {
		return getFitnessThreshold() == 0
				&& getTransformationRateModifier() != 1;
	}

	@Override
	public String toString() {
		String mutator = "NM";
		if (isCm())
			mutator = "CM";
		else if (isSim()) {
			if (isMutator())
				mutator = "SIM+";
			else
				mutator = "SIM-";
		}
		String recombinator = "NR";
		if (isCr())
			recombinator = "CR";
		else if (isSir()) {
			if (isRecombinator())
				recombinator = "SIR+";
			else
				recombinator = "SIR-";
		}
		return String.format("%s(%d %s %s)", getClass().getSimpleName(),
				getID(), mutator, recombinator);
	}
}
