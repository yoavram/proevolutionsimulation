package il.ac.tau.yoavram.pes.tests;

import il.ac.tau.yoavram.pes.utils.Randomizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.apache.commons.math.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

@org.junit.Ignore
public class TestColt {

	Uniform coltUniform;
	Poisson coltPoisson;
	Random javaRand;
	MersenneTwister apacheTwister;
	PoissonDistribution apachePoisson;
	Randomizer pesRandomizer;
//	Ranlux ranlux;
//	Ranmar ranmar;

	@Before
	public void beforeClass() {
		coltUniform = new Uniform(0, 1, Uniform.makeDefaultGenerator());
		coltPoisson = new Poisson(poissonMean, Poisson.makeDefaultGenerator());
		javaRand = new Random();
		apacheTwister = new MersenneTwister();
		apachePoisson = new PoissonDistributionImpl(poissonMean);
		// pesRandomizer = new Randomizer();
	//	ranlux = new Ranlux(2, new Date());
	//	ranmar = new Ranmar();
	}

	@Test
	public void nextDoubleTimeTest() {
		int times = 10000;
		int tests = 10000;
		long java = 0;
		long coltStatic = 0;
		long coltInstance = 0;
		long apacheT = 0;
		long pes = 0;
	//	long ranluxT = 0;
	//	long ranmarT = 0;

		for (int i = 0; i < tests; i++) {
			java += timeJavaDouble(times);
			coltStatic += timeColtStaticDouble(times);
			coltInstance += timeColtInstanceDouble(times);
			apacheT += timeApacheTwisterDouble(times);
			// pes += timePesRandomizerDouble(times);
		//	ranluxT += timeRanluxDouble(times);
		//	ranmarT += timeRanmarDouble(times);
		}

		System.out.println("java double rand: " + java);

		System.out.println("colt double static rand: " + coltStatic);

		System.out.println("colt double instance rand: " + coltInstance);

		System.out.println("apache double instance rand: " + apacheT);

		System.out.println("pes double instance rand: " + pes);

	//	System.out.println("ranlux double instance rand: " + ranluxT);

	//	System.out.println("ranmar double instance rand: " + ranmarT);

	}

	@Test
	public void nextIntTimeTest() {
		int times = 10000;
		int tests = 10000;
		long java = 0;
		long coltStatic = 0;
		long coltInstance = 0;
		long apacheT = 0;
		long pes = 0;
	//	long ranluxT = 0;
	//	long ranmarT = 0;

		for (int i = 0; i < tests; i++) {
			java += timeJavaInt(times);
			coltStatic += timeColtStaticInt(times);
			coltInstance += timeColtInstanceInt(times);
			apacheT += timeApacheTwisterInt(times);
			// pes+=timePesRandomizerInt(times);
			// ranluxT+=timeRanluxInt(times);
		//	ranmarT += timeRanmarInt(times);
		}

		System.out.println("java int rand: " + java);

		System.out.println("colt int static rand: " + coltStatic);

		System.out.println("colt int instance rand: " + coltInstance);

		System.out.println("apache int instance rand: " + apacheT);

		System.out.println("pes int instance rand: " + pes);

	//	System.out.println("ranlux int instance rand: " + ranluxT);

	//	System.out.println("ranmar int instance rand: " + ranmarT);

	}

	@Test
	public void nextPoissonTimeTest() {
		int times = 10000;
		int tests = 10000;
		long coltStatic = 0;
		long coltInstance = 0;
		for (int i = 0; i < tests; i++) {
			coltStatic += timeColtStaticPoisson(times);
			coltInstance += timeColtInstancePoisson(times);
		}

		System.out.println("colt poisson static rand: " + coltStatic);

		System.out.println("colt poisson instance rand: " + coltInstance);

	}

	@Test
	public void nextDoubleDistributionTest() {
		int times = 10000000;

		double mean = 0;
		double meanSq = 0;

		for (int i = 0; i < times; i++) {
			double d = apacheTwister.nextDouble();
			mean = (mean * i + d) / (i + 1);
			meanSq = (meanSq * i + Math.pow(d, 2)) / (i + 1);
		}

		double meanError1 = Math.abs(0.5 - mean);
		System.out.println("apache instance double mean " + meanError1);
		double varError1 = Math.pow(12, (-1)) - (meanSq - Math.pow(mean, 2));
		System.out.println("apache instance double variance " + varError1);

		mean = 0;
		meanSq = 0;

		for (int i = 0; i < times; i++) {
			double d = coltUniform.nextDouble();
			mean = (mean * i + d) / (i + 1);
			meanSq = (meanSq * i + Math.pow(d, 2)) / (i + 1);
		}

		double meanError2 = Math.abs(0.5 - mean);
		System.out.println("colt instance double mean " + meanError2);
		double varError2 = Math.pow(12, (-1)) - (meanSq - Math.pow(mean, 2));
		System.out.println("colt instance double variance " + varError2);
/*
		mean = 0;
		meanSq = 0;

		for (int i = 0; i < times; i++) {
			double d = ranmar.raw();
			mean = (mean * i + d) / (i + 1);
			meanSq = (meanSq * i + Math.pow(d, 2)) / (i + 1);
		}

		double meanError3 = Math.abs(0.5 - mean);
		System.out.println("ranmar instance double mean " + meanError3);
		double varError3 = Math.pow(12, (-1)) - (meanSq - Math.pow(mean, 2));
		System.out.println("ranmar instance double variance " + varError3);
*/
	}

	long timeJavaDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			javaRand.nextDouble();
		long tock = System.currentTimeMillis();
		return tock - tick;
	}

	long timeColtStaticDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			Uniform.staticNextDouble();
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timeColtInstanceDouble(double times) {

		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			coltUniform.nextDouble();
		long tock = System.currentTimeMillis();
		return tock - tick;
	}

	long timeApacheTwisterDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			apacheTwister.nextDouble();
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timePesRandomizerDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			pesRandomizer.nextDouble();
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

/*	long timeRanluxDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			ranlux.raw();
		long tock = System.currentTimeMillis();
		return tock - tick;

	}*/

/*	long timeRanmarDouble(double times) {
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			ranmar.raw();
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timeRanluxInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			ranlux.choose(0, to);
		long tock = System.currentTimeMillis();
		return tock - tick;

	}*/

	long timeJavaInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			javaRand.nextInt(to);
		long tock = System.currentTimeMillis();
		return tock - tick;
	}

	long timeColtStaticInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			Uniform.staticNextIntFromTo(0, to);
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timeColtInstanceInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			coltUniform.nextIntFromTo(0, to);
		long tock = System.currentTimeMillis();
		return tock - tick;
	}

	long timeApacheTwisterInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			apacheTwister.nextInt(to);
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timePesRandomizerInt(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			pesRandomizer.nextInt(0, to);
		long tock = System.currentTimeMillis();
		return tock - tick;

	}


	static double poissonMean = 0.01;

	long timeColtStaticPoisson(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			Poisson.staticNextInt(poissonMean);
		long tock = System.currentTimeMillis();
		return tock - tick;

	}

	long timeColtInstancePoisson(double times) {
		int to = Integer.MAX_VALUE;
		long tick = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			coltPoisson.nextInt();
		long tock = System.currentTimeMillis();
		return tock - tick;
	}
}
