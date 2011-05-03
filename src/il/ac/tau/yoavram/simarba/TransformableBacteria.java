package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;
import il.ac.tau.yoavram.simba.Bacteria;

import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

/**
 * Modified from {@link il.ac.tau.yoavram.simba.SimpleBacteria}. Added
 * transformation and explicit genome for all genes, including HK genes.
 * 
 * TODO add mutating and recombinating modifiers of basal rates, thresholds,
 * and increases
 * 
 * @author yoavram
 * @version Charles
 */
public class TransformableBacteria implements Bacteria {
	private static final long serialVersionUID = -7309784949905839395L;

	private static final Logger logger = Logger
			.getLogger(TransformableBacteria.class);

	private static final double DEFAULT_FITNESS = -1;
	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final int[] EMPTY_INT_ARRAY = new int[0];

	private static TransformableBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	private double selectionCoefficient; // STATIC?
	private double beneficialMutationProbability; // STATIC?
	private double deleteriousMutationProbability;// STATIC?

	private int[] alleles; // the genome is assumed to be a cycle
	private double mutationRate;
	private double transformationRate; // transformations per cell per
										// generation
	private double mutationFitnessThreshold = 0;
	private double transformationFitnessThreshold = 0;
	private double mutationRateModifier = 1;
	private double transformationRateModifier = 1;

	protected transient double fitness = DEFAULT_FITNESS;
	protected transient long update = DEFAULT_UPDATE;

	public TransformableBacteria() {
		alleles = EMPTY_INT_ARRAY;
	}

	public TransformableBacteria(TransformableBacteria other) {
		this();
		copy(other);
	}

	/**
	 * should be overridden if new members are given
	 * 
	 * @param other
	 */
	protected void copy(TransformableBacteria other) {
		if (alleles == null || alleles.length != other.alleles.length) {
			alleles = new int[other.alleles.length];
		}
		System.arraycopy(other.alleles, 0, alleles, 0, alleles.length);

		mutationRate = other.mutationRate;
		transformationRate = other.transformationRate;
		selectionCoefficient = other.selectionCoefficient;
		beneficialMutationProbability = other.beneficialMutationProbability;
		deleteriousMutationProbability = other.deleteriousMutationProbability;
		mutationFitnessThreshold = other.mutationFitnessThreshold;
		transformationFitnessThreshold = other.transformationFitnessThreshold;
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
	protected TransformableBacteria create() {
		return new TransformableBacteria();
	}

	protected TransformableBacteria getTrash() {
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

	public void die() {
		if (GenomicMemory.getInstance() != null) {
			GenomicMemory.getInstance().addGenome(alleles);
		}
		setTrash();
	}

	public TransformableBacteria spawn() {
		TransformableBacteria recycled = null;
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

	public TransformableBacteria reproduce() {
		// logger.debug(getID() + " reproducing");
		TransformableBacteria child = spawn();
		int numOfMutations = RandomUtils.nextPoisson(getMutationRate());
		if (numOfMutations > 0) {
			logger.debug(String.format("New Bacteria %d has %d mutations",
					child.getID(), numOfMutations));

			while (numOfMutations-- > 0) {
				child.mutate();
			}
		}
		return child;
	}

	public void mutate() {
		int gene = RandomUtils.nextInt(0, alleles.length - 1);
		double rand = RandomUtils.nextDouble();
		int idealAllele = getEnvironment().getIdealAllele(gene);

		if (rand < getBeneficialMutationProbability()) {
			alleles[gene] = idealAllele;
		} else if (rand < getBeneficialMutationProbability()
				+ getDeleteriousMutationProbability()) {
			// randomly chose one of the non-ideal alleles
			int change = RandomUtils.coinToss() ? 2 : 4;
			// 3 is number of alleles per gene
			alleles[gene] = (idealAllele + change) % 3;
		}

	}

	/**
	 * draw 2 rv to assign start (min) and stop (max+1) point from uniform dist
	 * 0-alleles.length-1. expected length of recombination is n/3 where n is
	 * genome length (http://www.jstor.org/stable/2583917)
	 * 
	 * @param otherAlleles
	 */
	public void recombinate(int[] otherAlleles) {
		if (alleles.length != otherAlleles.length) {
			String msg = "Can't recombinate genomes of different sizes: "
					+ alleles.length + " and " + otherAlleles.length;
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		int start = RandomUtils.nextInt(0, alleles.length - 1);
		int stop = RandomUtils.nextInt(1, alleles.length);

		if (start == stop) {
			System.arraycopy(otherAlleles, 0, alleles, 0, alleles.length);
		}
		if (start < stop) {
			System.arraycopy(otherAlleles, start, alleles, start, stop - start);
		} else {// start>stop
			System.arraycopy(otherAlleles, start, alleles, start,
					alleles.length - start);
			System.arraycopy(otherAlleles, 0, alleles, 0, stop);
		}

		logger.debug(String.format("Recombinated %d alleles", (stop - start)));
	}

	/**
	 * @see <a href=http://dx.doi.org/10.1038%2Fnrmicro844>review</a>
	 */
	@Override
	public void transform() {
		int[] otherAlleles = GenomicMemory.getInstance().getRandomGenome();
		if (otherAlleles == null) {
			logger.warn("Couldn't transform, genomic memory empty");
		} else {
			recombinate(otherAlleles);
		}
	}

	public double getFitness() {
		if (fitness == -1
				|| getEnvironment().getLastEnvironmentalChange() > update) {
			double s = getSelectionCoefficient();
			int deleteriousMutations = 0;
			for (int gene = 0; gene < alleles.length; gene++) {
				if (getEnvironment().getIdealAllele(gene) != alleles[gene]) {
					deleteriousMutations++;
				}
			}
			fitness = Math.pow((1 - s), deleteriousMutations);
			update = Simulation.getInstance().getTick();
		}
		return fitness;
	}

	public int getID() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TransformableBacteria
				&& this.getID() == ((TransformableBacteria) obj).getID();
	}

	protected Environment getEnvironment() {
		return Environment.getInstance();
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setSelectionCoefficient(double selectionCoefficient) {
		this.selectionCoefficient = selectionCoefficient;
	}

	public double getSelectionCoefficient() {
		return selectionCoefficient;
	}

	/**
	 * see http://www.sciencemag.org/cgi/content/full/317/5839/813
	 * 
	 * @return
	 */
	public double getBeneficialMutationProbability() {
		return beneficialMutationProbability;
	}

	public void setBeneficialMutationProbability(
			double beneficialMutationProbability) {
		this.beneficialMutationProbability = beneficialMutationProbability;
	}

	public void setMutationFitnessThreshold(double mutationFitnessThreshold) {
		this.mutationFitnessThreshold = mutationFitnessThreshold;
	}

	public double getMutationFitnessThreshold() {
		return mutationFitnessThreshold;
	}

	public void setMutationRateModifier(double mutationRateModifier) {
		this.mutationRateModifier = mutationRateModifier;
	}

	public double getMutationRateModifier() {
		return mutationRateModifier;
	}

	public void setTransformationRateModifier(double transformationRateModifier) {
		this.transformationRateModifier = transformationRateModifier;
	}

	public double getTransformationRateModifier() {
		return transformationRateModifier;
	}

	public void setTransformationRate(double transformationRate) {
		this.transformationRate = transformationRate;
	}

	public void setAlleles(int[] alleles) {
		this.alleles = alleles;
	}

	public int[] getAlleles() {
		return alleles;
	}

	public void setDeleteriousMutationProbability(
			double deleteriousMutationProbability) {
		this.deleteriousMutationProbability = deleteriousMutationProbability;
	}

	public double getDeleteriousMutationProbability() {
		return deleteriousMutationProbability;
	}

	public void setTransformationFitnessThreshold(
			double transformationFitnessThreshold) {
		this.transformationFitnessThreshold = transformationFitnessThreshold;
	}

	public double getTransformationFitnessThreshold() {
		return transformationFitnessThreshold;
	}

	public boolean isMutator() {
		return getFitness() < getMutationFitnessThreshold();
	}

	public boolean isSim() {
		return getMutationRateModifier() > 1
				&& getMutationFitnessThreshold() > 0
				&& getMutationFitnessThreshold() < 1;
	}

	public boolean isCm() {
		return getMutationRateModifier() > 1
				&& getMutationFitnessThreshold() == 1;
	}

	public boolean isTransformer() {
		return getFitness() < getTransformationFitnessThreshold();
	}

	public boolean isSit() {
		return getTransformationRateModifier() > 1
				&& getTransformationFitnessThreshold() > 0
				&& getTransformationFitnessThreshold() < 1;
	}

	public boolean isCt() {
		return getTransformationRateModifier() > 1
				&& getTransformationFitnessThreshold() == 1;
	}

	public double getMutationRate() {
		if ((isSim() && getFitness() < getMutationFitnessThreshold()) || isCm()) {
			return getMutationRateModifier() * mutationRate;
		} else {
			return mutationRate;
		}
	}

	public double getTransformationRate() {
		if ((isSit() && getFitness() < getTransformationFitnessThreshold())
				|| isCt()) {
			return getTransformationRateModifier() * transformationRate;
		} else {
			return transformationRate;
		}
	}

}
