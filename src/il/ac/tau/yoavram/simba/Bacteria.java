package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.filters.ClassFilter;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class Bacteria implements Serializable {
	private static final long serialVersionUID = -490159741474792583L;
	private static final Logger logger = Logger.getLogger(Bacteria.class);

	private static int nextID = 0;
	private final int id = nextID++;

	protected int[] environmentalAlleles;
	protected int[] housekeepingAlleles;
	protected double mutationRate;
	protected double selectionCoefficient;

	protected transient double fitness = -1;
	protected transient long update = -1;

	public Bacteria() {
	}

	public Bacteria(Bacteria other) {
		setEnvironmentalAlleles(Arrays.copyOf(other.environmentalAlleles,
				other.environmentalAlleles.length));
		setHousekeepingAlleles(Arrays.copyOf(other.housekeepingAlleles,
				other.housekeepingAlleles.length));
		setMutationRate(other.getMutationRate());
		setSelectionCoefficient(other.getSelectionCoefficient());
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

	public Bacteria reproduce() {
		// logger.debug(getID() + " reproducing");
		Bacteria child = spawn();
		int numOfMutations = Poisson.staticNextInt(child.getMutationRate());
		if (numOfMutations > 0) {
			logger.debug("new organism " + getID() + " has " + numOfMutations
					+ " mutations");
		}
		for (int i = 0; i < numOfMutations; i++) {
			child.mutate();
		}
		return child;
	}

	protected Bacteria spawn() {
		return new Bacteria(this);
	}

	public void mutate() {
		int gene = Uniform.staticNextIntFromTo(0, housekeepingAlleles.length
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
			newAllele = (currentAllele + Uniform.staticNextIntFromTo(1, 2)) % 3;
			environmentalAlleles[gene] = newAllele;
		}
		logger.debug("Mutation at bacteria " + getID() + " gene " + gene
				+ " mutated from " + currentAllele + " to " + newAllele);
	}

	public void randomize() {
		for (int gene = 0; gene < housekeepingAlleles.length; gene++) {
			housekeepingAlleles[gene] = Uniform.staticNextIntFromTo(0, 1);
		}
		for (int gene = 0; gene < environmentalAlleles.length; gene++) {
			environmentalAlleles[gene] = Uniform.staticNextIntFromTo(0, 2);
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
