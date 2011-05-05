package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Simulation;

public class RandomSelectionTransformableBacteria extends TransformableBacteria {

	private static final long serialVersionUID = 8253816008784277565L;

	@Override
	public double getFitness() {
		if (fitness == -1
				|| getEnvironment().getLastEnvironmentalChange() > update) {
			for (int gene = 0; gene < getAlleles().length; gene++) {
				if (getEnvironment().getIdealAllele(gene) != getAlleles()[gene]) {
					double rs = getRandomSelectionEnvironment()
							.getSelectionCoefficient(gene);
					fitness *= (1 - rs);
				}
			}
			update = Simulation.getInstance().getTick();
		}
		return fitness;
	}

	protected RandomSelectionEnvironment getRandomSelectionEnvironment() {
		return RandomSelectionEnvironment.getInstance();
	}

}
