package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.FixedSizedQueue;
import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;

public class SimarbaModel extends SimbaModel {
	private static final long serialVersionUID = -9092311069010612572L;

	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

	private Map<Integer, FixedSizedQueue<Bacteria>> graveyardMap;

	private int graveyardSize = 10;
	private boolean recombinationBarriers = false;

	public SimarbaModel() {
		super();
	}

	@Override
	public void init() {
		super.init();
		if (graveyardMap == null) {
			graveyardMap = Maps.newHashMapWithExpectedSize(2);
		}
	}

	@Override
	public void step() {
		// kill random bacteria and put it in the graveyard
		int kill = randomBacteriaIndex();
		Bacteria dead = getPopulations().get(0).remove(kill);
		logger.debug(String.format("Tick %d: Killed %s", Simulation
				.getInstance().getTick(), dead.toString()));
		if (isRecombinationBarriers())
			dead = getGraveyard(getStrain(dead)).add(dead);
		else
			dead = getGraveyard().add(dead);
		if (dead != null)
			dead.die();

		// reproduce random fit bacteria
		int tries = 0;
		while (getPopulations().get(0).size() < getPopulationSize()) {
			Bacteria mother = randomBacteria();
			Bacteria child = null;
			if (mother.getFitness() > RandomUtils.nextDouble()
					|| tries++ > getPopulationSize()) {
				// reproduce (incl. mutation)
				child = mother.reproduce();

				// transform
				transform(child);

				// add to population
				getPopulations().get(0).add(child);
				// log
				logger.debug(String.format(
						"Tick %d: Reproduced %s, child is %s", Simulation
								.getInstance().getTick(), mother.toString(),
						child.toString()));
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

	@Override
	public void changeEnvironment() {
		logger.info(String.format("Tick %d: Changing %f of the environment",
				NumberUtils.formatNumber(Simulation.getInstance().getTick()),
				getFractionOfGenesToChange()));
		Map<Integer, Integer> changedGenes = getEnvironment().change(
				getFractionOfGenesToChange());
		// check if the new allele at the changed genes is present
		for (List<Bacteria> pop : getPopulations()) {
			for (Bacteria b : pop) {
				for (Entry<Integer, Integer> e : changedGenes.entrySet()) {
					int gene = e.getKey();
					int allele = e.getValue();
					if (b.getAlleles()[gene] == allele) {
						logger.info(String
								.format("New allele %d in gene %d already exists in bacteria %s",
										allele, gene, b.toString()));
					}
				}
			}
		}
	}

	public boolean transform(Bacteria recipient) {
		int numOfTransformations = RandomUtils.nextPoisson(recipient
				.getTransformationRate());
		if (numOfTransformations == 0)
			return false;

		int strain = getStrain(recipient);
		FixedSizedQueue<Bacteria> graveyard = (isRecombinationBarriers()) ? getGraveyard(strain)
				: getGraveyard();
		if (graveyard.size() == 0) {
			logger.warn(String
					.format("Tick %d: Could not continue with transformation, no organisms in graveyard %d",
							Simulation.getInstance().getTick(), strain));
			return false;
		}

		while (numOfTransformations > 0) {
			Bacteria dnaDoner = graveyard.randomRemove();

			int genes = recipient.transform(dnaDoner.getAlleles());
			logger.debug(String
					.format("Tick %d: Transformed %s with DNA from dead %s - %d genes exchanged",
							Simulation.getInstance().getTick(),
							recipient.toString(), dnaDoner.toString(), genes));
			numOfTransformations--;
		}
		return true;
	}

	public int getStrain(Bacteria bacteria) {
		if (bacteria instanceof SexyBacteria) {
			SexyBacteria sexyBacteria = (SexyBacteria) bacteria;
			if (sexyBacteria.isCm()) {
				if (sexyBacteria.isCr())
					return 1; // CM-CR
				else if (sexyBacteria.isSir())
					return 2; // CM-SIR
				else
					return 3; // CM-WT
			} else if (sexyBacteria.isSim()) {
				if (sexyBacteria.isCr())
					return 4; // SIM-CM
				else if (sexyBacteria.isSir())
					return 5; // SIM-SIR
				else
					return 6; // SIM-WT
			} else if (sexyBacteria.isCr())
				return 7; // WT-CR
			else if (sexyBacteria.isSir())
				return 8; // WT-SIR
			else
				return 0; // WT-WT
		} else {
			return 0; // WT-WT
		}

	}

	public int getGraveyardSize() {
		return graveyardSize;
	}

	public void setGraveyardSize(int graveyardSize) {
		this.graveyardSize = graveyardSize;
	}

	public boolean isRecombinationBarriers() {
		return recombinationBarriers;
	}

	public void setRecombinationBarriers(boolean recombinationBarriers) {
		this.recombinationBarriers = recombinationBarriers;
	}

	public FixedSizedQueue<Bacteria> getGraveyard() {
		return getGraveyard(0);
	}

	public void setGraveyard(FixedSizedQueue<Bacteria> graveyard) {
		setGraveyard(0, graveyard);
	}

	public FixedSizedQueue<Bacteria> getGraveyard(int strain) {
		if (graveyardMap.containsKey(strain))
			return graveyardMap.get(strain);
		else {
			FixedSizedQueue<Bacteria> newGraveyard = new FixedSizedQueue<Bacteria>(
					getGraveyardSize());
			graveyardMap.put(strain, newGraveyard);
			return newGraveyard;
		}
	}

	public void setGraveyard(int strain, FixedSizedQueue<Bacteria> graveyard) {
		graveyardMap.put(strain, graveyard);
	}
}
