package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.io.CsvWriter;
import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;

public class Difeq {
	private static final BigDecimal ZERO = BigDecimal.ZERO;
	private static final BigDecimal ONE = BigDecimal.ONE;
	private static final BigDecimal TWO = ONE.add(ONE);
	private static final BigDecimal TEN = BigDecimal.TEN;
	private static final BigDecimal HUNDRED = TEN.multiply(TEN);
	private static final BigDecimal TENTH = ONE.divide(TEN);
	private static final BigDecimal HUNDREDTH = ONE.divide(HUNDRED);
	private static final BigDecimal THOUSAND = TEN.multiply(HUNDRED);
	private static final BigDecimal THOUSANDTH = ONE.divide(THOUSAND);
	private static final BigDecimal FIVE = TEN.divide(TWO);
	private static final BigDecimal FIFTH = ONE.divide(FIVE);
	private static final BigDecimal HALF = ONE.divide(TWO);

	private static final MathContext MC = new MathContext(100,
			RoundingMode.HALF_UP);

	private static final String log4jConfigFilename = "log4j.properties";

	private int n;
	private BigDecimal tau;
	private BigDecimal pi;
	private BigDecimal mu;
	private BigDecimal gamma;
	private BigDecimal phi;
	private BigDecimal psi;
	private BigDecimal s;
	private BigDecimal errorThreshold;
	private int maxIter;

	private String jobName = "difeq";

	public static void main(String[] args) throws IOException {
		Difeq difeq = new Difeq();
		if (args.length > 0)
			difeq.setParameters(new DifeqCommandLineParser(args));
		else
			difeq.setParameters("dif_eq.properties");
		difeq.start();
	}

	public void start() throws IOException {
		String params = "n=" + n + " tau=" + tau + " mu=" + mu + " gamma="
				+ gamma + " phi=" + phi + " err=" + errorThreshold + " iter="
				+ maxIter;
		CsvWriter writer = createCsvWriter(params);
		writer.writeCell(params);
		writer.newRow();
		System.out.println(params);

		for (int i = 0; i < n; i++) {
			writer.writeCell(i);
		}
		writer.newRow();

		for (int i = 0; i < n; i++) {
			pi = new BigDecimal(i);
			BigDecimal mean = equilibrium();
			writer.writeCell(mean);
		}
		writer.newRow();
		System.out.println();

		writer.close();
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

		// BigDecimal[] prev = new BigDecimal[n];
		BigDecimal[] sel = new BigDecimal[n];

		int iter = 0;
		BigDecimal dist = BigDecimal.valueOf(Double.MAX_VALUE);
		long tick = System.currentTimeMillis();

		while (dist.compareTo(errorThreshold) > 0 && iter < maxIter) {
			iter++;
			// prev = Arrays.copyOf(current, n);

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
				+ " in " + (tock - tick) + " millisecs: " + pi + " - " + meanW);
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

	public void setParameters(String propertiesFilename) throws IOException {
		Properties properties = new Properties();
		properties.load(Difeq.class.getClassLoader().getResourceAsStream(
				propertiesFilename));
		setParameters(properties);
	}

	public void setParameters(Properties properties) {
		System.out.println("set parameters: " + properties.toString());
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

	public void setParameters(DifeqCommandLineParser parser) {
		n = parser.getN();
		tau = parser.getTau();
		mu = parser.getMu();
		gamma = parser.getGamma();
		phi = parser.getPhi();
		psi = ONE.subtract(gamma).subtract(phi);
		s = parser.getS();
		errorThreshold = parser.getErr();
		maxIter = parser.getIter();
	}

	public BigDecimal parseProperty(Properties properties, String property) {
		return new BigDecimal(properties.getProperty(property),
				MathContext.UNLIMITED);
	}

	private Logger createLogger() throws IOException {
		Logger logger = Logger.getLogger(jobName);
		Properties log4jProps = loadPropertiesFromClasspath(new Properties(),
				log4jConfigFilename);
		String logFilename = "difeq-" + jobName + ".log";
		log4jProps.setProperty("log4j.appender.FILE.File", logFilename);
		PropertyConfigurator.configure(log4jProps);
		logger.info("logging to " + logFilename);
		return logger;
	}

	private CsvWriter createCsvWriter(String id) throws IOException {
		CsvWriter w = new CsvWriter();
		w.setDirectory("output/difeq");
		w.setFilename("difeq_" + id);
		w.setTime(new Date());
		w.init();
		return w;
	}

	private Properties loadPropertiesFromClasspath(Properties props,
			String filename) throws IOException {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		props.load(stream);
		return props;
	}
}
