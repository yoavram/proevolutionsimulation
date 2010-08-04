package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Event;
import il.ac.tau.yoavram.pes.Simulation;

//TODO replace simulation injection with method injection of the end method
public interface Terminator extends Event {
	void setSimulation(Simulation simulation);

	Simulation getSimulation();
}
