package il.ac.tau.yoavram.pes.tests;

import java.io.File;

public class TestCwd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = System.getProperty("user.dir");
		System.out.println(s);
		File f = new File("output");
		f.mkdir();
	}

}
