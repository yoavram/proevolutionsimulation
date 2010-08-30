package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.SerializableModel;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * @author Yoav
 * 
 */
public class SimbaModel extends SerializableModel<Bacteria> {
	private static final long serialVersionUID = -2304552481208280007L;
	private static final Logger logger = Logger.getLogger(SimbaModel.class);

	private Bacteria ancestor = null;
	private int populationSize = 0;
	private double fractionOfGenesToChange = 0;
	private double environmentalChangeFrequency = 0;
	private Environment environment = null;
	private List<List<Bacteria>> populations = null;
	private boolean changeEnvironmentOnStartup = false;
	private Invasion<Bacteria, ? extends Bacteria> invasion = null;

	@Override
	public void init() {
		if (getPopulations() == null) {
			logger.debug("Inhabiting the population with decendents of "
					+ getAncestor().getID());
			List<Bacteria> pop = Lists.newArrayList();
			for (int i = 0; i < getPopulationSize(); i++) {
				pop.add(getAncestor().reproduce());
			}
			populations = Lists.newArrayList();
			populations.add(pop);
		} else {
			logger.debug("Population already inhibited");
		}
		if (isChangeEnvironmentOnStartup()) {
			logger.info("Changing environment by "
					+ getFractionOfGenesToChange() + "%");
			getEnvironment().change(getFractionOfGenesToChange());
		}
		if (getInvasion() != null) {
			logger.info("Invading the populations with "
					+ getInvasion().getInvasionRate() + "% "
					+ getInvasion().getInvaderName());
			setPopulations(getInvasion().invade(getPopulations()));
		}

	}

	@Override
	public void step() {
		// kill random bacteria
		int kill = randomBacteriaIndex();
		getPopulations().get(0).remove(kill);
		logger.debug("Killed bacteria " + kill);

		// reproduce random fit bacteria
		while (getPopulations().get(0).size() < getPopulationSize()) {
			Bacteria reproduce = randomBacteria();
			if (reproduce.getFitness() > RandomUtils.nextDouble()) {
				Bacteria child = reproduce.reproduce();
				getPopulations().get(0).add(child);
				logger.debug("Reproduced bacteria " + reproduce.getID()
						+ ", child is " + child.getID());
			}
		}

		// change environment
		if (RandomUtils.nextDouble() < getEnvironmentalChangeFrequency()) {
			logger.debug("Changing the environment");
			getEnvironment().change(getFractionOfGenesToChange());
		}
	}

	private Bacteria randomBacteria() {
		return getPopulations().get(0).get(randomBacteriaIndex());
	}

	private int randomBacteriaIndex() {
		return RandomUtils.nextInt(0, getPopulations().get(0).size() - 1);
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

	private boolean isChangeEnvironmentOnStartup() {
		return changeEnvironmentOnStartup;
	}

	public void setChangeEnvironmentOnStartup(boolean changeEnvironmentOnStartup) {
		this.changeEnvironmentOnStartup = changeEnvironmentOnStartup;
	}

	public void setInvasion(Invasion<Bacteria, ? extends Bacteria> invasion) {
		this.invasion = invasion;
	}

	public Invasion<Bacteria, ? extends Bacteria> getInvasion() {
		return invasion;
	}

}
