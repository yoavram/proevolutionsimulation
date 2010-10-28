package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.math.LinearAlgebra;
import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Difeq {
	private static Logger logger;
	private static final BigDecimal ZERO = BigDecimal.ZERO;
	private static final BigDecimal ONE = BigDecimal.ONE;
	private static final BigDecimal TWO = ONE.add(ONE);

	private static MathContext MC = new MathContext(50, RoundingMode.HALF_EVEN);

	private static final String log4jConfigFilename = "log4j.properties";

	private int n;
	private BigDecimal tau;
	private BigDecimal pi;
	private BigDecimal gamma;
	private BigDecimal phi;
	// private BigDecimal psi;
	private BigDecimal s;
	private BigDecimal errorThreshold;
	private int maxIter;
	private int precision;

	public static void main(String[] args) throws IOException {
		Difeq difeq = new Difeq();
		if (args.length > 0)
			difeq.setParameters(new DifeqCommandLineParser(args));
		else
			difeq.setParameters("dif_eq.properties");
		difeq.start();
	}

	public void start() {
		if (precision > 0) {
			MC = new MathContext(precision, RoundingMode.HALF_EVEN);
			errorThreshold = new BigDecimal("1e-" + precision);
		}
		String params = "n-" + n + " tau-" + tau + " pi-" + pi + " gamma-"
				+ gamma + " phi-" + phi + " err-" + errorThreshold + " iter-"
				+ maxIter;
		try {
			logger = createLogger(params.replace(' ', '_'));
		} catch (IOException e) {
			System.err.println("Failed creating logger for " + params + ": "
					+ e);
		}
		File file = new File("output/difeq/" + "difeq_"
				+ params.replace(' ', '_'));
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			logger.error(e);
			System.exit(1);
		}
		logger.info("writing to " + file.getAbsolutePath());

		writer.println(params);
		BigDecimal meanW = equilibrium();
		logger.info(meanW);
		writer.println(meanW.round(MC).toString());
		writer.close();
	}

	public BigDecimal equilibrium() {
		BigDecimal[] current = new BigDecimal[n];
		BigDecimal A = ONE.divide(BigDecimal.valueOf(n));
		for (int i = 0; i < n; i++) {
			current[i] = A;
		}

		sanityCheckStochasticVector(current, "frequencies");
		sanityCheckProbability(tau.multiply(gamma),
				"Deleterious mutation probability");
		sanityCheckProbability(tau.multiply(phi),
				"Beneficial mutation probability");
		sanityCheckProbability(tau.multiply(phi.add(gamma)),
				"Mutation probability");

		BigDecimal[] w = createSelectionVector();
		BigDecimal meanW = LinearAlgebra.innerProduct(current, w);
		BigDecimal[][] M = createMutationMatrix();
		/*
		 * logger.info("Mutation matrix:"); logger.info(M);
		 */

		int iter = 0;
		BigDecimal dist = BigDecimal.valueOf(Double.MAX_VALUE);
		long tick = System.currentTimeMillis();

		while (dist.compareTo(errorThreshold) > 0 && maxIter > iter) {
			iter++;
			if (iter % 100 == 0)
				logger.info(iter + " " + dist.toEngineeringString());

			// selection
			current = LinearAlgebra.vectorProduct(current, w,
					ONE.divide(meanW, MC));
			// sanityCheckStochasticVector(current, "freqs after selection");

			current = LinearAlgebra.unsafeMatrixOperationSparse(M, current);
			// sanityCheckStochasticVector(current, "freqs after mutation");

			BigDecimal oldMeanW = meanW;
			meanW = LinearAlgebra.innerProduct(current, w);
			sanityCheckProbability(meanW, "mean fitness");

			dist = oldMeanW.subtract(meanW, MC).abs();
		}
		long tock = System.currentTimeMillis();
		logger.info("Finished with " + NumberUtils.formatNumber(iter)
				+ " iterations, distance " + dist.toEngineeringString()
				+ " in " + (tock - tick) + " millisecs");
		return meanW;
	}

	public BigDecimal[] createSelectionVector() {
		BigDecimal[] w = new BigDecimal[n];
		for (int i = 0; i < n; i++) {
			w[i] = (ONE.subtract(s)).pow(i);
		}
		return w;
	}

	public BigDecimal[][] createMutationMatrix() {
		BigDecimal[][] M = new BigDecimal[n][n];
		for (int j = 0; j < n; j++) {
			BigDecimal gammaStar = j < pi.intValue() ? gamma : gamma
					.multiply(tau);
			BigDecimal phiStar = j < pi.intValue() ? phi : phi.multiply(tau);
			BigDecimal psi = ONE.subtract(gammaStar).subtract(phiStar);

			BigDecimal[] mm = new BigDecimal[5];
			mm[4] = gammaStar.pow(2);
			sanityCheckProbability(mm[4], j + "->" + (j + 2));
			mm[0] = phiStar.pow(2);
			sanityCheckProbability(mm[0], j + "->" + (j - 2));
			mm[3] = TWO.multiply(gammaStar).multiply(psi);
			sanityCheckProbability(mm[3], j + "->" + (j + 1));
			mm[1] = TWO.multiply(phiStar).multiply(psi);
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
		sanityCheckStochasticMatrix(LinearAlgebra.transpose(M),
				"Transpose of Transition matrix");
		return M;
	}

	public void sanityCheckStochasticVector(BigDecimal[] v, String name) {
		if (LinearAlgebra.sum(v).subtract(ONE, MC).compareTo(ZERO) != 0) {
			throw new RuntimeException("sum of '" + name
					+ "' frequencies must be 1, it is: " + LinearAlgebra.sum(v));
		}
	}

	public void sanityCheckStochasticMatrix(BigDecimal[][] m, String name) {
		for (int i = 0; i < m.length; i++) {
			BigDecimal[] v = m[i];
			sanityCheckStochasticVector(v, m + " column " + i);
		}
	}

	public void sanityCheckProbability(BigDecimal p, String name) {
		// sanity check
		if (p.compareTo(ZERO) < 0 || p.compareTo(ONE) > 0) {
			throw new RuntimeException(name + " must be 0<x<1, it is " + p);
		}
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
		gamma = parseProperty(properties, "gamma");
		phi = parseProperty(properties, "phi");
		pi = parseProperty(properties, "pi");
		s = parseProperty(properties, "s");
		errorThreshold = parseProperty(properties, "errorThreshold");
		maxIter = Integer.valueOf(properties.getProperty("maxIter"));
		precision = Integer.valueOf(properties.getProperty("precision"));
	}

	public void setParameters(DifeqCommandLineParser parser) {
		n = parser.getN();
		tau = parser.getTau();
		gamma = parser.getGamma();
		phi = parser.getPhi();
		pi = parser.getPi();
		s = parser.getS();
		errorThreshold = parser.getErr();
		maxIter = parser.getIter();
		precision = parser.getPrecision();
	}

	public BigDecimal parseProperty(Properties properties, String property) {
		return new BigDecimal(properties.getProperty(property),
				MathContext.UNLIMITED);
	}

	private Logger createLogger(String id) throws IOException {
		Logger logger = Logger.getLogger(id.replace('.', '_'));
		Properties log4jProps = loadPropertiesFromClasspath(new Properties(),
				log4jConfigFilename);
		String logFilename = "log/difeq/difeq-" + id + ".log";
		log4jProps.setProperty("log4j.appender.FILE.File", logFilename);
		PropertyConfigurator.configure(log4jProps);
		logger.info("logging to " + logFilename);
		return logger;
	}

	private Properties loadPropertiesFromClasspath(Properties props,
			String filename) throws IOException {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		props.load(stream);
		return props;
	}

}
