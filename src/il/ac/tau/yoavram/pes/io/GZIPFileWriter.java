package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

/**
 * code copied from {@link FileWriter}
 * @author yoavram
 *
 */
public class GZIPFileWriter extends OutputStreamWriter {
	public GZIPFileWriter(String fileName) throws IOException {
		super(new GZIPOutputStream(new FileOutputStream(fileName)));
	}

	public GZIPFileWriter(String fileName, boolean append) throws IOException {
		super(new GZIPOutputStream(new FileOutputStream(fileName, append)));
	}

	public GZIPFileWriter(File file) throws IOException {
		super(new GZIPOutputStream(new FileOutputStream(file)));
	}

	public GZIPFileWriter(File file, boolean append) throws IOException {
		super(new GZIPOutputStream(new FileOutputStream(file, append)));
	}

	public GZIPFileWriter(FileDescriptor fd) throws IOException {
		super(new GZIPOutputStream(new FileOutputStream(fd)));
	}
}
