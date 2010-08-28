package il.ac.tau.yoavram.pes.terminators;


public interface Terminator {
	boolean terminate();

	void setInterval(int interval);

	int getInterval();

}
