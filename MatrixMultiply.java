import java.util.*;

public class MatrixMultiply {
	public static void main(String args[]) {
		System.out.println("Test");
	}


	// assume c is all zeroes
	public static void regMult(int[][] a, int[][] b, int[][] c, int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				for (int k = 0; k < len; k++) {
					c[i+ci][j+cj] += a[i+ai][k+aj] * b[k+bi][j+bj];
				}
			}
		}
		return;
	}

	public static void strassen(int[][] a, int[][] b, int[][] c, int[][] p1, int[][] p2, int[][] p3, int[][] p4, int[][] p5, int[][] p6, int[][] p7, int ai, int aj, int bi, int bj, int ci, int cj, int len, int crossover) {
		// p6
		subtract(a, b, p1, ai + len/2, aj)
	}
}
