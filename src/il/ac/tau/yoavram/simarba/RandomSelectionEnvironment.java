package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils.Distribution;

import org.apache.log4j.Logger;

public class RandomSelectionEnvironment extends Environment {
	private static final long serialVersionUID = -6984439971874242309L;

	private static final Logger logger = Logger
			.getLogger(RandomSelectionEnvironment.class);

	private double[] selectionCoefficients;
	private Distribution selectionCoefficientDistribution;

	public static RandomSelectionEnvironment getInstance() {
		return (RandomSelectionEnvironment)INSTANCE;
	}

	@Override
	public void init() {
		super.init();

		int numOfAlleles = getNumberOfEnvironmentalGenes()
				+ getNumberOfHousekeepingGenes();

		selectionCoefficients = new double[numOfAlleles];

		double p = selectionCoefficientDistribution.getCdf(1)
				- selectionCoefficientDistribution.getCdf(0);
		if (p > 0.01) {
			logger.warn(String
					.format("Selection coefficient mean %f and standard deviation %f are not suitable, probability of coefficient between 0 and 1 is %f",
							selectionCoefficientDistribution.getMean(),
							selectionCoefficientDistribution
									.getStandardDeviation(), p));
		}
		for (int gene = 0; gene < numOfAlleles; gene++) {
			double s = selectionCoefficientDistribution.nextDouble();
			if (s < 0)
				s = 0;
			else if (s > 1)
				s = 1;
			selectionCoefficients[gene] = s;
		}
	}

	public double getSelectionCoefficient(int gene) {
		return selectionCoefficients[gene];
	}

	public Distribution getSelectionCoefficientDistribution() {
		return selectionCoefficientDistribution;
	}

	public void setSelectionCoefficientDistribution(
			Distribution selectionCoefficientDistribution) {
		this.selectionCoefficientDistribution = selectionCoefficientDistribution;
	}
}
