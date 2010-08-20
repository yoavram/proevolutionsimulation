package il.ac.tau.yoavram.simba;

import java.io.Serializable;

import org.apache.log4j.Logger;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class Environment implements Serializable {
	private static final long serialVersionUID = -2665663049159958614L;

	private static final Logger logger = Logger.getLogger(Environment.class);

	private int[] alleles;
	private transient long lastEnvironmentalChange = 0;

	public void change(double fractionOfGenesToChange) {
		double mean = fractionOfGenesToChange * alleles.length;
		int toChange = Poisson.staticNextInt(mean);
		for (int i = 0; i < toChange; i++) {
			int gene = Uniform.staticNextIntFromTo(0, alleles.length);
			int currentAllele = alleles[gene];
			int newAllele = (currentAllele + 1) % 2;
			alleles[gene] = newAllele;
			logger.debug("Changed the environmental gene " + gene + " from "
					+ currentAllele + " to " + newAllele);
		}
		if (toChange > 0) {
			lastEnvironmentalChange = getTick();
		}
	}

	public int getIdealAllele(int gene) {
		return alleles[gene];
	}

	public long getLastEnvironmentalChange() {
		return lastEnvironmentalChange;
	}

	private long getTick() {
		// TODO - INJECT
		return Integer.MAX_VALUE;
	}
}
