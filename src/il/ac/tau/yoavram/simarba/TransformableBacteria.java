package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.random.Distribution;
import il.ac.tau.yoavram.pes.utils.RandomUtils;
import il.ac.tau.yoavram.simarba.Environment.GeneType;
import il.ac.tau.yoavram.simba.Bacteria;

import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import com.google.common.collect.Multimap;

/**
 * Modified from {@link il.ac.tau.yoavram.simba.SimpleBacteria}. Added
 * transformation and explicit genome for all genes, including HK genes.
 * 
 * @see <a href=http://dx.doi.org/10.1038%2Fnrmicro844>review</a>
 * 
 * @author yoavram
 * @version Charles
 */
public class TransformableBacteria implements Bacteria {
	private static final long serialVersionUID = 2231196569650656097L;
	private static final Logger logger = Logger
			.getLogger(TransformableBacteria.class);

	private static final double DEFAULT_VALUE = -1;
	private static final long DEFAULT_UPDATE = Long.MAX_VALUE;
	private static final float[] EMPTY_FLOAT_ARRAY = new float[0];

	// modifiers: 0:mut basal rate 1:mut induced rate 2:mut threshold 3:trans
	// basal rate 4:trans induced rate 5:trans threshold
	protected static Multimap<Integer, Integer> modifierPositions;

	protected static Distribution alleleChangeDistribution;
	protected static Distribution mutationRateChangeDistribution;
	protected static Distribution transformationRateChangeDistribution;
	protected static Distribution thresholdChangeDistribution;

	private static TransformableBacteria trash = null;
	private static int nextID = 0;
	private int id = nextID++;

	protected float[] alleles = EMPTY_FLOAT_ARRAY;

	protected double fitness = DEFAULT_VALUE;
	protected double mutationRate = DEFAULT_VALUE;
	protected double mutationThreshold = DEFAULT_VALUE;
	protected double transformationRate = DEFAULT_VALUE;
	protected double transformationThreshold = DEFAULT_VALUE;
	protected transient long fitnessUpdate = DEFAULT_UPDATE;
	protected transient long mutationRateUpdate = DEFAULT_UPDATE;
	protected transient long transformationRateUpdate = DEFAULT_UPDATE;
	protected transient long transformationThresholdUpdate = DEFAULT_UPDATE;
	protected transient long mutationThresholdUpdate = DEFAULT_UPDATE;

	public TransformableBacteria() {
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
			alleles = new float[other.alleles.length];
		}
		System.arraycopy(other.alleles, 0, alleles, 0, alleles.length);
		this.fitness = other.fitness;
		this.mutationRate = other.mutationRate;
		this.mutationThreshold = other.mutationThreshold;
		this.transformationRate = other.transformationRate;
		this.transformationThreshold = other.transformationThreshold;
		this.fitnessUpdate = other.fitnessUpdate;
		this.mutationRateUpdate = other.mutationRateUpdate;
		this.mutationThresholdUpdate = other.mutationThresholdUpdate;
		this.transformationRateUpdate = other.transformationRateUpdate;
		this.transformationThresholdUpdate = other.transformationThresholdUpdate;
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

	protected void notUpdated() {
		update(DEFAULT_UPDATE);
	}

	protected void update(long time) {
		fitnessUpdate = time;
		mutationRateUpdate = time;
		transformationRateUpdate = time;
		mutationThresholdUpdate = time;
		transformationThresholdUpdate = time;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		notUpdated();
	}

	public void die() {
		if (GenomicMemory.getInstance() != null) {
			GenomicMemory.getInstance().addGenome(alleles);
		}
		setTrash();
	}

	public TransformableBacteria spawn() {
		TransformableBacteria recycled = null;
		if (getTrash() == null) {
			recycled = create();
		} else {
			recycled = getTrash();
			removeTrash();
			recycled.id = nextID++;
		}
		recycled.copy(this);
		return recycled;
	}

	public TransformableBacteria reproduce() {
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
		mutate(RandomUtils.nextInt(0, alleles.length - 1));
	}

	public void mutate(int gene) {
		GeneType geneType = getEnvironment().geneType(gene);
		if (!geneType.equals(GeneType.MOD)) {
			// HK or ENV
			float change = (float) alleleChangeDistribution.nextDouble();
			if (getEnvironment().idealAllele(gene) == 1)
				change = -change;
			float nuevo = alleles[gene] + change;
			if (nuevo > 1)
				alleles[gene] = 1;
			else if (nuevo < 0)
				alleles[gene] = 0;
			else
				alleles[gene] = nuevo;
		} else {
			// MODIFIER
			if (modifierPositions.get(2).contains(gene)
					|| modifierPositions.get(5).contains(gene)) {
				// THRESHOLD
				float nuevo = alleles[gene]
						+ (float) thresholdChangeDistribution.nextDouble();
				if (nuevo > 1)
					alleles[gene] = 1;
				else if (nuevo < 0)
					alleles[gene] = 0;
				else
					alleles[gene] = nuevo;
			} else if (modifierPositions.get(0).contains(gene)
					|| modifierPositions.get(1).contains(gene)) {
				// MUTATION RATE
				alleles[gene] = alleles[gene]
						+ (float) mutationRateChangeDistribution.nextDouble();
			} else if (modifierPositions.get(3).contains(gene)
					|| modifierPositions.get(4).contains(gene)) {
				// MUTATION RATE
				alleles[gene] = alleles[gene]
						+ (float) transformationRateChangeDistribution
								.nextDouble();
			}
		}
		notUpdated();
	}

	/**
	 * draw 2 rv to assign start point (0 to n-1) and length (1 to n) from
	 * uniform dist. expected length of recombination is n/2s where n is genome
	 * length
	 * 
	 * @param otherAlleles
	 */
	@Override
	public int recombinate() {
		float[] otherAlleles = GenomicMemory.getInstance().getRandomGenome();
		if (alleles.length != otherAlleles.length) {
			String msg = "Can't recombinate genomes of different sizes: "
					+ alleles.length + " and " + otherAlleles.length;
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		int start = RandomUtils.nextInt(0, alleles.length - 1);
		int length = RandomUtils.nextInt(1, alleles.length);

		if (start + length < alleles.length) {
			System.arraycopy(otherAlleles, start, alleles, start, length);
		} else {
			int firstLen = alleles.length - start;
			System.arraycopy(otherAlleles, start, alleles, start, firstLen);
			int SecondLen = length - firstLen;
			System.arraycopy(otherAlleles, 0, alleles, 0, SecondLen);
		}
		notUpdated();
		return length;
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

	@Override
	public double getFitness() {
		if (fitness == DEFAULT_VALUE
				|| getEnvironment().getLastEnvironmentalChange() > fitnessUpdate) {
			fitness = 1;
			for (int gene = 0; gene < alleles.length; gene++) {
				float ideal = getEnvironment().idealAllele(gene);
				if (ideal != -1) {
					float dist = Math.abs(ideal - alleles[gene]);
					if (dist > 0) {
						double s = getEnvironment().getSelectionCoefficient(
								gene);
						fitness *= (1 - dist * s);
					}
				}
			}
			fitnessUpdate = Simulation.getInstance().getTick();
		}
		return fitness;
	}

	@Override
	public double getMutationRate() {
		if (mutationRate == DEFAULT_VALUE
				|| getEnvironment().getLastEnvironmentalChange() > mutationRateUpdate) {
			int index = 0;
			if (isMutator()) {
				index = 1;
			}
			mutationRate = 0;
			for (int gene : modifierPositions.get(index)) {
				mutationRate += alleles[gene];
			}
			mutationRate = mutationRate / modifierPositions.get(index).size();
			mutationRateUpdate = Simulation.getInstance().getTick();

		}
		return mutationRate;
	}

	@Override
	public double getTransformationRate() {
		if (transformationRate == DEFAULT_VALUE
				|| getEnvironment().getLastEnvironmentalChange() > transformationRateUpdate) {
			int index = 3;
			if (isTransformator()) {
				index = 4;
			}
			transformationRate = 0;
			for (int gene : modifierPositions.get(index)) {
				transformationRate += alleles[gene];
			}
			transformationRate=transformationRate/modifierPositions.get(index).size();
			transformationRateUpdate = Simulation.getInstance().getTick();

		}
		return transformationRate;
	}

	public double getMutationThreshold() {
		if (mutationThreshold == DEFAULT_VALUE
				|| getEnvironment().getLastEnvironmentalChange() > mutationThresholdUpdate) {
			mutationThreshold = 0;
			for (int gene : modifierPositions.get(2)) {
				mutationThreshold += alleles[gene];
			}
			mutationThreshold=mutationThreshold/modifierPositions.get(2).size();
			mutationThresholdUpdate = Simulation.getInstance().getTick();

		}
		return mutationThreshold;
	}

	public double getTransformationThreshold() {
		if (transformationThreshold == DEFAULT_VALUE
				|| getEnvironment().getLastEnvironmentalChange() > transformationThresholdUpdate) {
			transformationThreshold = 0;
			for (int gene : modifierPositions.get(5)) {
				transformationThreshold += alleles[gene];
			}
			transformationThreshold=transformationThreshold/modifierPositions.get(5).size();
			transformationThresholdUpdate = Simulation.getInstance().getTick();

		}
		return transformationThreshold;
	}

	public boolean isMutator() {
		return getFitness() < getMutationThreshold();
	}

	public boolean isTransformator() {
		return getFitness() < getTransformationThreshold();
	}

	public boolean isSim() {
		return getMutationThreshold() > 0;
	}

	public boolean isSit() {
		return getTransformationThreshold() > 0;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setTransformationRate(double transformationRate) {
		this.transformationRate = transformationRate;
	}

	public void setMutationThreshold(double mutationThreshold) {
		this.mutationThreshold = mutationThreshold;
	}

	public void setTransformationThreshold(double transformationThreshold) {
		this.transformationThreshold = transformationThreshold;
	}

	public Distribution getAlleleChangeDistribution() {
		return alleleChangeDistribution;
	}

	public void setAlleleChangeDistribution(
			Distribution alleleChangeDistribution) {
		TransformableBacteria.alleleChangeDistribution = alleleChangeDistribution;
	}

	public Distribution getMutationRateChangeDistribution() {
		return mutationRateChangeDistribution;
	}

	public void setMutationRateChangeDistribution(
			Distribution mutationRateChangeDistribution) {
		TransformableBacteria.mutationRateChangeDistribution = mutationRateChangeDistribution;
	}

	public Distribution getTransformationRateChangeDistribution() {
		return transformationRateChangeDistribution;
	}

	public void setTransformationRateChangeDistribution(
			Distribution transformationRateChangeDistribution) {
		TransformableBacteria.transformationRateChangeDistribution = transformationRateChangeDistribution;
	}

	public Distribution getThresholdChangeDistribution() {
		return thresholdChangeDistribution;
	}

	public void setThresholdChangeDistribution(
			Distribution thresholdChangeDistribution) {
		TransformableBacteria.thresholdChangeDistribution = thresholdChangeDistribution;
	}

}