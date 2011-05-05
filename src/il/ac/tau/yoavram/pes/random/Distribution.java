package il.ac.tau.yoavram.pes.random;

import java.io.Serializable;

public interface Distribution extends Serializable {
	public int nextInt();

	public double nextDouble();

	public double cdf(double x);

	public double pdf(double x);

	public double getMean();

	public double getStandardDeviation();

}
