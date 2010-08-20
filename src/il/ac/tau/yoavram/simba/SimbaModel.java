package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Model;

import java.util.List;

import cern.jet.random.Uniform;

public class SimbaModel implements Model {

	private List<Bacteria> population;
	private int populationSize;
	private double fractionOfGenesToChange;
	private double environmentalChangeFrequency;
	private Environment environment;

	@Override
	public void step() {
		// kill random bacteria
		int kill = randomBacteriaIndex();
		population.remove(kill);

		// reproduce random fit bacteria
		while (population.size() < getPopulationSize()) {
			Bacteria reproduce = randomBacteria();
			if (reproduce.getFitness() > Uniform.staticNextDouble()) {
				Bacteria child = reproduce.reproduce();
				population.add(child);
			}
		}

		// change environment
		if (Uniform.staticNextDouble() < getEnvironmentalChangeFrequency()) {
			getEnvironment().change(getFractionOfGenesToChange());
		}
	}

	private Bacteria randomBacteria() {
		return population.get(randomBacteriaIndex());
	}

	private int randomBacteriaIndex() {
		return Uniform.staticNextIntFromTo(0, population.size() - 1);
	}

	/* GETTERS AND SETTERS */
	public List<Bacteria> getPopulation() {
		return population;
	}

	public void setPopulation(List<Bacteria> population) {
		this.population = population;
	}

	public Environment getEnvironment() {
		return environment;
	}

	private double getFractionOfGenesToChange() {
		return fractionOfGenesToChange;
	}

	private double getEnvironmentalChangeFrequency() {
		return environmentalChangeFrequency;
	}

	private int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setFractionOfGenesToChange(double fractionOfGenesToChange) {
		this.fractionOfGenesToChange = fractionOfGenesToChange;
	}

	public void setEnvironmentalChangeFrequency(double environmentalChangeFrequency) {
		this.environmentalChangeFrequency = environmentalChangeFrequency;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
