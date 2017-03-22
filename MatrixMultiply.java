import java.util.*;
import java.io.*;

public class MatrixMultiply {
	public static void main(String[] args) {
		System.out.println("Test");
		int flag = parseInt(args[1]);
		int n = parseInt(args[2]);
		String inputfile = args[3];

		if (flag == 0) {
			int[][] A = new int[n][n];
			int[][] B = new int[n][n];
			try {
				BufferedReader in = new BufferedReader(new FileReader(inputfile));
				int i = 0;
				while (i < n) {
					A[i][j] = parseInt(in.readLine());
				}
				i = 0;
				while (i < n) {
					B[i][j] = parseInt(in.readLine());
				}
			} catch (IOException e) {
				System.out.println("Error: Incorrect input format.");
				System.exit(0);
			} finally {
				if (in != null) {
					in.close();
				}
			}
			
		}


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

	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.out.println("Error: Incorrect input format.");
			System.exit(0);
		}
		return 0;
	}	
}
