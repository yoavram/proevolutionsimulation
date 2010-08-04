package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestList {
	@org.junit.Test
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(0, 0);
		for (int j = 0; j < 5; j++) {
			int k = 0;
			for (int i : list) {
				System.out.println(k + "?=" + i);
				assertTrue(k == i);
				k++;
			}
		}
	}
}
