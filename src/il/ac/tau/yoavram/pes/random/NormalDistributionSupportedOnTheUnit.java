package il.ac.tau.yoavram.pes.random;

public class NormalDistributionSupportedOnTheUnit extends
		FiniteSupportDistributionWrapper implements Distribution {
	private static final long serialVersionUID = -6408180388995383377L;

	public NormalDistributionSupportedOnTheUnit() {
		super(new NormalDistribution(), 0, 1);
	}

	public NormalDistributionSupportedOnTheUnit(double mean,
			double standardDeviation) {
		super(new NormalDistribution(mean, standardDeviation), 0, 1);
	}

	public void setMean(double mean) {
		((NormalDistribution) inner).setMean(mean);
	}

	public double getMean() {
		return ((NormalDistribution) inner).getMean();
	}

	public void setStandardDeviation(double standardDeviation) {
		((NormalDistribution) inner).setStandardDeviation(standardDeviation);
	}

	public double getStandardDeviation() {
		return ((NormalDistribution) inner).getStandardDeviation();
	}
}
