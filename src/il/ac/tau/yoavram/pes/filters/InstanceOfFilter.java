package il.ac.tau.yoavram.pes.filters;

public class InstanceOfFilter<T> implements Filter<T> {

	private Class<T> clazz;

	@Override
	public boolean filter(T filtrate) {
		return getClazz().isInstance(filtrate);
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
