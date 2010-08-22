package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.SerilizableModel;

import java.util.List;

import org.apache.log4j.Logger;

import cern.jet.random.Uniform;

import com.google.common.collect.Lists;

//TODO sim invasion 
//TODO fixation terminator 
// TODO change environment after deserializaiton
/**
 * @author Yoav
 * 
 */
public class SimbaModel extends SerilizableModel<Bacteria> {
	private static final long serialVersionUID = -2304552481208280007L;
	private static final Logger logger = Logger.getLogger(SimbaModel.class);

	private Bacteria ancestor;
	private int populationSize;
	private double fractionOfGenesToChange;
	private double environmentalChangeFrequency;
	private Environment environment;
	private List<List<Bacteria>> populations;

	@Override
	public void init() {
		logger.debug("Inhabiting the population with decendents of "
				+ getAncestor().getID());
		List<Bacteria> pop = Lists.newArrayList();
		for (int i = 0; i < getPopulationSize(); i++) {
			pop.add(getAncestor().reproduce());
		}
		populations = Lists.newArrayList();
		populations.add(pop);
	}

	@Override
	public void step() {
		// kill random bacteria
		int kill = randomBacteriaIndex();
		getPopulations().get(0).remove(kill);

		// reproduce random fit bacteria
		while (getPopulations().get(0).size() < getPopulationSize()) {
			Bacteria reproduce = randomBacteria();
			if (reproduce.getFitness() > Uniform.staticNextDouble()) {
				Bacteria child = reproduce.reproduce();
				getPopulations().get(0).add(child);
			}
		}

		// change environment
		if (Uniform.staticNextDouble() < getEnvironmentalChangeFrequency()) {
			getEnvironment().change(getFractionOfGenesToChange());
		}
	}

	private Bacteria randomBacteria() {
		return getPopulations().get(0).get(randomBacteriaIndex());
	}

	private int randomBacteriaIndex() {
		return Uniform.staticNextIntFromTo(0,
				getPopulations().get(0).size() - 1);
	}

	/* GETTERS AND SETTERS */
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

	@Override
	public List<List<Bacteria>> getPopulations() {
		return populations;
	}

	@Override
	public void setPopulations(List<List<Bacteria>> populations) {
		this.populations = populations;
	}
}
