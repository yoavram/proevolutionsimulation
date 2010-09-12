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
 * TODO fork threads? listeners already fork. should simulation as well? i don't see why.
 */
public class Simulation {
	private static Logger logger = Logger.getLogger(Simulation.class);
	private static Simulation INSTACE = null;

	private Model<?> model;
	private long tick;
	private boolean running;
	private List<DataGatherer<?>> dataGatherers;
	private List<Terminator> terminators;
	private Object id;

	public static Simulation getInstance() {
		return INSTACE;
	}

	/**
	 * construct new simulation. as simulation is a singleton this will override
	 * previous simulation. to get the latest simulation use
	 * <code>getInstance()</code>
	 */
	public Simulation() {
		tick = 0;
		running = true;
		INSTACE = this;
	}

	public void start() {
		long start = System.currentTimeMillis();
		logger.info("Starting simulation id " + getID());
		while (running) {
			logger.debug("tick " + NumberUtils.formatNumber(getTick()));
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
	}

	private void incrementTick() {
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
}
