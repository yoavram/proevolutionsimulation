package il.ac.tau.yoavram.simarba;

import il.ac.tau.yoavram.pes.utils.RandomUtils;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.SimbaModel;

import org.apache.log4j.Logger;

/**
 * @author yoavram
 * @version Charles
 */
public class SimarbaModel extends SimbaModel {
	private static final long serialVersionUID = -855542231005863606L;
	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

	private GenomicMemory genomicMemory = null;

	@Override
	public void init() {
		super.init();
		if (genomicMemory == null) {
			genomicMemory = new GenomicMemory();
			genomicMemory.setCapacity(getPopulationSize() / 100);
			logger.warn(String.format(
					"No %s set, created one with capacity %d", genomicMemory
							.getClass().getSimpleName(), getGenomicMemory()
							.getCapacity()));
		}
	}

	@Override
	public void step() {
		super.step();
		// transformations
		Bacteria bacteria = randomBacteria();
		int numOfTrans = RandomUtils.nextPoisson(bacteria
				.getTransformationRate());
		if (numOfTrans > 0) {
			for (int i = 0; i < numOfTrans; i++) {
				int num = bacteria.recombinate();
				logger.debug(String.format("%s %d recombinated %d alleles",
						bacteria.getClass().getSimpleName(), bacteria.getID(),
						num));
			}
		}
	}

	/* GETTERS AND SETTERS */

	public void setGenomicMemory(GenomicMemory genmicMemory) {
		this.genomicMemory = genmicMemory;
	}

	public GenomicMemory getGenomicMemory() {
		return genomicMemory;
	}
}
