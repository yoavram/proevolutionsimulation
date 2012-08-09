/*
 *  proevolutionsimulation: an agent-based simulation framework for evolutionary biology
 *  Copyright 2010 Yoav Ram <yoavram@post.tau.ac.il>
 *
 *  This file is part of proevolutionsimulation.
 *
 *  proevolutionsimulation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License (GNU GPL v3) as published by
 *  the Free Software Foundation.
 *   
 *  proevolutionsimulation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. You are allowed to modify this code, link it with other code 
 *  and release it, as long as you keep the same license. 
 *  
 *  The content license is Creative Commons 3.0 BY-SA. 
 *   *
 *  You should have received a copy of the GNU General Public License
 *  along with proevolutionsimulation.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.terminators.Terminator;
import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * This class controls a single simulation. It contains the {@link Model}, which
 * has the logic of the simulation; the {@link DataGatherer}s that collect and
 * save statistics; the {@link Terminator}s that control the stopping conditions
 * of the simulations; and the timing of the simulation.
 * <p>
 * Simulation fulfills the Singleton pattern and therefore can be obtained by
 * any other class by calling <code>Simulation.getInstance()</code>.
 * <p>
 * An instance should be created by a runner, such as the {@link SpringRunner}
 * </br> The flow of a simulation is as follows:
 * <ul>
 * <li>1 initiation</li>
 * <li>2 time increment</li>
 * <li>3 one model step</li>
 * <li>4 all {@link DataGatherer}s gather data</li>
 * <li>5 all {@link Terminator}s are checked for stopping condition
 * <ul>
 * <li>5.1 if any terminator was positive for termination, proceed to 6</li>
 * <li>5.2 otherwise return to (2)</li>
 * </ul>
 * <li>6 finish simulaiton and close all gatherers</li>
 * </ul>
 * 
 * @author yoavram
 * @version Alfred
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
	private int tickInterval = 0;
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
			if (getTickInterval() != 0 && getTick() % getTickInterval() == 0)
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
