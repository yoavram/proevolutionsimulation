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

	private GenomicMemory genmicMemory = null; // for serialization
	private boolean transformations = true;

	@Override
	public void init() {
		super.init();
		if (getGenmicMemory() == null && isTransformations()) {
			setGenmicMemory(new GenomicMemory());
		}

	}

	@Override
	public void step() {
		super.step();
		// transformations
		if (isTransformations()) {
			for (Bacteria bacteria : getPopulations().get(0)) {
				int numOfTrans = RandomUtils.nextPoisson(bacteria
						.getTransformationRate());
				if (numOfTrans > 0) {
					logger.debug(String.format(
							"Bacteria %d has %d transformations",
							bacteria.getID(), numOfTrans));
					for (int i = 0; i < numOfTrans; i++) {
						bacteria.transform();
					}
				}
			}
		}
	}

	/* GETTERS AND SETTERS */

	public void setGenmicMemory(GenomicMemory genmicMemory) {
		this.genmicMemory = genmicMemory;
	}

	public GenomicMemory getGenmicMemory() {
		return genmicMemory;
	}

	public void setTransformations(boolean transformations) {
		this.transformations = transformations;
	}

	public boolean isTransformations() {
		return transformations;
	}

}
