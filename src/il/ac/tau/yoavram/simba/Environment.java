package il.ac.tau.yoavram.simba;

import java.io.Serializable;

public interface Environment extends Serializable {

	public abstract void init();

	public abstract void change(double fractionOfGenesToChange);

	public abstract boolean equals(Object obj);

	public abstract long getLastEnvironmentalChange();

	public abstract int getNumberOfEnvironmentalGenes();

}