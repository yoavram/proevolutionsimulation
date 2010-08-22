package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialization {

	public static String writeToFile(Serializable object, String filename)
			throws IOException {
		File file = new File(filename);
		ObjectOutputStream stream = null;
		try {
			file.createNewFile();
			stream = new ObjectOutputStream(new FileOutputStream(file));
			stream.writeObject(object);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return file.getAbsolutePath();
	}

	public static <T> T readFromFile(String filename) throws IOException,
			ClassNotFoundException {
		File file = new File(filename);
		if (!file.exists()) {
			throw new FileNotFoundException("File '" + filename
					+ "' doesn't exists, can't deserialize from it");
		} else if (!file.canRead()) {
			throw new IOException("Can't read from file '" + filename
					+ "', can't serialize from it");
		}
		ObjectInputStream stream = null;
		Object object = null;
		try {
			stream = new ObjectInputStream(new FileInputStream(file));
			object = stream.readObject();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		@SuppressWarnings("unchecked")
		T t = (T) object;
		return t;
	}
}
