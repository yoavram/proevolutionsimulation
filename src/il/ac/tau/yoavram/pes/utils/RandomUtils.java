package il.ac.tau.yoavram.pes.utils;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class RandomUtils {
	private RandomUtils() {
	}

	public static double nextDouble() {
		return Uniform.staticNextDouble();
	}

	public static int nextInt(int from, int to) {
		return Uniform.staticNextIntFromTo(from, to);
	}

	public static int nextPoisson(double mean) {
		return Poisson.staticNextInt(mean);
	}
}
