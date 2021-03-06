package il.ac.tau.yoavram.pes.utils;

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
}
