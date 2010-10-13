package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Properties;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Difeq {
	private static final BigDecimal ZERO = BigDecimal.ZERO;
	private static final BigDecimal ONE = BigDecimal.ONE;
	private static final BigDecimal TWO = ONE.add(ONE);
	private static final BigDecimal TEN = BigDecimal.TEN;
	private static final MathContext MC = new MathContext(100,
			RoundingMode.HALF_UP);

	private int n = 5;
	private BigDecimal tau = new BigDecimal("10");
	private BigDecimal pi = ZERO;
	private BigDecimal mu = new BigDecimal("0.01");
	private BigDecimal gamma = new BigDecimal("0.001");
	private BigDecimal phi = new BigDecimal("0.0001");
	private BigDecimal psi = ONE.subtract(gamma).subtract(phi);
	private BigDecimal s = new BigDecimal("0.1");
	private BigDecimal errorThreshold = ONE.divide(TEN.pow(20), MC);
	private int maxIter = 100000;

	public static void main(String[] args) throws IOException {
		Difeq difeq = new Difeq();
		Properties properties = new Properties();
		properties.load(Difeq.class.getClassLoader().getResourceAsStream(
				"dif_eq.properties"));
		difeq.setParameters(properties);
		difeq.go();
	}

	public void go() {
		Multimap<BigDecimal, Integer> meansmap = HashMultimap.create();
		for (int i = 0; i < n; i++) {
			pi = new BigDecimal(i);
			BigDecimal mean = equilibrium();
			meansmap.put(mean, i);
		}
		BigDecimal[] means = meansmap.keys().toArray(new BigDecimal[0]);
		Arrays.sort(means);
		for (BigDecimal key : means) {
			System.out.print(meansmap.get(key).toString().replace("[", "")
					.replace("]", "")
					+ ", ");
		}
		System.out.println();
		System.out.println(Arrays.toString(means));
		for (int i = 1; i < means.length; i++) {
			System.out.print(means[i].divide(means[i - 1], MC)
					.toEngineeringString() + ", ");
		}
		System.out.println();
	}

	public BigDecimal equilibrium() {
		BigDecimal[] current = new BigDecimal[n];
		BigDecimal N = new BigDecimal(n);
		for (int i = 0; i < n; i++) {
			current[i] = ONE.divide(N);
		}
		// sanity check
		if (sum(current).compareTo(BigDecimal.ONE) != 0)
			throw new RuntimeException("Frequencies must sum to 1");

		BigDecimal[] w = new BigDecimal[n];
		for (int i = 0; i < n; i++) {
			w[i] = ONE.subtract(s).pow(i);
		}
		BigDecimal meanW = innerProduct(current, w);

		BigDecimal[] muArr = new BigDecimal[n];
		for (int i = 0; i < n; i++) {
			if (i < pi.intValue())
				muArr[i] = mu;
			else
				muArr[i] = tau.multiply(mu);
		}

		BigDecimal[] prev = new BigDecimal[n];
		BigDecimal[] sel = new BigDecimal[n];

		int iter = 0;
		BigDecimal dist = BigDecimal.valueOf(Double.MAX_VALUE);
		long tick = System.currentTimeMillis();

		while (dist.compareTo(errorThreshold) > 0 && iter < maxIter) {
			iter++;
			prev = Arrays.copyOf(current, n);

			// selection
			for (int i = 0; i < n; i++) {
				sel[i] = current[i].multiply(w[i]).divide(meanW, MC);
			}
			// mutation TODO make sure it sums for 1 for i=0,1,n-1,n
			for (int i = 0; i < n; i++) {
				BigDecimal m = get(muArr, i - 2);
				current[i] = get(sel, i - 2).multiply(m.pow(2)).multiply(
						gamma.pow(2));

				m = get(muArr, i - 1);
				current[i] = current[i].add(get(sel, i - 1).multiply(
						TWO.multiply(m.pow(2)).multiply(gamma).multiply(psi)
								.add(m.multiply(gamma))));

				m = get(muArr, i);
				current[i] = current[i].add(get(sel, i).multiply(
						ONE.subtract(m)
								.subtract(m.pow(2))
								.add(m.multiply(psi))
								.add(m.pow(2).multiply(
										TWO.multiply(gamma).multiply(phi)
												.add(psi.pow(2))))));

				m = get(muArr, i + 1);
				current[i] = current[i].add(get(sel, i + 1).multiply(
						TWO.multiply(m.pow(2)).multiply(phi).multiply(psi)
								.add(m.multiply(phi))));

				m = get(muArr, i + 2);
				current[i] = current[i].add(get(sel, i + 2).multiply(
						m.pow(2).multiply(phi.pow(2))));
			}

			BigDecimal oldMeanW = meanW;
			meanW = innerProduct(current, w);
			dist = oldMeanW.subtract(meanW).abs();
			// dist = distanceLInf(current, prev);
		}
		long tock = System.currentTimeMillis();
		System.out.println("Finished with " + NumberUtils.formatNumber(iter)
				+ " iterations, distance " + dist.toEngineeringString()
				+ " in " + (tock - tick) + " millisecs");
		return meanW;
	}

	public BigDecimal get(BigDecimal[] arr, int ind) {
		if (ind < 0 || ind >= arr.length)
			return ZERO;
		else
			return arr[ind];
	}

	public BigDecimal innerProduct(BigDecimal[] arr1, BigDecimal[] arr2) {
		if (arr1.length != arr2.length)
			throw new IllegalArgumentException(
					"Arrays lengths must agree: arr1 length " + arr1.length
							+ ", arr2 length " + arr2.length);
		BigDecimal res = ZERO;
		for (int i = 0; i < arr1.length; i++) {
			res = res.add(arr1[i].multiply(arr2[i]));
		}
		return res;
	}

	public BigDecimal sum(BigDecimal[] array) {
		BigDecimal sum = ZERO;
		for (int i = 0; i < array.length; i++) {
			sum = sum.add(array[i]);
		}
		return sum;
	}

	public BigDecimal distanceLInf(BigDecimal[] arr1, BigDecimal[] arr2) {
		if (arr1.length != arr2.length)
			throw new IllegalArgumentException(
					"Arrays lengths must agree: arr1 length " + arr1.length
							+ ", arr2 length " + arr2.length);
		BigDecimal d = ZERO;
		for (int i = 0; i < arr1.length; i++) {
			d = arr1[i].subtract(arr2[i]).abs().max(d);
		}
		return d;
	}

	public BigDecimal distanceL1(BigDecimal[] arr1, BigDecimal[] arr2) {
		if (arr1.length != arr2.length)
			throw new IllegalArgumentException(
					"Arrays lengths must agree: arr1 length " + arr1.length
							+ ", arr2 length " + arr2.length);
		BigDecimal d = ZERO;
		for (int i = 0; i < arr1.length; i++) {
			d = d.add(arr1[i].subtract(arr2[i]).abs());
		}
		return d;
	}

	public void setParameters(Properties properties) {
		n = Integer.valueOf(properties.getProperty("n"));
		tau = parseProperty(properties, "tau");
		mu = parseProperty(properties, "mu");
		gamma = parseProperty(properties, "gamma");
		phi = parseProperty(properties, "phi");
		psi = ONE.subtract(gamma).subtract(phi);
		s = parseProperty(properties, "s");
		errorThreshold = parseProperty(properties, "errorThreshold");
		maxIter = Integer.valueOf(properties.getProperty("maxIter"));
	}

	public BigDecimal parseProperty(Properties properties, String property) {
		return new BigDecimal(properties.getProperty(property),
				MathContext.UNLIMITED);

	}

}
