package il.ac.tau.yoavram.pes.filters;

public class AndFilter<T> implements Filter<T> {
	protected Filter<T> filter1;
	protected Filter<T> filter2;
	
	@Override
	public boolean filter(T filtrate) {
		return getFilter1().filter(filtrate) && getFilter2().filter(filtrate);
	}

	public Filter<T> getFilter1() {
		return filter1;
	}

	public void setFilter1(Filter<T> filter1) {
		this.filter1 = filter1;
	}

	public Filter<T> getFilter2() {
		return filter2;
	}

	public void setFilter2(Filter<T> filter2) {
		this.filter2 = filter2;
	}

}
