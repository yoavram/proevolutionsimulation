package il.ac.tau.yoavram.simba;

import java.io.Serializable;

public interface Bacteria extends Serializable{

	public abstract void die();

	public abstract Bacteria spawn();

	public abstract Bacteria reproduce();

	public abstract void transform();

	public abstract void mutate();

	public abstract double getFitness();

	public abstract int getID();

	public abstract boolean equals(Object obj);

	public abstract double getMutationRate();

	public abstract double getTransformationRate();

	public abstract double getSelectionCoefficient();

	public abstract boolean isMutator();

	public abstract boolean isSim();

	public abstract boolean isCm();

}