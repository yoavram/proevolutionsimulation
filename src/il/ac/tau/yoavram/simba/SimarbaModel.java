package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import org.apache.log4j.Logger;

public class SimarbaModel extends SimbaModel {
	private static final long serialVersionUID = 3652521078393600882L;
	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

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
				// reproduce (incl. mutation)
				Bacteria child = mother.reproduce();
				// transform
				int numOfTransformations = RandomUtils.nextPoisson(child
						.getTransformationRate());
				if (numOfTransformations > 0) {
					logger.debug("new organism " + child.getID() + " has "
							+ numOfTransformations + " transformations");
					for (int i = 0; i < numOfTransformations; i++) {
						SexyBacteria dnaDoner = randomSexyBacteria();
						child.transform(dnaDoner.getAlleles());
					}
				}

				// add to population
				getPopulations().get(0).add(child);
				// log
				logger.debug(String.format("Reproduced %s %d, child is %s %d",
						mother.getClass().getSimpleName(), mother.getID(),
						child.getClass().getSimpleName(), child.getID()));
			}
		}

		// change environment
		if (getEnvironmentalChangeFrequency() > 0
				&& RandomUtils.nextDouble() < getEnvironmentalChangeFrequency()) {
			logger.debug("Changing the environment");
			getEnvironment().change(getFractionOfGenesToChange());
		} else if (getEnvironmentalChangeFrequency() < 0) {
			if (Simulation.getInstance().getTick() % getPeriod() == 0) {
				logger.debug("Changing the environment");
				getEnvironment().change(getFractionOfGenesToChange());
			}
		}
	}

	private SexyBacteria randomSexyBacteria() {
		Bacteria b = randomBacteria();
		while (!(b instanceof SexyBacteria))
			b = randomBacteria();
		return (SexyBacteria) b;

	}
}
