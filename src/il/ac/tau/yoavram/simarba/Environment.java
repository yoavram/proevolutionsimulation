package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.random.Distribution;
import il.ac.tau.yoavram.pes.utils.RandomUtils;

import java.io.ObjectInputStream;
import java.util.Arrays;

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

	protected static Environment INSTANCE = null;

	private int numberOfEnvironmentalGenes;
	private int numberOfHousekeepingGenes;
	private int numberOfModifierGenes;
	private Distribution selectionDistribution;

	private float[] alleles;
	private GeneType[] geneTypes;
	private double[] selectionCoefficients;

	public enum GeneType {
		HK, ENV, MOD
	}

	private transient long lastEnvironmentalChange = 0;

	protected Environment() {
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
		geneTypes = new GeneType[getNumberOfHousekeepingGenes()
				+ getNumberOfEnvironmentalGenes() + getNumberOfModifierGenes()];

		for (int gene = 0; gene < geneTypes.length; gene++) {
			if (gene < getNumberOfHousekeepingGenes())
				geneTypes[gene] = GeneType.HK;
			else if (gene < getNumberOfEnvironmentalGenes())
				geneTypes[gene] = GeneType.ENV;
			else {
				geneTypes[gene] = GeneType.MOD;
			}
		}

		// shuffle genes
		RandomUtils.shuffleArray(geneTypes);

		alleles = new float[geneTypes.length];
		selectionCoefficients = new double[geneTypes.length];
		for (int gene = 0; gene < alleles.length; gene++) {
			if (geneTypes[gene].equals(GeneType.HK)) {
				alleles[gene] = 0;
				selectionCoefficients[gene] = selectionDistribution
						.nextDouble();
			} else if (geneTypes[gene].equals(GeneType.ENV)) {
				alleles[gene] = RandomUtils.coinToss() ? 0 : 1;
				selectionCoefficients[gene] = selectionDistribution
						.nextDouble();
			} else if (geneTypes[gene].equals(GeneType.MOD)) { // MODIFIERS
				alleles[gene] = -1;
				selectionCoefficients[gene] = Double.NaN;
			}
		}
	}

	public void change(double fractionOfGenesToChange) {
		double toChange = Math.ceil(fractionOfGenesToChange * alleles.length);
		for (int i = 0; i < toChange; i++) {
			// pick gene
			int gene = RandomUtils.nextInt(0, alleles.length - 1);
			// make sure it is an environmental gene
			while (!geneTypes[gene].equals(GeneType.ENV)) {
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

	public float idealAllele(int gene) {
		return alleles[gene];
	}

	/**
	 * 
	 * @return a copy of the ideal alleles
	 */
	public float[] idealAlleles() {
		return Arrays.copyOf(alleles, alleles.length);
	}

	public GeneType geneType(int gene) {
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

	public void setNumberOfModifierGenes(int numberOfModifierGenes) {
		this.numberOfModifierGenes = numberOfModifierGenes;
	}

	public int getNumberOfModifierGenes() {
		return numberOfModifierGenes;
	}

	public int getNumberOfGenes() {
		return alleles.length;
	}

	public Distribution getSelectionDistribution() {
		return selectionDistribution;
	}

	public void setSelectionDistribution(Distribution selectionDistribution) {
		this.selectionDistribution = selectionDistribution;
	}

	public double getSelectionCoefficient(int gene) {
		return selectionCoefficients[gene];
	}
}
