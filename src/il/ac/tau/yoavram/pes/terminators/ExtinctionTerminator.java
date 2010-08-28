package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Model;

import java.util.List;

import org.apache.log4j.Logger;

public class ExtinctionTerminator extends AbstractTerminator implements
		Terminator {
	private static final Logger logger = Logger
			.getLogger(ExtinctionTerminator.class);

	Model<?> model;
	Class<?> clazz;

	@Override
	public boolean terminate() {
		for (List<?> population : getModel().getPopulations()) {
			for (Object obj : population) {
				if (clazz.equals(obj.getClass())) {
					return false;
				}
			}
		}

		return true;
	}

	public Model<?> getModel() {
		return model;
	}

	public void setModel(Model<?> model) {
		this.model = model;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getClazzName() {
		return clazz.getName();
	}

	public void setClazzName(String clazzName) {
		try {
			setClazz(Class.forName(clazzName));
		} catch (ClassNotFoundException e) {
			logger.error("Can't find a class named '" + clazzName + "': " + e);
		}
	}
}
