package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestStuff {

	public static void main(String[] args) {
	}

	// @org.junit.Test
	public void testList() {
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

	@org.junit.Test
	public void testDate() {
		System.out.println(Calendar.getInstance().getTime().toString());

	}
}
