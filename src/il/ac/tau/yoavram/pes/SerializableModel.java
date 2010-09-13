package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.io.Serialization;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public abstract class SerializableModel<T extends Object> implements Model<T>,
		Serializable {

	private static final long serialVersionUID = 2620240837057469787L;
	private static final Logger logger = Logger
			.getLogger(SerializableModel.class);
	private static final String DEFAULT_EXTENSION = ".ser";

	private String filename;
	private String dir;
	private String extension;
	private Object id;
	private boolean serializedAtEnd;

	public SerializableModel() {
	}

	public void destroy() {
		if (isSerializedAtEnd()) {
			serialize();
		}
	}

	public String serialize() {
		String serString = getDir()+File.separator;
		if (Strings.isNullOrEmpty(getFilename())) {
			serString += getID().toString();
		} else {
			serString += getFilename();
		}
		if (!serString.endsWith(getExtension())) {
			serString += getExtension();
		}
		try {
			Serialization.writeToFile(this, serString);
			File file = new File(serString);
			logger.info("Serialized model " + getID().toString() + " to "
					+ file.getAbsolutePath());
			return serString;
		} catch (IOException e) {
			logger.debug("Failed serializing model "
					+ this.getClass().getSimpleName() + " id "
					+ getID().toString() + ": " + e);
		}
		return null;
	}

	public static <E extends SerializableModel<?>> E deserialize(String filename) {
		E deserialized = null;
		try {
			deserialized = Serialization.readFromFile(filename);
			File file = new File(filename);
			logger.info("Deserialized model " + deserialized.getID().toString()
					+ " to " + file.getAbsolutePath());
		} catch (IOException e) {
			logger.error(e);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		return deserialized;
	}

	public void setSerializedAtEnd(boolean serializedAtEnd) {
		this.serializedAtEnd = serializedAtEnd;
	}

	public boolean isSerializedAtEnd() {
		return serializedAtEnd;
	}

	public void setID(Object id) {
		this.id = id;
	}

	public Object getID() {
		return id;
	}

	public String getExtension() {
		if (Strings.isNullOrEmpty(extension))
			return DEFAULT_EXTENSION;
		return extension;
	}

	/**
	 * 
	 * @param extension
	 *            setting to null will prevent the addition of an extension to
	 *            the filename
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SerializableModel
				&& ((SerializableModel<T>) obj).getID().equals(getID());
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}

	public static class Factory {
		private static final Logger logger = Logger.getLogger(Factory.class);
		private String filename;

		public <E extends SerializableModel<?>> E deserialize() {
			E deserialized = null;
			try {
				deserialized = Serialization.readFromFile(getFilename());
				File file = new File(getFilename());
				logger.info("Deserialized model "
						+ deserialized.getID().toString() + " from "
						+ file.getAbsolutePath());
			} catch (IOException e) {
				logger.error("Deserialization failed: " + e);
			} catch (ClassNotFoundException e) {
				logger.error("Deserialization failed: " + e);
			}
			return deserialized;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getFilename() {
			return filename;
		}
	}
}
