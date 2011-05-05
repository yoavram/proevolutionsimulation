package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * This simple bacteria is haploid, asexual, non-recombinating. It is
 * hypermutatung, including with stress-induction. </br> Note this class is
 * changed from the class <code>Bacteria</code> that was used for the first
 * SIMBA simulations. This is reflected in a different serial version ID, and
 * therefore serialized models of the earlier class cannot be deserialized with
 * this class. </br> For the older class see code for class
 * <code>Bacteria</code> from version <em>Baptiste</em> or
 * <code>pes2010.jar</code>.
 * 
 * @author yoavram
 * 
 */
public class SimpleBacteria implements Serializable, Bacteria {
	private static final long serialVersionUID = 4923704443786823232L;

	private static final Logger logger = Logger.getLogger(SimpleBacteria.class);

	private static final double DEFAULT_FITNESS = -1;
	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY = new int[0];

	protected static SimpleBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	protected int numberOfDeleteriousHousekeepingAlleles;
	protected int numberOfHousekeepingGenes;
	protected int[] environmentalAlleles;
	protected double mutationRate;
	protected double selectionCoefficient;
	protected double beneficialMutationProbability;
	private double fitnessThreshold = 0;
	private double mutationRateModifier = 1;

	protected transient double fitness = DEFAULT_FITNESS;
	protected transient long update = DEFAULT_UPDATE;

	public SimpleBacteria() {
		environmentalAlleles = EMPTY_INT_ARRAY;
		numberOfDeleteriousHousekeepingAlleles = 0;
	}

	public SimpleBacteria(SimpleBacteria other) {
		this();
		copy(other);
	}

	/**
	 * should be overridden if new members are given
	 * 
	 * @param other
	 */
	protected void copy(SimpleBacteria other) {
		environmentalAlleles = EMPTY_INT_ARRAY;
		if (other.environmentalAlleles.length > 0)
			environmentalAlleles = Arrays.copyOf(other.environmentalAlleles,
					other.environmentalAlleles.length);

		numberOfHousekeepingGenes = other.numberOfHousekeepingGenes;
		numberOfDeleteriousHousekeepingAlleles = other.numberOfDeleteriousHousekeepingAlleles;
		mutationRate = other.mutationRate;
		selectionCoefficient = other.selectionCoefficient;
		beneficialMutationProbability = other.beneficialMutationProbability;
		fitnessThreshold = other.fitnessThreshold;
		mutationRateModifier = other.mutationRateModifier;

		fitness = DEFAULT_FITNESS;
		update = DEFAULT_UPDATE;
	}

	/**
	 * should be overridden by new types
	 * 
	 * @param other
	 */
	protected SimpleBacteria create() {
		logger.debug("creating new bacteria");
		return new SimpleBacteria();
	}

	protected SimpleBacteria getTrash() {
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
	public SimpleBacteria spawn() {
		SimpleBacteria recycled = null;
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
	public SimpleBacteria reproduce() {
		// logger.debug(getID() + " reproducing");
		SimpleBacteria child = spawn();
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
		int gene = RandomUtils.nextInt(0, numberOfHousekeepingGenes
				+ environmentalAlleles.length - 1);
		double rand = RandomUtils.nextDouble();

		if (gene < numberOfHousekeepingGenes) {
			if (rand < getBeneficialMutationProbability()
					&& numberOfDeleteriousHousekeepingAlleles > 0) {
				numberOfDeleteriousHousekeepingAlleles--;
			} else if (numberOfDeleteriousHousekeepingAlleles < numberOfHousekeepingGenes) {
				numberOfDeleteriousHousekeepingAlleles++;
			}
		} else {
			int newAllele = -1;
			gene -= numberOfHousekeepingGenes;
			if (rand < getBeneficialMutationProbability())
				newAllele = getEnvironment().getIdealAllele(gene);
			else if (rand < 2 * getBeneficialMutationProbability())
				newAllele = (getEnvironment().getIdealAllele(gene) + 1) % 2;
			else
				newAllele = 2;
			environmentalAlleles[gene] = newAllele;
		}
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
			double s = getSelectionCoefficient();
			int deleteriousMutations = numberOfDeleteriousHousekeepingAlleles;
			for (int gene = 0; gene < environmentalAlleles.length; gene++) {
				if (getEnvironment().getIdealAllele(gene) != environmentalAlleles[gene]) {
					deleteriousMutations++;
				}
			}
			fitness = Math.pow((1 - s), deleteriousMutations);
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
		return obj instanceof SimpleBacteria
				&& this.getID() == ((Bacteria) obj).getID();
	}

	protected Environment getEnvironment() {
		return SimpleEnvironment.getInstance();
	}

	public int[] getEnvironmentalAlleles() {
		return environmentalAlleles;
	}

	public void setEnvironmentalAlleles(int[] environmentalAlleles) {
		this.environmentalAlleles = environmentalAlleles;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	@Override
	public double getMutationRate() {
		if ((isSim() && getFitness() < getFitnessThreshold()) || isCm()) {
			return getMutationRateModifier() * mutationRate;
		} else {
			return mutationRate;
		}
	}

	public void setSelectionCoefficient(double selectionCoefficient) {
		this.selectionCoefficient = selectionCoefficient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#getSelectionCoefficient()
	 */
	@Override
	public double getSelectionCoefficient() {
		return selectionCoefficient;
	}

	public double getBeneficialMutationProbability() {
		return beneficialMutationProbability;
	}

	public void setBeneficialMutationProbability(
			double beneficialMutationProbability) {
		this.beneficialMutationProbability = beneficialMutationProbability;
	}

	public int getNumberOfDeleteriousHousekeepingGenes() {
		return numberOfDeleteriousHousekeepingAlleles;
	}

	public void setNumberOfDeleteriousHousekeepingGenes(
			int numberOfDeleteriousHousekeepingGenes) {
		this.numberOfDeleteriousHousekeepingAlleles = numberOfDeleteriousHousekeepingGenes;
	}

	public int getNumberOfHousekeepingGenes() {
		return numberOfHousekeepingGenes;
	}

	public void setNumberOfHousekeepingGenes(int numberOfHousekeepingGenes) {
		this.numberOfHousekeepingGenes = numberOfHousekeepingGenes;
	}

	public void setFitnessThreshold(double fitnessThreshold) {
		this.fitnessThreshold = fitnessThreshold;
	}

	public double getFitnessThreshold() {
		return fitnessThreshold;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#isMutator()
	 */
	@Override
	public boolean isMutator() {
		return getFitness() < getFitnessThreshold();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#isSim()
	 */
	@Override
	public boolean isSim() {
		return getMutationRateModifier() > 1 && getFitnessThreshold() > 0
				&& getFitnessThreshold() < 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.Bacteria#isCm()
	 */
	@Override
	public boolean isCm() {
		return getMutationRateModifier() > 1 && getFitnessThreshold() == 1;
	}

	@Override
	public double getTransformationRate() {
		return 0;
	}
}
