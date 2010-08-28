package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.terminators.Terminator;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/*
 * TODO fork threads
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

	public Simulation() {
		tick = 0;
		running = true;
		INSTACE = this;
	}

	public void start() {
		logger.info("Starting simulation id " + getID());
		while (running) {
			logger.debug("tick " + getTick());
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
					end();
				}
			}
		}
		logger.info("Simulation finished, exiting...");
	}

	private void incrementTick() {
		tick++;
	}

	public void end() {
		logger.info("Ending simulation at tick " + getTick());
		running = false;
	}

	public void setTick(long tick) {
		logger.debug("Changing tick from " + this.tick + " to " + tick);
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
