package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.io.Serialization;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

public abstract class SerilizableModel<T> implements Model<T>, Serializable {

	private static final long serialVersionUID = 2620240837057469787L;
	private static final Logger logger = Logger
			.getLogger(SerilizableModel.class);

	private String filename;
	private Date time;
	private boolean serializedAtEnd;

	public void destroy() {
		if (isSerializedAtEnd()) {
			serialize();
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void serialize() {
		try {
			Serialization.writeToFile(this,
					getFilename() + "." + TimeUtils.formatDate(getTime()));
		} catch (IOException e) {
			logger.debug("Failed serializing model "
					+ this.getClass().getSimpleName() + " created "
					+ TimeUtils.formatDate(getTime()) + ": " + e);
		}
	}

	public void deserialize() {
		// TODO
	}

	public void setSerializedAtEnd(boolean serializedAtEnd) {
		this.serializedAtEnd = serializedAtEnd;
	}

	public boolean isSerializedAtEnd() {
		return serializedAtEnd;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}
}
