package il.ac.tau.yoavram.pes.random;

import org.apache.log4j.Logger;

public class FiniteSupportDistributionWrapper implements Distribution {
	private static final long serialVersionUID = -2095328569706687670L;
	private static final Logger logger = Logger
			.getLogger(FiniteSupportDistributionWrapper.class);

	protected Distribution inner;
	protected double min;
	protected double max;
	protected int minInt;
	protected int maxInt;

	public FiniteSupportDistributionWrapper() {
	}

	public FiniteSupportDistributionWrapper(Distribution inner, double min,
			double max) {
		this.inner = inner;
		this.min = min;
		this.max = max;
		rangeMsg();
	}

	public void init() {
		rangeMsg();
	}

	public int nextInt() {
		int a = inner.nextInt();
		if (a < minInt)
			return minInt;
		else if (a > maxInt)
			return maxInt;
		else
			return a;
	}

	public double nextDouble() {
		double a = inner.nextDouble();
		if (a < min)
			return min;
		else if (a > max)
			return max;
		else
			return a;
	}

	public double cdf(double x) {
		return inner.cdf(x);
	}

	public double pdf(double x) {
		return inner.pdf(x);
	}

	public double getMean() {
		return inner.getMean();
	}

	public double getStandardDeviation() {
		return inner.getStandardDeviation();
	}

	public Distribution getInner() {
		return inner;
	}

	public void setInner(Distribution inner) {
		this.inner = inner;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
		minInt = (int) min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
		maxInt = (int) max;
	}

	public double checkSupportRange() {
		return cdf(max) - cdf(min);
	}

	private void rangeMsg() {
		double c = checkSupportRange();
		if (c < 0.95) {
			String msg = String
					.format("Support on %f to %f only makes up %f of the distribution range",
							min, max, c);
			logger.warn(msg);
		}
	}
}
