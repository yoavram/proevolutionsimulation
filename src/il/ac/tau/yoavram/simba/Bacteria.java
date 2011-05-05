package il.ac.tau.yoavram.simba;

import java.io.Serializable;

public interface Bacteria extends Serializable {

	public abstract void die();

	public abstract Bacteria spawn();

	public abstract Bacteria reproduce();

	public abstract void mutate();

	public abstract double getFitness();

	public abstract int getID();

	public abstract boolean equals(Object obj);

	public abstract double getMutationRate();

	public abstract double getTransformationRate();

	public abstract int recombinate();

	public abstract double getMutationThreshold();

	public abstract double getTransformationThreshold();

}