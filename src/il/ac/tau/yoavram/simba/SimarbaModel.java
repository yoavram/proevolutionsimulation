package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.FixedSizedQueue;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import org.apache.log4j.Logger;

public class SimarbaModel extends SimbaModel {
	private static final long serialVersionUID = 3652521078393600882L;
	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

	private FixedSizedQueue<Bacteria> graveyard;

	public SimarbaModel() {
		super();
		graveyard = new FixedSizedQueue<Bacteria>(10);
	}

	@Override
	public void step() {
		// kill random bacteria and put it in the graveyard
		int kill = randomBacteriaIndex();
		Bacteria dead = getPopulations().get(0).remove(kill);
		logger.debug(String.format("Killed %s %d", dead.getClass()
				.getSimpleName(), dead.getID()));
		dead = graveyard.add(dead);
		if (dead != null)
			dead.die();

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
				for (int i = 0; i < numOfTransformations; i++) {
					Bacteria dnaDoner = graveyard.random();
					if (dnaDoner == null) {
						logger.warn("Could not continue with transformation, no organisms in the graveyard");
					} else {
						logger.debug(String
								.format("Transforming %s with DNA from dead %s",
										child.toString(), dnaDoner.toString()));
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
			getEnvironment().change(getFractionOfGenesToChange());
		} else if (getEnvironmentalChangeFrequency() < 0) {
			if (Simulation.getInstance().getTick() % getPeriod() == 0) {
				getEnvironment().change(getFractionOfGenesToChange());
			}
		}
	}

}
