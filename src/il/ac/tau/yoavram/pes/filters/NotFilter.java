package il.ac.tau.yoavram.pes.filters;

public class NotFilter<T> implements Filter<T> {
	private Filter<T> filter;

	@Override
	public boolean filter(T filtrate) {
		return !filter.filter(filtrate);
	}

	public void setFilter(Filter<T> filter) {
		this.filter = filter;
	}

	public Filter<T> getFilter() {
		return filter;
	}

}
