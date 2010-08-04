package il.ac.tau.yoavram.pes;

//TODO replace simulation injection with method injection of the end method
public interface Terminator extends Event {
	void setSimulation(Simulation simulation);

	Simulation getSimulation();
}
