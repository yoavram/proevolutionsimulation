package il.ac.tau.yoavram.pes.random;

public class Constant implements Distribution {
	private static final long serialVersionUID = -6747857666365282177L;
	double c;

	public Constant() {

	}

	public Constant(double constant) {
		c = constant;
	}

	@Override
	public int nextInt() {
		return (int) Math.round(c);
	}

	@Override
	public double nextDouble() {
		return c;
	}

	@Override
	public double cdf(double x) {
		if (x < c)
			return 1;
		else
			return 0;
	}

	@Override
	public double pdf(double x) {
		return 0;
	}

	@Override
	public double getMean() {
		return c;
	}

	@Override
	public double getStandardDeviation() {
		return 0;
	}

	public double getConstant() {
		return c;
	}

	public void setConstant(double constant) {
		this.c = constant;
	}

}
