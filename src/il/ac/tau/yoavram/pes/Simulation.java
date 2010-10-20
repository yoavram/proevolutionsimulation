package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.terminators.Terminator;
import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/*
 */
public class Simulation {
	private static Logger logger = Logger.getLogger(Simulation.class);
	private static Simulation INSTANCE = null;

	private Model<?> model;
	private long tick;
	private boolean running;
	private List<DataGatherer<?>> dataGatherers;
	private List<Terminator> terminators;
	private Object id;
	private int tickInterval = 100000;
	private boolean blockAtEnd = false;

	public static Simulation getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Simulation();
		return INSTANCE;
	}

	/**
	 * construct new simulation. as simulation is a singleton this will override
	 * previous simulation. to get the latest simulation use
	 * <code>getInstance()</code> TODO make this private and create with factory
	 * mehtod in spring
	 */
	public Simulation() {
		tick = 0;
		running = true;
		INSTANCE = this;
	}

	public void start() {
		long start = System.currentTimeMillis();
		logger.info("Starting simulation id " + getID());
		while (running) {
			if (getTick() % getTickInterval() == 0)
				logger.info("tick " + NumberUtils.formatNumber(getTick()));
			incrementTick();

			getModel().step();

			for (DataGatherer<?> dataG : getDataGatherers()) {
				if (getTick() % dataG.getInterval() == 0) {
					dataG.gather();
				}
			}
			for (Terminator terminator : getTerminators()) {
				if (getTick() % terminator.getInterval() == 0
						&& terminator.terminate()) {
					logger.info(terminator.getClass().getSimpleName()
							+ " terminated the simulation");
					end();
				}
			}
		}
		long stop = System.currentTimeMillis();
		logger.info("Simulation finished, "
				+ NumberUtils.formatNumber(getTick()) + " ticks, "
				+ TimeUtils.formatDuration(stop - start));

		logger.info("Closing data gatherers");
		for (DataGatherer<?> dataG : getDataGatherers()) {
			try {
				dataG.close();
			} catch (IOException e) {
				logger.warn("Error closing " + dataG.toString() + ": " + e);
			}
		}
		if (isBlockAtEnd()) {
			System.out.println("Press any key to continue...");
			try {
				System.in.read();
			} catch (IOException e) {
				// nothing TO DO here
			}
		}
	}

	public void incrementTick() {
		tick++;
	}

	public void end() {
		running = false;
	}

	public void setTick(long tick) {
		logger.debug("Changing tick from "
				+ NumberUtils.formatNumber(getTick()) + " to "
				+ NumberUtils.formatNumber(tick));
		this.tick = tick;
	}

	public long getTick() {
		return tick;
	}

	public void setModel(Model<?> model) {
		this.model = model;
	}

	public Model<?> getModel() {
		return model;
	}

	public void addTerminator(Terminator terminator) {
		if (terminators == null) {
			terminators = Lists.newArrayList();
		}
		getTerminators().add(terminator);
	}

	public void setTerminators(List<Terminator> terminators) {
		this.terminators = terminators;
	}

	public List<Terminator> getTerminators() {
		return terminators;
	}

	public void setDataGatherers(List<DataGatherer<?>> dataGatherers) {
		this.dataGatherers = dataGatherers;
	}

	public List<DataGatherer<?>> getDataGatherers() {
		return dataGatherers;
	}

	public void addDataGatherer(DataGatherer<?> dataGatherer) {
		if (dataGatherers == null) {
			dataGatherers = Lists.newArrayList();
		}
		dataGatherers.add(dataGatherer);
	}

	public void setID(Object id) {
		this.id = id;
	}

	public Object getID() {
		return id;
	}

	public void setTickInterval(int tickInterval) {
		if (tickInterval < 1)
			this.tickInterval = Integer.MAX_VALUE;
		else
			this.tickInterval = tickInterval;
	}

	public int getTickInterval() {
		return tickInterval;
	}

	public void setBlockAtEnd(boolean blockAtEnd) {
		this.blockAtEnd = blockAtEnd;
	}

	public boolean isBlockAtEnd() {
		return blockAtEnd;
	}
}
