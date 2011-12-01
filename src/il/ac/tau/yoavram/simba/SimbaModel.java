package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.SerializableModel;
import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * @author yoavram
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

	private transient double period;

	@Override
	public void init() {
		if (getPopulations() == null) {
			logger.debug(String.format(
					"Inhabiting the population with decendents of %s %d",
					getAncestor().getClass().getName(), getAncestor().getID()));
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
			changeEnvironment();
		}
		if (getInvasion() != null) {
			logger.info(String.format(
					"Invading the populations with invasion %s with rate %f",
					getInvasion().getInvaderName(), getInvasion()
							.getInvasionRate()));
			setPopulations(getInvasion().invade(getPopulations()));
		}
		// this is a workaround...
		if (environmentalChangeFrequency < 0) {
			double freq = Math.abs(environmentalChangeFrequency);
			period = Math.ceil(1 / freq);
		}
	}

	@Override
	public void step() {
		// kill random bacteria
		int kill = randomBacteriaIndex();
		getPopulations().get(0).remove(kill).die();
		logger.debug(String.format("Killed bacteria %d", kill));

		// reproduce random fit bacteria
		int tries = 0;
		while (getPopulations().get(0).size() < getPopulationSize()) {
			Bacteria mother = randomBacteria();
			if (mother.getFitness() > RandomUtils.nextDouble()
					|| tries++ > getPopulationSize()) {
				Bacteria child = mother.reproduce();
				getPopulations().get(0).add(child);
				logger.debug(String.format("Reproduced %s %d, child is %s %d",
						mother.getClass().getSimpleName(), mother.getID(),
						child.getClass().getSimpleName(), child.getID()));
			}
		}

		// change environment
		if (getEnvironmentalChangeFrequency() > 0
				&& RandomUtils.nextDouble() < getEnvironmentalChangeFrequency()) {
			changeEnvironment();
		} else if (getEnvironmentalChangeFrequency() < 0) {
			if (Simulation.getInstance().getTick() % getPeriod() == 0) {
				changeEnvironment();
			}
		}
	}

	public void changeEnvironment() {
		logger.info(String.format("Tick %d: Changing %f of the environment",
				Simulation.getInstance().getTick(),
				getFractionOfGenesToChange()));
		getEnvironment().change(getFractionOfGenesToChange());
	}

	protected Bacteria randomBacteria() {
		return getPopulations().get(0).get(randomBacteriaIndex());
	}

	protected int randomBacteriaIndex() {
		return RandomUtils.nextInt(0, getPopulations().get(0).size() - 1);
	}

	/* GETTERS AND SETTERS */
	public Environment getEnvironment() {
		return environment;
	}

	public double getFractionOfGenesToChange() {
		return fractionOfGenesToChange;
	}

	public double getEnvironmentalChangeFrequency() {
		return environmentalChangeFrequency;
	}

	public int getPopulationSize() {
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

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

}
