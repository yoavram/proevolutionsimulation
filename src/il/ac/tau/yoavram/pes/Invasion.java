package il.ac.tau.yoavram.pes;

import java.util.List;

public interface Invasion<T, E extends T> {
	List<List<T>> invade(List<List<T>> populations);

	public double getInvasionRate();

	public void setInvasionRate(double invasionRate);

	String getInvaderName();
}
