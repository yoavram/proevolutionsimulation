package il.ac.tau.yoavram.pes;


import java.util.List;

import org.apache.log4j.Logger;

public class CycleSimulation implements Simulation {
	private static Logger logger = Logger.getLogger(CycleSimulation.class);

	List<Event> events;
	long tick = 0;

	public CycleSimulation() {
	}

	@Override
	public void start() {
		while (true) {
			logger.debug("Starting cycle " + tick);
			for (Event e : events) {
				logger.debug("Calling event " + e.getClass().getName());
				e.happen();
			}
			tick++;
		}
	}

	@Override
	public void end() {
		logger.warn("Ending simulation at tick " + getTick());
		events.clear();
	}

	@Override
	public void addEvent(Event event) {
		if (tick > 0) {
			throw new IllegalAccessError(
					"Can't add events after simulation started");
		}
		events.add(event);
	}

	@Override
	public List<Event> getEvents() {
		return events;
	}

	@Override
	public void setEvents(List<Event> events) {
		if (tick > 0) {
			throw new IllegalAccessError(
					"Can't set events after simulation started");
		}
		this.events = events;
	}

	public void setTick(long tick) {
		logger.debug("Changing tick from " + this.tick + " to " + tick);
		this.tick = tick;
	}

	@Override
	public long getTick() {
		return tick;
	}
}
