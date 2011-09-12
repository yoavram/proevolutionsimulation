package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

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
				if (numOfTransformations > 0) {
					logger.info(String.format(
							"New organism %s %d has %d transformations", child
									.getClass().getSimpleName(), child.getID(),
							numOfTransformations));
					for (int i = 0; i < numOfTransformations; i++) {
						Bacteria dnaDoner = graveyard.random();
						if (dnaDoner == null) {
							logger.warn("Could not continue with transformation, no organisms in the graveyard");
						} else {
							logger.info(String
									.format("Transforming %s %d with DNA from dead bacteria %d",
											child.getClass().getSimpleName(),
											child.getID(), dnaDoner.getID()));
							child.transform(dnaDoner.getAlleles());
						}
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

	public static class FixedSizedQueue<B> {
		private static final long serialVersionUID = -6436424249586452569L;

		private List<B> list;
		private int limit;

		public FixedSizedQueue(int limit) {
			super();
			this.limit = limit;
			list = Lists.newArrayListWithCapacity(limit);
		}

		/**
		 * add b to the queue. if queue is full, remove the oldest item in the
		 * queue and return it. otherwise return null.
		 * 
		 * @param b
		 *            item to add
		 * @return oldest item in the queue that was removed to allow space for
		 *         input, or null if the queue was not full.
		 */
		public B add(B b) {
			B ret = null;
			if (list.size() >= limit) {
				ret = list.remove(0);
			}
			list.add(b);
			return ret;
		}

		/**
		 * randomly choose an item from the queue, remove it and return it.
		 * 
		 * @return the randomly removed item
		 */
		public B randomRemove() {
			int rand = RandomUtils.nextInt(0, list.size() - 1);
			return list.remove(rand);
		}

		/**
		 * randomly choose an item from the queue and return it, leaving it in
		 * the queue.
		 * 
		 * @return randomly chosen item
		 */
		public B random() {
			int rand = RandomUtils.nextInt(0, list.size() - 1);
			return list.get(rand);
		}

		/**
		 * returns the number of items in the queue
		 */
		public int size() {
			return list.size();
		}
	}
}
