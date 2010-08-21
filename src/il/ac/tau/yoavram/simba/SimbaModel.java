package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Model;

import java.util.List;

import org.apache.log4j.Logger;

import cern.jet.random.Uniform;

import com.google.common.collect.Lists;

/**
 * TODO serialization
 * 
 * @author Yoav
 * 
 */
public class SimbaModel implements Model {
	private static final long serialVersionUID = -2304552481208280007L;
	private static final Logger logger = Logger.getLogger(SimbaModel.class);

	private List<Bacteria> population;
	private Bacteria ancestor;
	private int populationSize;
	private double fractionOfGenesToChange;
	private double environmentalChangeFrequency;
	private Environment environment;

	@Override
	public void init() {
		logger.debug("Inhabiting the population with decendents of "
				+ getAncestor().getID());
		population = Lists.newArrayList();
		for (int i = 0; i < getPopulationSize(); i++) {
			population.add(getAncestor().reproduce());
		}
	}

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

	public void setEnvironmentalChangeFrequency(
			double environmentalChangeFrequency) {
		this.environmentalChangeFrequency = environmentalChangeFrequency;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setAncestor(Bacteria ancestor) {
		this.ancestor = ancestor;
	}

	public Bacteria getAncestor() {
		return ancestor;
	}
}