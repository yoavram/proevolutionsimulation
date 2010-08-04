package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Event;

import java.util.List;

public class KillRandomBacteria extends AbstractPopulationEvent implements
		Event {

	@Override
	public void happen() {
		List<Bacteria> pop = getPopulation();
		synchronized (pop) {
			pop.remove(cern.jet.random.Uniform.staticNextIntFromTo(0,
					getPopulation().size() - 1));
		}

	}
}
