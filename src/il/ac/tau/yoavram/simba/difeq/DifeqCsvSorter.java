package il.ac.tau.yoavram.simba.difeq;

import il.ac.tau.yoavram.pes.io.csv.AbstractCsvSorter;
import il.ac.tau.yoavram.simba.difeq.DifeqCsvSorter.DifeqDataSet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;

import com.google.common.collect.TreeMultimap;

public class DifeqCsvSorter extends AbstractCsvSorter<DifeqDataSet> {
	//private static final Logger logger = Logger.getLogger(DifeqCsvSorter.class);
	public static void main(String[] args) throws IOException {
		DifeqCsvSorter s = new DifeqCsvSorter();
		s.sort(args[0]);
	}
	

	@Override
	protected TreeMultimap<DifeqDataSet, String[]> createMap() {
		return TreeMultimap.create(DifeqDataSetNaturalComparator.create(),
				AbstractCsvSorter.StringArrayComparator.create());
	}

	@Override
	protected DifeqDataSet getField(String[] row) {
		return new DifeqDataSet(row);
	}

	public static class DifeqDataSet implements Comparable<DifeqDataSet> {
		// n,s,tau,pi,gamma,phi,precision,iter,meanW
		Integer n;
		Double s;
		Integer pi;
		Double gamma;
		Double tau;
		Double phi;
		Integer precision;
		BigDecimal meanW;

		public DifeqDataSet(String[] row) {
			this(row, 0);
		}
		
		public DifeqDataSet(String[] row, int addPrecision) {
			n = Integer.parseInt(row[0]);
			s = Double.parseDouble(row[1]);
			tau = Double.parseDouble(row[2]);
			pi = Integer.parseInt(row[3]);
			gamma = Double.parseDouble(row[4]);
			phi = Double.parseDouble(row[5]);
			precision = Integer.parseInt(row[6]);
			row[8]=row[8].substring(0,precision+addPrecision+100);
			meanW = new BigDecimal(row[8], new MathContext(precision+addPrecision));
		}

		@Override
		public int compareTo(DifeqDataSet o) {
			int cmp = n.compareTo(o.n);
			if (cmp == 0) {
				cmp = s.compareTo(o.s);
			}
			if (cmp == 0) {
				cmp = tau.compareTo(o.tau);
			}
			if (cmp == 0) {
				cmp = gamma.compareTo(o.gamma);
			}
			if (cmp == 0) {
				cmp = phi.compareTo(o.phi);
			}
			if (cmp == 0) {
				cmp = meanW.compareTo(o.meanW);
			}
			return cmp;
		}
		
	}

	public static class DifeqDataSetNaturalComparator implements
			Comparator<DifeqDataSet> {

		@Override
		public int compare(DifeqDataSet o1, DifeqDataSet o2) {
			return o1.compareTo(o2);
		}

		public static Comparator<? super DifeqDataSet> create() {
			return new DifeqDataSetNaturalComparator();
		}

	}
}
