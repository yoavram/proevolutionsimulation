package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Simulation;

public class TickTerminator extends AbstractTerminator implements Terminator {

	private long endAtTick = 0;

	@Override
	public boolean terminate() {
		return Simulation.getInstance().getTick() >= getEndAtTick();
	}

	public void setEndAtTick(long endAtTick) {
		this.endAtTick = endAtTick;
	}

	public long getEndAtTick() {
		return endAtTick;
	}

}
