package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;

public class SimpleEnvironment implements Environment {
	private static final long serialVersionUID = 8898297052351732121L;

	private static final Logger logger = Logger.getLogger(Environment.class);

	private static SimpleEnvironment INSTANCE = null;

	private int numberOfEnvironmentalGenes;
	protected int[] alleles;
	protected transient long lastEnvironmentalChange = 0;

	// TODO make this private and lazy init in getInstance, and remove set
	// instance from readObject()
	public SimpleEnvironment() {
		INSTANCE = this;
	}

	public static SimpleEnvironment getInstance() {
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
	public Map<Integer, Integer> change(double fractionOfGenesToChange) {
		int toChange = (int) Math
				.ceil(fractionOfGenesToChange * alleles.length);
		Map<Integer, Integer> genesChanged = Maps.newTreeMap();
		for (int i = 0; i < toChange; i++) {
			int gene = chooseRandomGeneToChange();
			int newAllele = changeGene(gene);
			genesChanged.put(gene, newAllele);
		}
		if (toChange > 0) {
			lastEnvironmentalChange = getTick();
		}
		return genesChanged;
	}

	protected int chooseRandomGeneToChange() {
		return RandomUtils.nextInt(0, alleles.length - 1);
	}

	protected int changeGene(int gene) {
		int currentAllele = alleles[gene];
		int newAllele = (currentAllele + 1) % 2;
		alleles[gene] = newAllele;
		geneChangedMsg(gene, currentAllele, newAllele);
		return newAllele;
	}

	protected void geneChangedMsg(int gene, int currentAllele, int newAllele) {
		logger.info("Changed the environmental gene " + gene + " from "
				+ currentAllele + " to " + newAllele);
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

	public int getIdealAllele(int gene) {
		return alleles[gene];
	}

	public long getLastEnvironmentalChange() {
		return lastEnvironmentalChange;
	}

	protected long getTick() {
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

	public int[] getIdealAlleles() {
		return Arrays.copyOf(alleles, alleles.length);
	}

}
