package il.ac.tau.yoavram.pes;

import java.util.List;

public interface Simulation {
	long getTick();

	void setEvents(List<Event> events);

	List<Event> getEvents();

	void addEvent(Event event);

	void start();

	void end();
}
