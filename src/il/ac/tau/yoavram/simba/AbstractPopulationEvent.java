package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Event;

import java.util.List;

public abstract class AbstractPopulationEvent implements Event {
	private List<Bacteria> population;

	public void setPopulation(List<Bacteria> population) {
		this.population = population;
	}

	public List<Bacteria> getPopulation() {
		return population;
	}

}
