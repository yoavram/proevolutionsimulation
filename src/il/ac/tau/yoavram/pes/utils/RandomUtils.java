package il.ac.tau.yoavram.pes.utils;

import java.io.Serializable;

import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class RandomUtils {
	private static Uniform uniform = new Uniform(0, 1,
			Uniform.makeDefaultGenerator());
	private static Poisson poisson = new Poisson(0.0,
			Poisson.makeDefaultGenerator());
	private static Normal normal = new Normal(0.0, 1,
			Normal.makeDefaultGenerator());

	/*
	 * private static Binomial binomial = new Binomial(0, 0,
	 * Binomial.makeDefaultGenerator());
	 */

	private RandomUtils() {
	}

	public static double nextDouble() {
		return uniform.nextDouble();
	}

	/**
	 * inclusive
	 * 
	 * @param from
	 * @param to
	 * @return
	 */

	public static int nextInt(int from, int to) {
		return uniform.nextIntFromTo(from, to);
	}

	public static int nextInt() {
		return nextInt(0, Integer.MAX_VALUE);
	}

	public static int nextPoisson(double mean) {
		return poisson.nextInt(mean);
	}

	public static double nextStandardNormal() {
		return normal.nextDouble();
	}

	public static double nextNormal(double mean, double stdev) {
		return normal.nextDouble(mean, stdev);
	}

	/*
	 * public static int nextBinomial(int tests, double success) { return
	 * binomial.nextInt(tests, success); }
	 */

	public static boolean coinToss() {
		return nextDouble() < 0.5;
	}

	/**
	 * Knuth shuffle.
	 * 
	 * @param unshuffled
	 *            int array
	 * 
	 * @see <a
	 *      href=http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle>wiki
	 *      </a>
	 */
	public static void shuffleArray(Object[] array) {
		for (int i = array.length - 1; i > 0; i--) {
			int j = nextInt(0, i);
			Object tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
		}
	}
	
	public static NormalDistribution createNormalDistribution(double mean,
			double standardDeviation) {
		return new NormalDistribution(mean, standardDeviation);
	}

	public static NormalDistribution createStandardNormalDistribution() {
		return new NormalDistribution();
	}

	public static interface Distribution extends Serializable {
		public int nextInt();

		public double nextDouble();

		public double getCdf(double x);

		public double getPdf(double x);

		public double getMean();

		public void setMean(double mean);

		public double getStandardDeviation();

		public void setStandardDeviation(double standardDeviation);
	}

	public static class NormalDistribution implements Distribution {
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

		public int nextInt() {
			return normal.nextInt();
		}

		public double nextDouble() {
			return normal.nextDouble();
		}

		public double getMean() {
			return mean;
		}

		public void setMean(double mean) {
			this.mean = mean;
			normal.setState(mean, standardDeviation);
		}

		public double getStandardDeviation() {
			return standardDeviation;
		}

		public void setStandardDeviation(double standardDeviation) {
			this.standardDeviation = standardDeviation;
			normal.setState(mean, standardDeviation);
		}

		public double getCdf(double x) {
			return normal.cdf(x);
		}

		public double getPdf(double x) {
			return normal.pdf(x);
		}
	}

	public static class Constant implements Distribution {
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
		public double getCdf(double x) {
			if (x < c)
				return 1;
			else
				return 0;
		}

		@Override
		public double getPdf(double x) {
			return 0;
		}

		@Override
		public double getMean() {
			return c;
		}

		@Override
		public void setMean(double mean) {
			c = mean;
		}

		@Override
		public double getStandardDeviation() {
			return 0;
		}

		@Override
		public void setStandardDeviation(double standardDeviation) {

		}
	}

}
