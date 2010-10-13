package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.terminators.Terminator;
import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.log4j.Logger;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

public class DifferenceEquationsModel implements Model<RealVector>, Terminator {
	private static final Logger logger = Logger
			.getLogger(DifferenceEquationsModel.class);

	private static final int NUMBER_OF_STATES = 50;

	private int n = NUMBER_OF_STATES;
	private double tau = 10;
	private double pi;
	private double mu = 0.01;
	private double gamma = 0.01;
	private double phi = 0.001;
	private double s = 0.1;
	private RealVector f;
	private RealVector prevF;
	private RealVector w;
	private RealMatrix PW;
	private double meanW;
	private Object id;
	private int terminateInterval = 1;
	private double errorThreshold = Math.pow(10, -10);
	private int equilibriumSteps = 0;
	private int equilibriumStepsThreshold = 10;
	private double dist = Double.POSITIVE_INFINITY;

	public void start() {
		long ticks = 0;
		long tick = System.currentTimeMillis();
		while (!terminate()) {
			step();
			ticks++;
		}
		long tock = System.currentTimeMillis();
		logger.info("Finished with " + NumberUtils.formatNumber(ticks)
				+ " ticks in " + TimeUtils.formatDuration(tock - tick));
	}

	@Override
	public void step() {
		for (int i = 0; i < n; i++)
			prevF.setEntry(i, f.getEntry(i));

		f = PW.operate(f);
		f = f.mapDivide(meanW);

		dist = f.getLInfDistance(prevF);
		meanW = f.dotProduct(w);
	}

	@Override
	public void init() {
		if (f == null) {
			this.f = new ArrayRealVector(n);
			f.set(1);
			f = f.mapDivide(n);
			/*
			 * f.set(0); f.setEntry(n - 1, 1);
			 */
			/*
			 * f.set(0); f.setEntry(RandomUtils.nextInt(0, n), 1);
			 */
		}

		prevF = new ArrayRealVector(n);
		RealMatrix P = new Array2DRowRealMatrix(n, n);
		RealMatrix W = new Array2DRowRealMatrix(n, n);
		w = new ArrayRealVector(n);

		double[] m = new double[n];
		for (int i = 0; i < n; i++) {
			if (i < pi) {
				m[i] = mu;
			} else {
				m[i] = tau * mu;
			}
		}

		for (int i = 0; i < n; i++) {
			double wi = Math.pow(1 - s, i);
			W.setEntry(i, i, wi);
			w.setEntry(i, wi);

			if (i > 1) {
				P.setEntry(i, i - 2, Math.pow(m[i - 2], 2) * Math.pow(gamma, 2));
			}
			if (i > 0) {
				P.setEntry(i, i - 1, Math.pow(m[i - 1], 2) * 2 * gamma
						* (1 - gamma - phi) + m[i - 1] * gamma);
			}
			P.setEntry(i, i, 1 - m[i] - Math.pow(m[i], 2) + m[i]
					* (1 - gamma - phi) + Math.pow(m[i], 2)
					* (2 * gamma * phi + (1 - gamma - phi) * (1 - gamma - phi)));
			if (i < n - 1) {
				P.setEntry(i, i + 1, Math.pow(m[i + 1], 2) * 2 * phi
						* (1 - gamma - phi) + m[i + 1] * phi);
			}
			if (i < n - 2) {
				P.setEntry(i, i + 2, Math.pow(m[i + 2], 2) * Math.pow(phi, 2));
			}
		}
		// sanity check
		for (int i = 2; i < n - 2; i++) {
			double sum = 0;
			for (double d : P.getColumn(i)) {
				sum += d;

			}
			if (Math.abs(sum - 1) > getErrorThreshold()) {
				throw new RuntimeException("column " + i
						+ " is not a stochastic vector: " + sum);
			}

		}

		PW = P.multiply(W);
		meanW = f.dotProduct(w);
	}

	@Override
	public boolean terminate() {
		if (dist < getErrorThreshold()) {
			equilibriumSteps++;
			if (equilibriumSteps >= getEquilibriumStepsThreshold()) {
				logger.info("Frequencies: " + Arrays.toString(getFrequencies()));
				logger.info("Distance: " + getDistance());
				logger.info("Mean fitness: " + getMeanFitness());
				return true;
			}
		} else {
			equilibriumSteps = 0;
		}
		return false;
	}

	@Override
	public List<List<RealVector>> getPopulations() {
		return null;
	}

	@Override
	public void setPopulations(List<List<RealVector>> populations) {
	}

	public double[] getFrequencies() {
		return f.getData();
	}

	public void setFrequencies(double[] f) {
		this.f = new ArrayRealVector(f);
		n = this.f.getDimension();
	}

	public void setNumberOfStates(int n) {
		this.n = n;
	}

	public int getNumberOfStates() {
		return n;
	}

	public double getMutationRate() {
		return mu;
	}

	public void setMutationRate(double mu) {
		this.mu = mu;
	}

	public double getMutationRateModifier() {
		return tau;
	}

	public void setMutationRateModifier(double tau) {
		this.tau = tau;
	}

	public double getDeleteriousMutationRate() {
		return gamma;
	}

	public void setDeleteriousMutationRate(double gamma) {
		this.gamma = gamma;
	}

	public double getBeneficialMutationRate() {
		return phi;
	}

	public void setBeneficialMutationRate(double phi) {
		this.phi = phi;
	}

	public double getSelectionCoefficient() {
		return s;
	}

	public void setSelectionCoefficient(double s) {
		this.s = s;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public int getTerminateInterval() {
		return terminateInterval;
	}

	public void setTerminateInterval(int terminateInterval) {
		this.terminateInterval = terminateInterval;
	}

	@Override
	public Object getID() {
		return id;
	}

	@Override
	public void setID(Object id) {
		this.id = id;
	}

	@Override
	public void setInterval(int interval) {
		this.terminateInterval = interval;
	}

	@Override
	public int getInterval() {
		return terminateInterval;
	}

	public void setErrorThreshold(double errorThreshold) {
		this.errorThreshold = errorThreshold;
	}

	public double getErrorThreshold() {
		return errorThreshold;
	}

	public void setEquilibriumStepsThreshold(int equilibriumStepsThreshold) {
		this.equilibriumStepsThreshold = equilibriumStepsThreshold;
	}

	public int getEquilibriumStepsThreshold() {
		return equilibriumStepsThreshold;
	}

	public double getMeanFitness() {
		return meanW;
	}

	public double getDistance() {
		return dist;
	}

	public void setMutationThreshold(double pi) {
		this.pi = pi;
	}

	public double getMutationThreshold() {
		return pi;
	}

	public static void main(String[] args) {
		SortedSetMultimap<Double, Integer> ordered = TreeMultimap.create();
		double[] means = new double[NUMBER_OF_STATES + 1];
		for (int i = 0; i < NUMBER_OF_STATES + 1; i++) {
			DifferenceEquationsModel model = new DifferenceEquationsModel();
			model.setMutationThreshold(i);
			model.init();
			model.start();
			means[i] = model.getMeanFitness();
			ordered.put(means[i], i);
		}
		System.out.println(Arrays.toString(means));
		System.out.println(Arrays.toString(ordered.values().toArray()));
	}
}
