package il.ac.tau.yoavram.pes.terminators;

public abstract class AbstractTerminator implements Terminator {

	private int interval = 1;

	@Override
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public int getInterval() {
		return interval;
	}

}
