package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Simulation;

public abstract class AbstractTerminator implements Terminator {
	Simulation simulation;

	@Override
	public Simulation getSimulation() {
		return simulation;
	}

	@Override
	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}
}
