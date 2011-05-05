package il.ac.tau.yoavram.pes.random;

import cern.jet.random.Normal;

public class NormalDistribution implements Distribution {
	private static final long serialVersionUID = -3432554780516962290L;
	private Normal normal;
	private double mean;
	private double standardDeviation;

	public NormalDistribution() {
		this(0, 1);
	}

	public NormalDistribution(double mean, double standardDeviation) {
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		normal = new Normal(mean, standardDeviation,
				Normal.makeDefaultGenerator());
	}

	@Override
	public int nextInt() {
		return normal.nextInt();
	}

	@Override
	public double nextDouble() {
		return normal.nextDouble();
	}

	@Override
	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
		normal.setState(mean, standardDeviation);
	}

	@Override
	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
		normal.setState(mean, standardDeviation);
	}

	@Override
	public double cdf(double x) {
		return normal.cdf(x);
	}

	@Override
	public double pdf(double x) {
		return normal.pdf(x);
	}

	public static Distribution createNormalDistributionSupportedOnTheUnit(
			double mean, double standardDeviation) {
		return new FiniteSupportDistributionWrapper(new NormalDistribution(
				mean, standardDeviation), 0, 1);
	}
}
