package il.ac.tau.yoavram.pes.io.gzip;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GZIPFileReader extends InputStreamReader {

	public GZIPFileReader(String fileName) throws IOException {
		super(new GZIPInputStream(new FileInputStream(fileName)));
	}

	public GZIPFileReader(File file) throws IOException {
		super(new GZIPInputStream(new FileInputStream(file)));
	}

	public GZIPFileReader(FileDescriptor fd) throws IOException {
		super(new GZIPInputStream(new FileInputStream(fd)));
	}
}
