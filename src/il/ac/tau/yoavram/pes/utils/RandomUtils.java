package il.ac.tau.yoavram.pes.utils;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class RandomUtils {
	private static Uniform uniform = new Uniform(0, 1,
			Uniform.makeDefaultGenerator());
	private static Poisson poisson = new Poisson(0.0,
			Poisson.makeDefaultGenerator());

	private RandomUtils() {
	}

	public static double nextDouble() {
		return uniform.nextDouble();
	}

	public static int nextInt(int from, int to) {
		return uniform.nextIntFromTo(from, to);
	}

	public static int nextPoisson(double mean) {
		return poisson.nextInt(mean);
	}

	public static int nextInt() {
		return nextInt(0, Integer.MAX_VALUE);
	}

	public static boolean coinToss() {
		return nextDouble() < 0.5;
	}

}
