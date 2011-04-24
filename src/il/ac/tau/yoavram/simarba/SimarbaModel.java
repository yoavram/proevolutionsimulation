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

	private static final long serialVersionUID = 864378378066751467L;

	private static final Logger logger = Logger.getLogger(SimarbaModel.class);

	private GenomicMemory genomicMemory = null; // for serialization

	@Override
	public void step() {
		super.step();
		// transformations
		Bacteria bacteria = randomBacteria();
		int numOfTrans = RandomUtils.nextPoisson(bacteria
				.getTransformationRate());
		if (numOfTrans > 0) {
			logger.debug(String.format("Bacteria %d has %d transformations",
					bacteria.getID(), numOfTrans));
			for (int i = 0; i < numOfTrans; i++) {
				bacteria.transform();
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
