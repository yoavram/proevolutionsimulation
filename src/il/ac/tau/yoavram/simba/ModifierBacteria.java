/**
 * 
 */
package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * @author yoavram
 * @since 8/8/2012
 */
public class ModifierBacteria implements Bacteria {
	private static final long serialVersionUID = 2097663119090595125L;
	private static final Logger logger = Logger.getLogger(ModifierBacteria.class);

	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY = new int[0];
	private static final int DEFAULT_INT = -1;
	
	protected static ModifierBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;
	
	protected double allelesPerLocus = 11;
	protected int[] alleles;
	protected double mutationRate;
	protected double transformationRate;
	protected double selectionCoefficient;
	protected double fitnessThreshold = Double.POSITIVE_INFINITY;
	protected double mutationRateModifier = 1;
	protected double transformationRateModifier = 1;
	
	protected transient int harmfulAlleles = DEFAULT_INT;
	protected transient long update = DEFAULT_UPDATE;

	public ModifierBacteria() {
		alleles = EMPTY_INT_ARRAY;
	}

	public ModifierBacteria(ModifierBacteria other) {
		this();
		copy(other);
	}
	
	/**
	 * should be overridden if new members are given
	 * 
	 * @param other
	 */
	protected void copy(ModifierBacteria other) {
		alleles = EMPTY_INT_ARRAY;
		if (other.alleles.length > 0)
			alleles = Arrays.copyOf(other.alleles, other.alleles.length);

		mutationRate = other.mutationRate;
		transformationRate = other.transformationRate;
		selectionCoefficient = other.selectionCoefficient;
		allelesPerLocus = other.allelesPerLocus;
		fitnessThreshold = other.fitnessThreshold;
		mutationRateModifier = other.mutationRateModifier;
		transformationRateModifier = other.transformationRateModifier;
		
		harmfulAlleles = DEFAULT_INT;
		update = DEFAULT_UPDATE;	
	}
	
	/**
	 * should be overridden by new types
	 * 
	 * @param other
	 */
	protected ModifierBacteria create() {
		return new ModifierBacteria();
	}

	protected ModifierBacteria getTrash() {
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
	}
	
	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#die()
	 */
	@Override
	public void die() {
		setTrash();

	}

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#spawn()
	 */
	@Override
	public ModifierBacteria spawn() {
		ModifierBacteria recycled = null;
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

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#reproduce()
	 */
	@Override
	public Bacteria reproduce() {
		ModifierBacteria child = spawn();
		int numOfMutations = RandomUtils.nextPoisson(getMutationRate());
		for (int i = 0; i < numOfMutations; i++) {
			child.mutate();
		}
		logger.debug(String.format("Tick %d: %s reproduced, child is %s",
				Simulation.getInstance().getTick(), toString(),
				child.toString()));
		return child;
	}

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#mutate()
	 */
	@Override
	public void mutate() {
		int gene = RandomUtils.nextInt(0, alleles.length-1);
		int currentAllele = alleles[gene];
		int newAllele = -1;
		// if currentAllele is favorable, it will change to harmful with probability 1
		// if currentAllele is harmful, it will change to favorable with probability 1/(allelesPerLocus-1)
		double rand = RandomUtils.nextDouble();
		for (int i = 1; i < allelesPerLocus; i++) {
			if (rand <= i / (allelesPerLocus-1)) {
				newAllele = (currentAllele + i) % (int) allelesPerLocus;
			}
		}
		alleles[gene] = newAllele;
		logger.debug(String.format(
				"Tick %d: %s mutated in locus %d from %d to %d", Simulation
						.getInstance().getTick(), toString(), gene,
				currentAllele, newAllele));
	}
	
	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#transform(int[])
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
		return obj instanceof ModifierBacteria
				&& this.getID() == ((Bacteria) obj).getID();
	}
	
	protected SimpleEnvironment getEnvironment() {
		return SimpleEnvironment.getInstance();
	}

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#getAlleles()
	 */
	@Override
	public int[] getAlleles() {
		return alleles;
	}

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#getMutationRate()
	 */
	@Override
	public double getMutationRate() {
		if (isMutator()) {
			return getMutationRateModifier() * mutationRate;
		} else {
			return mutationRate;
		}
	}

	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#getTransformationRate()
	 */
	@Override
	public double getTransformationRate() {
		if (isRecombinator()) {
			return getTransformationRateModifier() * transformationRate;
		} else {
			return transformationRate;
		}
	}
	
	/* (non-Javadoc)
	 * @see il.ac.tau.yoavram.simba.Bacteria#getFitnessThreshold()
	 */
	@Override
	public double getFitnessThreshold() {
		return fitnessThreshold;
	}

	public double getAllelesPerLocus() {
		return allelesPerLocus;
	}

	public void setAllelesPerLocus(double allelesPerLocus) {
		this.allelesPerLocus = allelesPerLocus;
	}

	public double getSelectionCoefficient() {
		return selectionCoefficient;
	}

	public void setSelectionCoefficient(double selectionCoefficient) {
		this.selectionCoefficient = selectionCoefficient;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	public double getTransformationRateModifier() {
		return transformationRateModifier;
	}

	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = transformationRateModifier;
	}

	public int getHarmfulAlleles() {
		return harmfulAlleles;
	}

	public void setHarmfulAlleles(int harmfulAlleles) {
		this.harmfulAlleles = harmfulAlleles;
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

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
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
