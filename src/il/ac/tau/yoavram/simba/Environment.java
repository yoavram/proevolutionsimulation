package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class Environment implements Serializable {
	private static final long serialVersionUID = -2665663049159958614L;
	private static final Logger logger = Logger.getLogger(Environment.class);

	private static Environment INSTANCE = null;

	private int numberOfEnvironmentalGenes;
	private int[] alleles;
	private transient long lastEnvironmentalChange = 0;

	// TODO make this private and lazy init in getInstance, and remove set
	// instance from readObject()
	public Environment() {
		INSTANCE = this;
	}

	public static Environment getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public void init() {
		alleles = new int[getNumberOfEnvironmentalGenes()];
		for (int gene = 0; gene < alleles.length; gene++) {
			alleles[gene] = RandomUtils.nextInt(0, 1);
		}
	}

	public void change(double fractionOfGenesToChange) {
		double toChange = Math.ceil(fractionOfGenesToChange * alleles.length);
		for (int i = 0; i < toChange; i++) {
			int gene = RandomUtils.nextInt(0, alleles.length - 1);
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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Environment
				&& Arrays.equals(this.alleles, ((Environment) obj).alleles);
	}

	public int getIdealAllele(int gene) {
		return alleles[gene];
	}

	public long getLastEnvironmentalChange() {
		return lastEnvironmentalChange;
	}

	private long getTick() {
		return Simulation.getInstance().getTick();
	}

	public int getNumberOfEnvironmentalGenes() {
		return numberOfEnvironmentalGenes;
	}

	public void setNumberOfEnvironmentalGenes(int numberOfEnvironmentalGenes) {
		this.numberOfEnvironmentalGenes = numberOfEnvironmentalGenes;
	}

}
