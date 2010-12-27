package il.ac.tau.yoavram.math;

import java.math.BigDecimal;
import java.util.Arrays;

public class LinearAlgebra {

	/**
	 * u[i] = innerProduct(m[i], v)
	 * 
	 * @param m
	 * @param v
	 * @return u
	 */
	public static BigDecimal[] unsafeMatrixOperation(BigDecimal[][] m,
			BigDecimal[] v) {
		// not checking dimensions, assuming square matrix
		BigDecimal[] u = new BigDecimal[m.length];
		for (int i = 0; i < v.length; i++) {
			u[i] = innerProduct(m[i], v);
		}
		return u;
	}

	/**
	 * u[i] = innerProduct(m[i], v)
	 * 
	 * @param m
	 * @param v
	 * @return u
	 */
	public static BigDecimal[] unsafeMatrixOperationSparse(BigDecimal[][] m,
			BigDecimal[] v) {
		// not checking dimensions, assuming square matrix
		BigDecimal[] u = new BigDecimal[m.length];
		for (int i = 0; i < v.length; i++) {
			u[i] = innerProductSparse(m[i], v);
		}
		return u;
	}

	/**
	 * @see unsafeMatrixOperation
	 */
	public static BigDecimal[] matrixOperation(BigDecimal[][] m, BigDecimal[] v) {
		for (int i = 0; i < m.length; i++) {
			if (m[i].length != v.length)
				throw new IllegalArgumentException(
						"Matrix column lengths must agree with vector length: column "
								+ i + " length " + v.length
								+ ", vector length " + v.length);
		}
		return unsafeMatrixOperation(m, v);
	}

	/**
	 * product[i] = v[i] * u[i];
	 * 
	 * @param v
	 * @param u
	 * @return product
	 */
	public static BigDecimal[] vectorProduct(BigDecimal[] v, BigDecimal u[]) {
		checkLength(v, u);
		BigDecimal[] product = new BigDecimal[v.length];
		for (int i = 0; i < v.length; i++) {
			product[i] = v[i].multiply(u[i]);
		}
		return product;
	}

	/**
	 * product[i] = v[i] * u[i] * t;
	 * 
	 * @param v
	 * @param u
	 * @param t
	 * @return product
	 */
	public static BigDecimal[] vectorProduct(BigDecimal[] v, BigDecimal u[],
			BigDecimal t) {
		checkLength(v, u);
		BigDecimal[] product = new BigDecimal[v.length];
		for (int i = 0; i < v.length; i++) {
			product[i] = v[i].multiply(u[i]).multiply(t);
		}
		return product;
	}

	public static void printMatrix(BigDecimal[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j]);
				System.out.print(", ");
			}
			System.out.println();
		}
	}

	/**
	 * product = sum( v[i] * u[i]), i from 0 to v.length
	 * 
	 * @param v
	 * @param u
	 * @return product
	 */
	public static BigDecimal innerProduct(BigDecimal[] v, BigDecimal[] u) {
		checkLength(v, u);
		BigDecimal product = BigDecimal.ZERO;
		for (int i = 0; i < v.length; i++) {
			product = product.add(v[i].multiply(u[i]));
		}
		return product;
	}

	/**
	 * product = sum( v[i] * u[i]), i from 0 to v.length
	 * 
	 * @param v
	 * @param u
	 * @return product
	 */
	public static BigDecimal innerProductSparse(BigDecimal[] v, BigDecimal[] u) {
		checkLength(v, u);
		BigDecimal product = BigDecimal.ZERO;
		for (int i = 0; i < v.length; i++) {
			if (v[i].compareTo(BigDecimal.ZERO) != 0)
				product = product.add(v[i].multiply(u[i]));
		}
		return product;
	}

	/**
	 * product = sum( v[i] * u[i] * t), i from 0 to v.length
	 * 
	 * @param v
	 * @param u
	 * @param t
	 * @return product
	 */
	public static BigDecimal innerProduct(BigDecimal[] v, BigDecimal[] u,
			BigDecimal t) {
		checkLength(v, u);
		BigDecimal product = BigDecimal.ZERO;
		for (int i = 0; i < v.length; i++) {
			product = product.add(v[i].multiply(u[i]).multiply(t));
		}
		return product;
	}

	/**
	 * sum = sum( v[i] ), i from 0 to v.length
	 * 
	 * @param v
	 * @return sum
	 */
	public static BigDecimal sum(BigDecimal[] v) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < v.length; i++) {
			sum = sum.add(v[i]);
		}
		return sum;
	}

	/**
	 * d = max( | v[i] - u[i] | ), i from 0 to v.length
	 * 
	 * @param v
	 * @param u
	 * @return d
	 */
	public BigDecimal distanceLInf(BigDecimal[] v, BigDecimal[] u) {
		checkLength(u, v);
		BigDecimal d = BigDecimal.ZERO;
		for (int i = 0; i < v.length; i++) {
			d = v[i].subtract(u[i]).abs().max(d);
		}
		return d;
	}

	/**
	 * d = sum( | v[i] - u[i] | ), i from 0 to v.length
	 * 
	 * @param v
	 * @param u
	 * @return d
	 */
	public BigDecimal distanceL1(BigDecimal[] u, BigDecimal[] v) {
		checkLength(u, v);
		BigDecimal d = BigDecimal.ZERO;
		for (int i = 0; i < u.length; i++) {
			d = d.add(u[i].subtract(v[i]).abs());
		}
		return d;
	}

	public static BigDecimal[][] transpose(BigDecimal[][] m) {
		int n = m.length;
		// check m is square
		for (int i = 0; i < n; i++) {
			if (m[i].length != n)
				throw new IllegalArgumentException(
						"Transpose only works for square matrix");
		}
		BigDecimal[][] t = new BigDecimal[n][];
		for (int j = 0; j < n; j++) {
			t[j] = new BigDecimal[n];
			for (int i = 0; i < n; i++) {
				t[j][i] = m[i][j];
			}
		}
		return t;
	}

	public static void checkLength(BigDecimal[] v, BigDecimal[] u) {
		if (v.length != u.length)
			throw new IllegalArgumentException(
					"Vector lengths must agree: u length " + v.length
							+ ", v length " + u.length);
	}
	
	public static void fillMatrix(BigDecimal[][] m, BigDecimal value) {
		for (int i = 0 ; i < m.length ; i++) {
			Arrays.fill(m[i], value);
		}
	}
}
