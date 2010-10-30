package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;

import com.google.common.base.Strings;
import com.google.common.io.LineReader;

@SuppressWarnings("rawtypes")
public class CsvMerger extends DirectoryWalker {
	private PrintWriter writer;
	private boolean firstFileInDir = true;
	private File output;
	private FileFilter csvFilter = FileFilterUtils.suffixFileFilter(".csv");

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java " + CsvMerger.class.getSimpleName()
					+ " <directory name>");
			System.exit(1);
		}
		CsvMerger merger = new CsvMerger();
		System.out.println("Starting " + CsvMerger.class.getSimpleName()
				+ " on " + args[0]);
		merger.start(args[0]);
		System.out.println(CsvMerger.class.getSimpleName() + " finished on "
				+ args[0]);
	}

	public void start(String directoryName) {
		try {
			walk(new File(directoryName), null);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	protected void handleDirectoryStart(File directory, int depth,
			Collection results) throws IOException {
		output = new File(directory, directory.getName() + ".csv");
		writer = new PrintWriter(output);
		firstFileInDir = true;
		System.out.println("Walking directory " + directory.getAbsolutePath());
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		if (csvFilter.accept(file) && !file.equals(output)) {
			System.out.println("Reading " + file.getName());
			LineReader reader = new LineReader(new FileReader(file));
			String line = reader.readLine();
			if (firstFileInDir && !Strings.isNullOrEmpty(line)) {
				firstFileInDir = false;
				writer.println(line);
				System.out.println("Header: " + line);
			}
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
		}
	}

	@Override
	protected void handleDirectoryEnd(File directory, int depth,
			Collection results) throws IOException {
		writer.close();
		System.out.println("Finished directory " + directory.getAbsolutePath());
	}
}
