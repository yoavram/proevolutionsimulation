package il.ac.tau.yoavram.pes.filters;

public class ClassNameFilter<T> implements Filter<T> {
	private String className;

	@Override
	public boolean filter(T filtrate) {
		return getClassName().equals(filtrate.getClass().getSimpleName());
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


}
