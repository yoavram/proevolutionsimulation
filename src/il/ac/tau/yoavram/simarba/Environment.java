package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

/**
 * <ul>
 * <li>
 * 2 gene types: housekeeping (HK-0) and environmental (ENV-1)</li>
 * <li>3 alleles per gene (0, 1, 2):</li>
 * <ul>
 * <li>
 * HK: 0 is beneficial, 1 and 2 are deleterious</li>
 * <li>
 * ENV: 0 or 1 is beneficial, the other deleterious - 2 always deleterious.</li>
 * </ul>
 * <li>
 * gene types are randomly ordered</li> </ul>
 * 
 * @author yoavram
 * @version Charles
 */
public class Environment implements il.ac.tau.yoavram.simba.Environment {
	private static final long serialVersionUID = 6584044267519864446L;

	private static final Logger logger = Logger.getLogger(Environment.class);

	private static Environment INSTANCE = null;

	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;
	private int[] alleles;
	private int[] geneTypes;

	private transient long lastEnvironmentalChange = 0;

	private Environment() {
		INSTANCE = this;
	}

	public static Environment getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Environment();
		return INSTANCE;
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public void init() {
		geneTypes = new int[getNumberOfHousekeepingGenes()
				+ getNumberOfEnvironmentalGenes()];
		// 0 is HK
		Arrays.fill(geneTypes, 0, getNumberOfHousekeepingGenes(), 0);
		// 1 is ENV
		Arrays.fill(geneTypes, getNumberOfHousekeepingGenes(),
				geneTypes.length, 1);
		// shuffle genes
		RandomUtils.shuffleArray(geneTypes);

		alleles = new int[geneTypes.length];
		for (int gene = 0; gene < alleles.length; gene++) {
			if (geneTypes[gene] == 0) {
				alleles[gene] = 0;
			} else if (geneTypes[gene] == 1) {
				alleles[gene] = RandomUtils.coinToss() ? 0 : 1;
			} else {
				String msg = this.getClass().getName()
						+ " does not support gene type with value "
						+ geneTypes[gene] + " in position " + gene;
				logger.error(msg);
				throw new NotImplementedException(msg);
			}
		}
	}

	public void change(double fractionOfGenesToChange) {
		double toChange = Math.ceil(fractionOfGenesToChange * alleles.length);
		for (int i = 0; i < toChange; i++) {
			// pick gene
			int gene = RandomUtils.nextInt(0, alleles.length - 1);
			// make sure it is an environmental gene and not housekeeping
			while (geneTypes[gene] == 0) {
				gene = RandomUtils.nextInt(0, alleles.length - 1);
			}
			// change it (0->1, 1->0)
			alleles[gene] = (alleles[gene] + 1) % 2;
			logger.debug("Changed allele at gene " + gene + " to "
					+ alleles[gene]);
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

	/**
	 * 
	 * @return a copy of the ideal alleles
	 */
	public int[] getIdealAlleles() {
		return Arrays.copyOf(alleles, alleles.length);
	}

	public int getGeneType(int gene) {
		return geneTypes[gene];
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

	public void setNumberOfHousekeepingGenes(int numberOfHousekeepingGenes) {
		this.numberOfHousekeepingGenes = numberOfHousekeepingGenes;
	}

	public int getNumberOfHousekeepingGenes() {
		return numberOfHousekeepingGenes;
	}

}
