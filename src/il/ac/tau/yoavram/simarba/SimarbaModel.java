package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Invasion;
import il.ac.tau.yoavram.pes.SerializableModel;
import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;
import il.ac.tau.yoavram.simba.Bacteria;

import java.util.List;
import org.apache.log4j.Logger;
import com.google.common.collect.Lists;

/**
 * @author yoavram
 * @version Charles
 */
public class SimarbaModel extends SerializableModel<Bacteria> {

	private static final long serialVersionUID = 864378378066751467L;

	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

	private TransformableBacteria ancestor = null;
	private int populationSize = 0;
	private double fractionOfGenesToChange = 0;
	private double environmentalChangeFrequency = 0;
	private Environment environment = null;
	private GenomicMemory genmicMemory = null; // for serialization
	private List<List<Bacteria>> populations = null;
	private boolean changeEnvironmentOnStartup = false;
	private Invasion<Bacteria, ? extends Bacteria> invasion = null;
	private boolean transformations = true;

	private transient double period;

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
		if (getGenmicMemory() == null && isTransformations()) {
			setGenmicMemory(new GenomicMemory());
		}
		if (getInvasion() != null) {
			logger.info("Invading the populations with "
					+ getInvasion().getInvasionRate() * 100 + "% "
					+ getInvasion().getInvaderName());
			setPopulations(getInvasion().invade(getPopulations()));
		}
		// periodical environmental changes are given by a negative frequency
		if (environmentalChangeFrequency < 0) {
			double freq = Math.abs(environmentalChangeFrequency);
			period = Math.ceil(1 / freq);
		}
	}

	@Override
	public void step() {
		// kill random bacteria
		int kill = randomBacteriaIndex();
		Bacteria killed = getPopulations().get(0).remove(kill);
		int id = killed.getID();
		logger.debug(String.format("Killed bacteria %d", id));

		// transformations
		if (isTransformations()) {
			for (Bacteria bacteria : getPopulations().get(0)) {				
				int numOfTrans = RandomUtils.nextPoisson(bacteria
						.getTransformationRate());
				if (numOfTrans > 0) {
					logger.debug(String.format(
							"Bacteria %d has %d transformations",
							bacteria.getID(), numOfTrans));
					for (int i = 0; i < numOfTrans; i++) {
						bacteria.transform();
					}
				}
			}
		}

		// reproduce random fit bacteria
		int tries = 0;
		while (getPopulations().get(0).size() < getPopulationSize()) {
			Bacteria mother = randomBacteria();

			if (mother.getFitness() > RandomUtils.nextDouble()
					|| tries++ > getPopulationSize()) {
				Bacteria child = mother.reproduce();
				getPopulations().get(0).add(child);
				logger.debug("Reproduced " + mother.getClass().getSimpleName()
						+ mother.getID() + ", child is "
						+ child.getClass().getSimpleName() + child.getID());
			}
		}

		// change environment
		if (getEnvironmentalChangeFrequency() > 0
				&& RandomUtils.nextDouble() < getEnvironmentalChangeFrequency()) {
			logger.debug("Changing the environment");
			getEnvironment().change(getFractionOfGenesToChange());
		} else if (getEnvironmentalChangeFrequency() < 0) {
			if (Simulation.getInstance().getTick() % period == 0) {
				logger.debug("Changing the environment");
				getEnvironment().change(getFractionOfGenesToChange());
			}
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

	/**
	 * 
	 * @param environmentalChangeFrequency
	 *            periodical environmental changes are given by a negative
	 *            frequency
	 */
	public void setEnvironmentalChangeFrequency(
			double environmentalChangeFrequency) {
		this.environmentalChangeFrequency = environmentalChangeFrequency;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setAncestor(TransformableBacteria ancestor) {
		this.ancestor = ancestor;
	}

	public TransformableBacteria getAncestor() {
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

	public void setInvasion(
			Invasion<Bacteria, ? extends Bacteria> invasion) {
		this.invasion = invasion;
	}

	public Invasion<Bacteria, ? extends Bacteria> getInvasion() {
		return invasion;
	}

	public void setGenmicMemory(GenomicMemory genmicMemory) {
		this.genmicMemory = genmicMemory;
	}

	public GenomicMemory getGenmicMemory() {
		return genmicMemory;
	}

	public void setTransformations(boolean transformations) {
		this.transformations = transformations;
	}

	public boolean isTransformations() {
		return transformations;
	}

}
