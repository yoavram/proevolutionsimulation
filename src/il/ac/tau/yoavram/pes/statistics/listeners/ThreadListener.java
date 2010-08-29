package il.ac.tau.yoavram.pes.statistics.listeners;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public abstract class ThreadListener extends Thread implements DataListener {
	private static final Logger logger = Logger.getLogger(ThreadListener.class);

	protected static final String DEFAULT_TITLE = "proevolution simulation";

	private final BlockingQueue<Number[]> queue;
	protected List<String> dataFieldNames;

	public ThreadListener(String name) {
		super(name);
		queue = new LinkedBlockingQueue<Number[]>();
	}

	public void init() {
		start();
	}

	public void destroy() {
		Iterator<Number[]> e = queue.iterator();
		while (e.hasNext()) {
			consume(e.next());
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException e) {
			logger.warn("Interrupted: " + e);
		}
	}
	
	@Override
	public void listen(Number[] data) {
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			logger.warn("Interrupted: " + e);
		}
	}

	protected abstract void consume(Number[] data);

	
	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		this.dataFieldNames = dataFieldNames;
	}

}
