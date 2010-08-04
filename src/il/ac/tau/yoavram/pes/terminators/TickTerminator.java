package il.ac.tau.yoavram.pes.terminators;


public class TickTerminator extends AbstractTerminator implements Terminator {

	private long endAtTick = 0;

	@Override
	public void happen() {
		if (getSimulation().getTick() >= getEndAtTick())
			getSimulation().end();
	}

	public void setEndAtTick(long endAtTick) {
		this.endAtTick = endAtTick;
	}

	public long getEndAtTick() {
		return endAtTick;
	}

}
