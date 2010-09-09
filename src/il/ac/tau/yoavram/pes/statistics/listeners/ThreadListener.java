package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * wrapper class
 * 
 * @author yoavram
 * 
 */
public class ThreadListener extends Thread implements DataListener {
	private static final Logger logger = Logger.getLogger(ThreadListener.class);

	private DataListener inner;
	private final BlockingQueue<Number[]> queue;
	private boolean running = true;

	public ThreadListener() {
		this(ThreadListener.class.getSimpleName());
	}

	public ThreadListener(String name) {
		super(name);
		queue = new LinkedBlockingQueue<Number[]>();
	}

	public void init() {
		start();
	}

	@Override
	public void close() throws IOException {
		setRunning(false);
		setRunning(false);
		Iterator<Number[]> e = queue.iterator();
		while (e.hasNext()) {
			inner.listen(e.next());
		}
	}

	@Override
	public void run() {
		try {
			while (isRunning()) {
				inner.listen(queue.take());
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

	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		inner.setDataFieldNames(dataFieldNames);
	}

	public DataListener getInner() {
		return inner;
	}

	public void setInner(DataListener inner) {
		this.inner = inner;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
