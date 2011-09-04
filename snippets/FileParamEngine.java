package il.ac.tau.yoavram.pes.params;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertyConverter;
import org.apache.commons.configuration.reloading.InvariantReloadingStrategy;

public class FileParamEngine implements ParamEngine {

	PropertiesConfiguration prop = null;

	String filename = null;

	public FileParamEngine() {
	}

	public FileParamEngine(String filename) throws ConfigurationException {
		setFilename(filename);
		init();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void init() throws ConfigurationException {
		prop = new PropertiesConfiguration(getFilename());
		prop.setAutoSave(false);
		prop.setReloadingStrategy(new InvariantReloadingStrategy());
		prop.setThrowExceptionOnMissing(true);
	}

	private Object get(String paramName) {
		Object p = prop.getProperty(paramName);
		if (p == null) {
			throw new IllegalArgumentException("Parameter '" + paramName
					+ "' not found in parameters");
		}

		// BOOLEAN
		try {
			return PropertyConverter.toBoolean(p);
		} catch (ConversionException e) {
		}
		// INTEGER
		try {
			return PropertyConverter.toInteger(p);
		} catch (ConversionException e) {
		}
		// DOUBLE
		try {
			return PropertyConverter.toDouble(p);
		} catch (ConversionException e) {
		}
		// STRING - DEFAULT
		return p.toString();

	}

	@Override
	public <T> T getParam(String paramName) {
		T t = null;
		try {
			t = (T) get(paramName);
		} catch (ClassCastException e) {
			throw new ClassCastException("Can't cast parameter value for '"
					+ paramName + "': " + e.getMessage());
		}

		return t;
	}
}
