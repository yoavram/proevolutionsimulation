package il.ac.tau.yoavram.simba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdealParentPopulationFactory {
	private Environment environment;
	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;
	private int populationSize;

	public List<Bacteria> createPopulation() {
		int[] housekeepingAlleles = new int[getNumberOfHousekeepingGenes()];
		Arrays.fill(housekeepingAlleles,
				HousekeepingAllele.Beneficial.ordinal());
		int[] environmentalAlleles = new int[getNumberOfEnvironmentalGenes()];
		for (int gene = 0; gene < environmentalAlleles.length; gene++) {
			environmentalAlleles[gene] = getEnvironment().getIdealAllele(gene);
		}
		Bacteria ideal = new Bacteria();
		ideal.setEnvironmentalAlleles(environmentalAlleles);
		ideal.setHousekeepingAlleles(housekeepingAlleles);

		List<Bacteria> population = new ArrayList<Bacteria>(getPopulationSize());
		for (int i = 0; i < getPopulationSize(); i++) {
			Bacteria child = ideal.reproduce();
			population.add(child);
		}

		return population;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public int getNumberOfEnvironmentalGenes() {
		return numberOfEnvironmentalGenes;
	}

	public void setNumberOfEnvironmentalGenes(int numberOfEnvironmentalGenes) {
		this.numberOfEnvironmentalGenes = numberOfEnvironmentalGenes;
	}

	public int getNumberOfHousekeepingGenes() {
		return numberOfHousekeepingGenes;
	}

	public void setNumberOfHousekeepingGenes(int numberOfHousekeepingGenes) {
		this.numberOfHousekeepingGenes = numberOfHousekeepingGenes;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getPopulationSize() {
		return populationSize;
	}
}
