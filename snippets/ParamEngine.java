package il.ac.tau.yoavram.pes.params;

public interface ParamEngine {
	<T> T getParam(String paramName);
}
