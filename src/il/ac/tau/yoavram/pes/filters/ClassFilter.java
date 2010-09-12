package il.ac.tau.yoavram.pes.filters;

public class ClassFilter<T> implements Filter<T> {

	private Class<? extends T> clazz;

	@Override
	public boolean filter(T filtrate) {
		return getClazz().equals(filtrate.getClass());
	}

	public void setClazz(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
