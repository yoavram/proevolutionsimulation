/*
 *  proevolutionsimulation: an agent-based simulation framework for evolutionary biology
 *  Copyright 2010 Yoav Ram <yoavram@post.tau.ac.il>
 *
 *  This file is part of proevolutionsimulation.
 *
 *  proevolutionsimulation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License (GNU GPL v3) as published by
 *  the Free Software Foundation.
 *   
 *  proevolutionsimulation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. You are allowed to modify this code, link it with other code 
 *  and release it, as long as you keep the same license. 
 *  
 *  The content license is Creative Commons 3.0 BY-SA. 
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with proevolutionsimulation.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package il.ac.tau.yoavram.pes;

import il.ac.tau.yoavram.pes.io.Serialization;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

/**
 * This abstract implementation of {@link Model} adds serialization
 * capabilities.</br> Serializaiton is done using the {@link Serialization}
 * static class.</br> As of the writing of this doc (subject to changes in
 * {@link Serialization}), model is serialized using Java built-in
 * serialization, and then compressed using Java built-in GZIP compression.</br>
 * Any class that extends this abstract class must make sure that all it's
 * members are {@link Serializable}</br> Users are encouraged to test the
 * serialization and deserialization of their models.</br>
 * <p>
 * Deserialization can be done in two ways:</br> 1) call the static method
 * <code>deserialize(filename)</code>.</br> 2) create an instance of
 * {@link SerializableModel.Factory} and call it's
 * <code>deserialize(filename)</code> method.</br> There is no difference
 * between these methods and they co-exist for comfort. I had some problems
 * running the static method from Spring and therefore used the factory instance
 * method.</br>
 * 
 * @author user
 * 
 * @param <T>
 */
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

	/**
	 * Model will be serialized to the filename specified by <code>setDir</code>
	 * , <code>setFilename</code>, and <code>setExtension</code>. Default
	 * extensions is '.ser'.</br> If no filename is specified then the model's
	 * ID will be used (<code>getID</code>).
	 * 
	 * @return the filepath to which the model was serialized
	 */
	public String serialize() {
		String serString = getDir() + File.separator;
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
				logger.fatal("Deserialization failed: " + e);
			} catch (ClassNotFoundException e) {
				logger.fatal("Deserialization failed: " + e);
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
