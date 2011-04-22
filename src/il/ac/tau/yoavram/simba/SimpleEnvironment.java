package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

public class SimpleEnvironment implements Environment {
	private static final long serialVersionUID = 8898297052351732121L;

	private static final Logger logger = Logger.getLogger(Environment.class);

	private static Environment INSTANCE = null;

	private int numberOfEnvironmentalGenes;
	private int[] alleles;
	private transient long lastEnvironmentalChange = 0;

	// TODO make this private and lazy init in getInstance, and remove set
	// instance from readObject()
	public SimpleEnvironment() {
		INSTANCE = this;
	}

	public static Environment getInstance() {
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#init()
	 */
	@Override
	public void init() {
		alleles = new int[getNumberOfEnvironmentalGenes()];
		for (int gene = 0; gene < alleles.length; gene++) {
			alleles[gene] = RandomUtils.nextInt(0, 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#change(double)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SimpleEnvironment
				&& Arrays.equals(this.alleles,
						((SimpleEnvironment) obj).alleles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#getIdealAllele(int)
	 */
	@Override
	public int getIdealAllele(int gene) {
		return alleles[gene];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#getLastEnvironmentalChange()
	 */
	@Override
	public long getLastEnvironmentalChange() {
		return lastEnvironmentalChange;
	}

	private long getTick() {
		return Simulation.getInstance().getTick();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.tau.yoavram.simba.IEnvironment#getNumberOfEnvironmentalGenes()
	 */
	@Override
	public int getNumberOfEnvironmentalGenes() {
		return numberOfEnvironmentalGenes;
	}

	public void setNumberOfEnvironmentalGenes(int numberOfEnvironmentalGenes) {
		this.numberOfEnvironmentalGenes = numberOfEnvironmentalGenes;
	}

	@Override
	public int[] getIdealAlleles() {
		throw new NotImplementedException();
	}

}
