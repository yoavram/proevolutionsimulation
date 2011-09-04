package il.ac.tau.yoavram.pes.random;

import cern.jet.random.Exponential;

public class ExponentialDistribution implements Distribution {
	private static final long serialVersionUID = -6287386702876697377L;
	private Exponential exponential;
	private double lambda;

	public ExponentialDistribution() {
		this(0.5);
	}

	public ExponentialDistribution(double lambda) {
		this.lambda = lambda;
		exponential = new Exponential(lambda,
				Exponential.makeDefaultGenerator());
	}

	public int nextInt() {
		return exponential.nextInt();
	}

	public double nextDouble() {
		return exponential.nextDouble();
	}

	public double getMean() {
		return 1 / lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
		exponential.setState(lambda);
	}

	public double getStandardDeviation() {
		return 1 / lambda;
	}

	public double cdf(double x) {
		return exponential.cdf(x);
	}

	public double pdf(double x) {
		return exponential.pdf(x);
	}
}
