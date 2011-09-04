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
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with proevolutionsimulation.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package il.ac.tau.yoavram.pes;

import java.util.List;

/**
 * This class contains the entire logic of the simulation - the model that explains the real-world.
 * <p>
 * A model is a generic of some agent - as proevolution is an agent-based simulation framework, the agent is the atomic decision maker in the simulation.</br>
 * As much decisions and logic as possible should be implemented in the agent classes, leaving the model with "environmental" or "world" decisions.</br>
 * A model contains a populations - a list of lists of agents.
 * 
 * @author yoavram
 * @version Alfred
 * @param <T> The type of the agent this model can handle.
 * @see SerializableModel
 */
public interface Model<T> {
	void step();

	void init();

	List<List<T>> getPopulations();

	void setPopulations(List<List<T>> populations);

	Object getID();

	void setID(Object id);

	// TODO map, resources,...

}
