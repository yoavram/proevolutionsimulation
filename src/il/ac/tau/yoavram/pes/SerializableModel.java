package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.io.Serialization;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.google.common.base.Joiner;

public abstract class SerializableModel<T> implements Model<T>, Serializable {

	private static final long serialVersionUID = 2620240837057469787L;
	private static final Logger logger = Logger
			.getLogger(SerializableModel.class);

	private String filename;
	private String extension = "ser";
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
		String serString = Joiner.on('.').skipNulls()
				.join(getFilename(), getID().toString(), getExtension());
		try {
			Serialization.writeToFile(this, serString);
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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SerializableModel 
				&& ((SerializableModel<T>) obj).getID().equals(getID());
	}
}
