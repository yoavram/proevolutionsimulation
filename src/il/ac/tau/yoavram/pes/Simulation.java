package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.terminators.Terminator;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

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
	private Terminator terminator;
	private Date time;

	public static Simulation getInstance() {
		return INSTACE;
	}

	public Simulation() {
		tick = 0;
		running = true;
		INSTACE = this;
	}

	public void start() {
		logger.info("Starting simulation " + TimeUtils.formatDate(getTime()));
		while (running) {
			logger.debug("tick " + tick);
			tick++;
			getModel().step();
			for (DataGatherer<?> dataG : dataGatherers) {
				if (getTick() % dataG.getInterval() == 0) {
					dataG.gather();
				}
			}
			if (terminator.terminate()) {
				end();
			}
		}
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

	public void setTerminator(Terminator terminator) {
		this.terminator = terminator;
	}

	public Terminator getTerminator() {
		return terminator;
	}

	public void setDataGatherers(List<DataGatherer<?>> dataGatherers) {
		this.dataGatherers = dataGatherers;
	}

	public List<DataGatherer<?>> getDataGatherers() {
		return dataGatherers;
	}

	public void addDataGatherer(DataGatherer<?> dataGatherer) {
		dataGatherers.add(dataGatherer);
	}

	public void setTime(Date date) {
		this.time = date;
	}

	public Date getTime() {
		return time;
	}
}
