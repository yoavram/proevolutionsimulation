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
		System.out.println(params);

		writer.writeCell("params");
		for (int i = 0; i < n; i++) {
			writer.writeCell(i);
		}
		writer.newRow();

		writer.writeCell(params);
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

		sanityCheckStochasticVector(current, "frequencies");
		sanityCheckProbability(mu.multiply(tau).multiply(gamma),
				"Deleterious mutation probability");
		sanityCheckProbability(mu.multiply(tau).multiply(phi),
				"Beneficial mutation probability");

		BigDecimal[] w = createSelectionVector();
		BigDecimal meanW = innerProduct(current, w);
		BigDecimal[][] M = createMutationMatrix();
	//	printMatrix(M);

		int iter = 0;
		BigDecimal dist = BigDecimal.valueOf(Integer.MAX_VALUE);
		long tick = System.currentTimeMillis();

		while (dist.compareTo(errorThreshold) > 0 && iter < maxIter) {
			iter++;

			// selection
			for (int i = 0; i < n; i++) {
				current[i] = current[i].multiply(w[i]).divide(meanW, MC);
			}
			//sanityCheckStochasticVector(current, "freqs after selection");

			current = matrixOperation(M, current);
		//	sanityCheckStochasticVector(current, "freqs after mutation");

			BigDecimal oldMeanW = meanW;
			meanW = innerProduct(current, w);
			sanityCheckProbability(meanW, "mean fitness");

			dist = oldMeanW.subtract(meanW).abs();
		}
		long tock = System.currentTimeMillis();
		System.out.println("Finished with " + NumberUtils.formatNumber(iter)
				+ " iterations, distance " + dist.toEngineeringString()
				+ " in " + (tock - tick) + " millisecs: " + pi + " - " + meanW);
		return meanW;
	}

	public BigDecimal[] createSelectionVector() {
		BigDecimal[] w = new BigDecimal[n];
		for (int i = 0; i < n; i++) {
			w[i] = ONE.subtract(s).pow(i);
		}
		return w;
	}

	public BigDecimal[][] createMutationMatrix() {
		BigDecimal[][] M = new BigDecimal[n][n];
		for (int j = 0; j < n; j++) {
			BigDecimal m = j < pi.intValue() ? mu : mu.multiply(tau);
			BigDecimal[] mm = new BigDecimal[5];
			mm[4] = m.pow(2).multiply(gamma.pow(2));
			sanityCheckProbability(mm[4], j + "->" + (j + 2));
			mm[0] = m.pow(2).multiply(phi.pow(2));
			sanityCheckProbability(mm[0], j + "->" + (j - 2));
			mm[3] = TWO.multiply(m.pow(2)).multiply(gamma).multiply(psi)
					.add(m.multiply(gamma));
			sanityCheckProbability(mm[3], j + "->" + (j + 1));
			mm[1] = TWO.multiply(m.pow(2)).multiply(phi).multiply(psi)
					.add(m.multiply(phi));
			sanityCheckProbability(mm[1], j + "->" + (j - 1));
			mm[2] = ZERO;

			BigDecimal stay = ONE;
			for (int i = 0; i < n; i++) {
				if (j - i >= -2 && 2 >= j - i && j != i) {
					M[i][j] = mm[j - i + 2];
					stay = stay.subtract(mm[j - i + 2]);
				} else {
					M[i][j] = ZERO;
				}
			}
			M[j][j] = stay;
		}
		return M;
	}

	public void sanityCheckStochasticVector(BigDecimal[] v, String name) {
		if (sum(v).subtract(ONE, MC).compareTo(ZERO) != 0) {
			throw new RuntimeException("sum of '" + name
					+ "' frequencies must be 1, it is: " + sum(v));
		}
	}

	public void sanityCheckProbability(BigDecimal p, String name) {
		// sanity check
		if (p.compareTo(ZERO) < 0 || p.compareTo(ONE) > 0) {
			throw new RuntimeException(name + " mus't be 0<x<1, it is " + p);
		}
	}

	public BigDecimal[] matrixOperation(BigDecimal[][] m, BigDecimal[] v) {
		// not checking dimensions, assuming square matrix
		BigDecimal[] u = new BigDecimal[v.length];
		for (int i = 0; i < v.length; i++) {
			u[i] = innerProduct(m[i], v);
		}
		return u;
	}

	public void printMatrix(BigDecimal[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j]);
				System.out.print(", ");
			}
			System.out.println();
		}
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
		w.setFilename("difeq_" + id.replace(' ', '_'));
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
