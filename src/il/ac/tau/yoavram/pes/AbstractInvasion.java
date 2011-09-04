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

import il.ac.tau.yoavram.pes.utils.RandomUtils;

/**
 * This class implements a simple invasion.</br>
 * It iterates over all agents in all the populations. For each agent it performs a random test with probability for success equal to the invasion rate.</br>
 * If the test succeeds, the agent is transformed using the <code>transform</code>.</br>
 * This <code>transform</code> method needs to be overriden by implementing classes, because it is abstract.</br>
 * <p>
 * For example, a permutation can be implemented by:<pre>
 * <code>
 * public class Permutation extends AbstractInvasion<Integer, Integer> {
 *	\@Override
 *	protected Integer transform(Integer t) {
 *		return t + 1;
 *	}
 *
 *   \@Override
 *	public String getInvaderName() {
 *		return "GreaterInteger";
 *	}
 * }
 *  </code></pre>
 *
 * @author yoavram
 * 
 * @see Invasion
 */
public abstract class AbstractInvasion<T, E extends T> implements
		Invasion<T, E> {

	protected double invasionRate;

	@Override
	public List<List<T>> invade(List<List<T>> populations) {
		for (List<T> population : populations) {
			for (int i = 0; i < population.size(); i++) {
				if (RandomUtils.nextDouble() < getInvasionRate()) {
					E e = transform(population.get(i));
					population.set(i, e);
				}
			}
		}
		return populations;
	}

	protected abstract E transform(T t);

	public double getInvasionRate() {
		return invasionRate;
	}

	public void setInvasionRate(double invasionRate) {
		if (invasionRate < 0 || invasionRate > 1) {
			throw new IllegalArgumentException(
					"Invasion rate value must be >= 0 and <= 1, it is "
							+ invasionRate);
		}
		this.invasionRate = invasionRate;
	}

}