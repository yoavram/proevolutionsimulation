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
 * An invasion class is used in order to introduce new agents, including of new types, to pre-existing populations.</br>
 * Example usage: invasion of a population of altruists by cheaters.
 *  
 * @author yoavram
 * @version Alfred
 *
 * @param <T> input agent type
 * @param <E> output agent type that extends T
 * @see AbstractInvasion
 */
public interface Invasion<T, E extends T> {
	List<List<T>> invade(List<List<T>> populations);

	public double getInvasionRate();

	public void setInvasionRate(double invasionRate);

	String getInvaderName();
}
