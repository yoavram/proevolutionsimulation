package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import cern.colt.Arrays;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;

public class DifeqCheck {
	private static final char SEPARATOR = '-';
	private static String input = "D:\\My Documents\\My Desktop\\_6-12.csv";

	public static void main(String[] args) {
		System.out.println("starting with " + input);
		CsvReader reader = new CsvReader(input, true);
		TreeMap<BigDecimal, Integer> map = Maps.newTreeMap(NumberUtils.createNaturalBigDecimalComparator());

		System.out.println("header: " + Arrays.toString(reader.firstRow()));
		// put data
		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {

			BigDecimal meanw = new BigDecimal(row[8]);
			int pi = Integer.parseInt(row[3]);
			map.put(meanw, pi);
		}

		// check data
		Entry<BigDecimal, Integer> e = null;
		while ((e = map.pollFirstEntry()) != null) {
			System.out
					.println(String.format("%d: %s", e.getValue(), e.getKey().toString()));
		}
	}

	public static void main1(String[] args) {
		System.out.println("starting with " + input);
		CsvReader reader = new CsvReader(input, true);
		ArrayListMultimap<String, Integer> table = ArrayListMultimap.create();

		System.out.println("header: " + Arrays.toString(reader.firstRow()));

		// put data
		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {
			String key = join(row, 0, 3);
			int val = Integer.parseInt(row[5]);
			table.put(key, val);
		}

		// check data
		for (String key : table.keySet()) {
			List<Integer> values = table.get(key);
			int first = values.get(0);
			int last = values.get(values.size() - 1);
			if (first != 0) {
				System.out.println(String.format("%s first value is not 0: %s",
						key, Arrays.toString(values.toArray())));
			}
			if (last != 1) {
				System.out.println(String.format("%s last value is not 1: %s",
						key, Arrays.toString(values.toArray())));
			}
		}

		System.out.println("finished");
	}

	public static String join(String[] arr, int start, int stop) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i <= stop; i++) {
			sb.append(arr[i]).append(SEPARATOR);
		}
		return sb.toString();
	}
}
