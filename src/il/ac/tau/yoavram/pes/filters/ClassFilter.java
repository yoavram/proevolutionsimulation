package il.ac.tau.yoavram.pes.filters;


public class ClassFilter<T> implements Filter<T> {

	private Class<?> clazz;

	@Override
	public boolean filter(T filtrate) {
		return clazz.isInstance(filtrate);
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
